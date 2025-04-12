package com.jozefv.whatsnew.feat_articles.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jozefv.whatsnew.core.domain.util.JustError
import com.jozefv.whatsnew.core.domain.LocalArticlesRepository
import com.jozefv.whatsnew.core.domain.util.ResultHandler
import com.jozefv.whatsnew.core.domain.SessionStorage
import com.jozefv.whatsnew.core.domain.util.CoroutineDispatchers
import com.jozefv.whatsnew.core.domain.util.ErrorResult
import com.jozefv.whatsnew.core.presentation.util.toUiText
import com.jozefv.whatsnew.feat_articles.domain.articles.ArticlesRepository
import com.jozefv.whatsnew.feat_articles.domain.filter.FiltersStorage
import com.jozefv.whatsnew.feat_articles.domain.filter.FilterCategories
import com.jozefv.whatsnew.feat_articles.domain.search.SuggestionsStorage
import com.jozefv.whatsnew.feat_articles.presentation.filter.model.FilterUi
import com.jozefv.whatsnew.feat_articles.presentation.filter.model.toFilter
import com.jozefv.whatsnew.feat_articles.presentation.filter.model.toFilterUi
import com.jozefv.whatsnew.feat_articles.presentation.articles.model.ArticleUi
import com.jozefv.whatsnew.feat_articles.presentation.articles.model.toArticleLocal
import com.jozefv.whatsnew.feat_articles.presentation.articles.model.toArticleUi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class SharedArticlesViewModel(
    private val articlesRepository: ArticlesRepository,
    private val localArticlesRepository: LocalArticlesRepository,
    private val sessionStorage: SessionStorage,
    private val filtersStorage: FiltersStorage,
    private val suggestionsStorage: SuggestionsStorage,
    private val coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {

    var state by mutableStateOf(ArticlesState())
        private set

    private val _channel = Channel<SharedArticlesEvent>()
    val channel = _channel.receiveAsFlow()

    private val allSelectedUiFilters = mutableStateListOf<FilterUi>()
    private val cachedFilters = mutableListOf<FilterUi>()
    private var cachedExcludedCategory: Boolean = false
    private var cachedSearch: SearchState = SearchState()


    init {
        viewModelScope.launch(coroutineDispatchers.mainImmediate) {
            state = state.copy(isLoadingArticles = true)
            state = state.copy(isLoggedIn = sessionStorage.getUser()?.isLoggedIn == true)
            if (state.isLoggedIn) {
                getFilters()
                getSearchSettings()
            }
            getArticles()
            state = state.copy(isLoadingArticles = false)
        }
    }

    fun onAction(action: ArticlesAction) {
        when (action) {
            // If we are at the bottom of the list - load more data
            ArticlesAction.OnLoadMoreArticles -> {
                viewModelScope.launch(coroutineDispatchers.mainImmediate) {
                    state = state.copy(isLoadingMoreArticles = true)
                    loadMoreArticles()
                    state = state.copy(isLoadingMoreArticles = false)
                }
            }

            // Pull down or refresh click
            ArticlesAction.OnRefresh -> {
                viewModelScope.launch(coroutineDispatchers.mainImmediate) {
                    state = state.copy(isLoadingArticles = true)
                    refreshArticles()
                    state = state.copy(isLoadingArticles = false)
                }
            }

            is ArticlesAction.OnSaveArticle -> {
                viewModelScope.launch(coroutineDispatchers.mainImmediate) {
                    state = state.copy(isLoadingArticleSave = true)
                    val delay = async(coroutineDispatchers.mainImmediate) { delay(1000) }
                    val result =
                        localArticlesRepository.upsertArticle(
                            action.article.toArticleLocal(
                                coroutineDispatchers
                            )
                        )
                    // Always show the loading indicator for at least 1s. If upsert article took
                    // longer, in the mean time, delay would be done ant it will not delay our coroutine
                    // after result is done
                    delay.await()
                    when (result) {
                        is ResultHandler.Success -> {
                            _channel.send(SharedArticlesEvent.SaveSuccess)
                        }

                        is ResultHandler.Error -> {
                            _channel.send(SharedArticlesEvent.ErrorEventShared(result.toUiText()))
                        }
                    }
                    state = state.copy(isLoadingArticleSave = false)
                }
            }

            // Filter
            FilterAction.OnFilterOpen -> {
                // Start with fresh cached data
                allSelectedUiFilters.removeAll(allSelectedUiFilters)
                allSelectedUiFilters.addAll(cachedFilters)
                state =
                    state.copy(
                        filterState = state.filterState.copy(
                            selectedFilters = cachedFilters,
                            excludeFilterCategory = cachedExcludedCategory
                        )
                    )
            }

            FilterAction.OnRemoveAllFilters -> {
                allSelectedUiFilters.removeAll(allSelectedUiFilters)
                state =
                    state.copy(filterState = state.filterState.copy(selectedFilters = emptyList()))
            }

            is FilterAction.OnSelectFilter -> {
                viewModelScope.launch(coroutineDispatchers.mainImmediate) { selectFilter(action) }
            }

            FilterAction.OnFilter -> {
                viewModelScope.launch(coroutineDispatchers.mainImmediate) {
                    state = state.copy(isLoadingArticles = true)
                    // We want to work with static list as allSelectedUi filters notify state with every change
                    val selectedFilters = state.filterState.selectedFilters.toList()
                    val filters = selectedFilters.map { it.toFilter() }
                    if (state.isLoggedIn) {
                        if (withoutError(filtersStorage.saveFilters(filters)) && withoutError(
                                filtersStorage.saveExcludeCategory(state.filterState.excludeFilterCategory)
                            )
                        ) {
                            cachedExcludedCategory = state.filterState.excludeFilterCategory
                        }
                    }
                    // Always start with a new cache
                    cachedFilters.removeAll(cachedFilters)
                    cachedFilters.addAll(selectedFilters)
                    getArticles(event = SharedArticlesEvent.FilterSuccess)
                    // What filters we want to show on the news screen
                    val filtersForDisplay = if (state.filterState.excludeFilterCategory) {
                        selectedFilters.filterNot { it.filterCategory == FilterCategories.CATEGORY_FILTERS }
                    } else {
                        selectedFilters
                    }
                    state = state.copy(
                        isLoadingArticles = false,
                        filterState = state.filterState.copy(filtersForDisplay = filtersForDisplay)
                    )
                }
            }

            FilterAction.OnExcludeCategories -> {
                state = state.copy(
                    filterState = state.filterState.copy(
                        excludeFilterCategory = !state.filterState.excludeFilterCategory
                    )
                )
            }
            // Chip filter
            is InfoChipAction.OnFilterArticles -> {
                sortBasedOnFilter(action)
            }

            InfoChipAction.OnRemoveSearchQuery -> {
                viewModelScope.launch(coroutineDispatchers.mainImmediate) {
                    state = state.copy(
                        isLoadingArticles = true,
                        searchState = state.searchState.copy(query = "")
                    )
                    //getArticles(ArticlesEvent.FilterSuccess)
                    getArticles()
                    // Erase only query from the cached state
                    cachedSearch = cachedSearch.copy(query = "")
                    state = state.copy(isLoadingArticles = false)
                }
            }
            // Search
            SearchAction.OnSearchOpen -> {
                state = state.copy(searchState = cachedSearch)
            }

            SearchAction.OnSearchWithFilters -> {
                state = state.copy(
                    searchState = state.searchState.copy(
                        searchWithFilters = !state.searchState.searchWithFilters
                    )
                )
            }

            SearchAction.OnSearchInTitle -> {
                state = state.copy(
                    searchState = state.searchState.copy(
                        searchInTitle = !state.searchState.searchInTitle
                    )
                )
            }

            SearchAction.OnSearchExactPhrases -> {
                state = state.copy(
                    searchState = state.searchState.copy(
                        searchExactPhrases = !state.searchState.searchExactPhrases
                    )
                )
            }

            is SearchAction.OnQuery -> {
                state = state.copy(
                    searchState = state.searchState.copy(
                        query = action.query
                    )
                )
            }

            is SearchAction.OnQuerySearch -> {
                viewModelScope.launch(coroutineDispatchers.mainImmediate) {
                    //state = state.copy(isLoadingArticles = true)
                    val query = state.searchState.query
                    val searchWithFilters = state.searchState.searchWithFilters
                    val searchInTitle = state.searchState.searchInTitle
                    val searchExactPhrases = state.searchState.searchExactPhrases
                    val inList = state.searchState.querySuggestions?.contains(query) == true
                    if (query.isNotBlank() && query.length <= 100) {
                        state =
                            state.copy(searchState = state.searchState.copy(isLoadingSearch = true))
                        var saveSearchSuggestions = true
                        if (!inList && state.isLoggedIn) {
                            val suggestions =
                                buildList<String> {
                                    // We want to have the most recent insertions at the top
                                    add(query)
                                    state.searchState.querySuggestions?.let {
                                        addAll(it)
                                    }
                                }
                            state =
                                state.copy(searchState = state.searchState.copy(querySuggestions = suggestions))
                            saveSearchSuggestions =
                                withoutError(suggestionsStorage.saveSuggestions(suggestions))
                        }
                        val saveSearchFilters =
                            withoutError(suggestionsStorage.saveSearchWithFilters(searchWithFilters))
                        val saveSearchInTitle =
                            withoutError(suggestionsStorage.saveSearchInTitle(searchInTitle))
                        val saveSearchExactPhrases =
                            withoutError(
                                suggestionsStorage.saveSearchExactPhrases(
                                    searchExactPhrases
                                )
                            )
                        state = state.copy(searchState = state.searchState.copy(query = query))
                        if (saveSearchSuggestions && saveSearchFilters && saveSearchInTitle && saveSearchExactPhrases) {
                            // Reset isLoading state - so we don't cache it
                            cachedSearch = state.searchState.copy(
                                isLoadingSearch = false,
                                searchWithFiltersVisible = searchWithFilters
                            )
                        }
                        getArticles(event = SharedArticlesEvent.SearchSuccess)
                        state =
                            state.copy(searchState = state.searchState.copy(isLoadingSearch = false))
                        //state = state.copy(isLoadingArticles = false)
                    }
                }
            }

            SearchAction.OnDeleteAllQuerySuggestions -> {
                viewModelScope.plus(coroutineDispatchers.mainImmediate).launch {
                    suggestionsStorage.deleteAllSuggestions()
                    state =
                        state.copy(
                            searchState = state.searchState.copy(
                                query = "",
                                querySuggestions = null
                            )
                        )
                    cachedSearch = cachedSearch.copy(
                        query = "",
                        querySuggestions = null
                    )
                }
            }
        }
    }

    private suspend fun getArticles(event: SharedArticlesEvent? = null) {
        val query = state.searchState.query
        when (
            val result = articlesRepository.getArticles(
                isLoggedIn = state.isLoggedIn,
                allSelectedFilters = state.filterState.selectedFilters.map { it.toFilter() },
                query.ifBlank { null }
            )
        ) {
            is ResultHandler.Success -> {
                state = state.copy(
                    articles = result.data.map { it.toArticleUi() },
                    refreshedTime = getLocalTime()
                )
                event?.let {
                    _channel.send(it)
                }
            }

            is ResultHandler.Error -> {
                // When there is a success action, there can be also failed action
                // Provide possible error event, else show error as a state
                if (event != null) {
                    _channel.send(SharedArticlesEvent.ErrorEventShared(result.toUiText()))
                } else {
                    state = state.copy(error = result.toUiText())
                }
            }
        }
        // Reset our main screen sorting to default
        state = state.copy(
            filteredArticles = null,
            selectedFilterChip = null
        )
    }

    private suspend fun loadMoreArticles() {
        val query = state.searchState.query
        val result = articlesRepository.getMoreArticles(
            isLoggedIn = state.isLoggedIn,
            allSelectedFilters = state.filterState.selectedFilters.map { it.toFilter() },
            query = query.ifBlank { null })
        when (result) {
            is ResultHandler.Success -> {
                state = state.copy(
                    articles = result.data.map { it.toArticleUi() },
                    refreshedTime = getLocalTime()
                )
            }

            is ResultHandler.Error -> {
                _channel.send(SharedArticlesEvent.ErrorEventShared(result.toUiText()))
            }
        }
        state = state.copy(
            filteredArticles = null,
            selectedFilterChip = null
        )
    }

    private suspend fun refreshArticles() {
        val query = state.searchState.query
        // For better UI - show loading indicator always at least for 1s
        val delay = viewModelScope.async(coroutineDispatchers.mainImmediate) { delay(1000) }
        val result = articlesRepository.getRefreshedArticles(
            isLoggedIn = state.isLoggedIn,
            allSelectedFilters = state.filterState.selectedFilters.map { it.toFilter() },
            query = query.ifBlank { null })
        delay.await()
        when (result) {
            is ResultHandler.Success -> {
                state = state.copy(
                    error = null,
                    articles = result.data.map { it.toArticleUi() },
                    refreshedTime = getLocalTime()
                )
            }

            is ResultHandler.Error -> {
                state = state.copy(error = result.toUiText())
                _channel.send(SharedArticlesEvent.ErrorEventShared(result.toUiText()))
            }
        }
        state = state.copy(
            filteredArticles = null,
            selectedFilterChip = null
        )
    }


    private suspend fun selectFilter(action: FilterAction.OnSelectFilter) {
        when (action.action) {
            Action.ADD -> {
                val canAddFilter = addFilterToTheList(
                    filter = action.filter,
                    filterList = allSelectedUiFilters
                )
                if (!canAddFilter) {
                    _channel.send(SharedArticlesEvent.FiveCategoriesSelected)
                }
                state =
                    state.copy(filterState = state.filterState.copy(selectedFilters = allSelectedUiFilters))
            }

            Action.REMOVE -> {
                allSelectedUiFilters.remove(action.filter)
                state = state.copy(
                    filterState = state.filterState.copy(
                        selectedFilters = allSelectedUiFilters
                    )
                )
            }
        }
    }

    private fun sortBasedOnFilter(infoChipAction: InfoChipAction.OnFilterArticles) {
        val filter = infoChipAction.filter
        val filteredNews = mutableListOf<ArticleUi>()
        when (infoChipAction.action) {
            Action.ADD -> {
                state.articles?.let { news ->
                    news.forEach { article ->
                        // This cannot be null
                        if (article.filters!!.contains(filter.filterName)) {
                            filteredNews.add(article)
                        }
                    }
                    state = state.copy(
                        filteredArticles = filteredNews.ifEmpty { null },
                        selectedFilterChip = filter
                    )
                }
            }

            Action.REMOVE -> {
                state = state.copy(
                    filteredArticles = null,
                    selectedFilterChip = null
                )
            }
        }
    }

    private fun addFilterToTheList(filter: FilterUi, filterList: MutableList<FilterUi>): Boolean {
        // Maximum 5 items per category
        return if (filterList.count { it.filterCategory == filter.filterCategory } > 4) {
            false
        } else {
            filterList.add(filter)
        }
    }

    private suspend fun getFilters() {
        val filters = viewModelScope
            .async(coroutineDispatchers.mainImmediate) { filtersStorage.getFilters() }
        val excludeCategory = viewModelScope
            .async(coroutineDispatchers.mainImmediate) { filtersStorage.getExcludeCategory() }
        val selectedFilters = filters.await()?.map { it.toFilterUi() } ?: emptyList()
        cachedFilters.addAll(selectedFilters)
        //if (allSelectedUiFilters.isEmpty()) {
        allSelectedUiFilters.addAll(selectedFilters)
        // }
        val isCategoryExcluded = excludeCategory.await()
        cachedExcludedCategory = isCategoryExcluded
        // What filters we want to show on the articles screen
        val filtersForDisplay = if (isCategoryExcluded) {
            selectedFilters.filterNot { it.filterCategory == FilterCategories.CATEGORY_FILTERS }
        } else {
            selectedFilters
        }
        state = state.copy(
            filterState = state.filterState.copy(
                excludeFilterCategory = isCategoryExcluded,
                selectedFilters = selectedFilters,
                filtersForDisplay = filtersForDisplay
            )
        )
    }

    private suspend fun getSearchSettings() {
        val suggestions =
            viewModelScope.async(coroutineDispatchers.mainImmediate) { suggestionsStorage.getSuggestions() }
        val searchExactPhrases =
            viewModelScope.async(coroutineDispatchers.mainImmediate) { suggestionsStorage.getSearchExactPhrases() }
        val searchWithFilters =
            viewModelScope.async(coroutineDispatchers.mainImmediate) { suggestionsStorage.getSearchWithFilters() }
        val searchInTitle =
            viewModelScope.async(coroutineDispatchers.mainImmediate) { suggestionsStorage.getSearchInTitle() }
        val searchWithF = searchWithFilters.await()
        state = state.copy(
            searchState = state
                .searchState.copy(
                    querySuggestions = suggestions.await(),
                    searchWithFilters = searchWithF,
                    searchWithFiltersVisible = searchWithF,
                    searchInTitle = searchInTitle.await(),
                    searchExactPhrases = searchExactPhrases.await()
                )
        )
        cachedSearch = state.searchState
    }

    private suspend fun withoutError(forSave: JustError<ErrorResult>): Boolean {
        return if (forSave is ResultHandler.Error) {
            _channel.send(SharedArticlesEvent.ErrorEventShared(forSave.toUiText()))
            false
        } else true
    }
}

private fun getLocalTime(): String {
    val currentDateTime = LocalDateTime.parse(LocalDateTime.now().toString())
    val zone = ZoneId.systemDefault()

    return currentDateTime.atZone(zone)
        .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
}
