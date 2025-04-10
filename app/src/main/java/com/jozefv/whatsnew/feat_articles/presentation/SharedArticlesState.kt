package com.jozefv.whatsnew.feat_articles.presentation

import com.jozefv.whatsnew.core.presentation.util.UiText
import com.jozefv.whatsnew.feat_articles.presentation.filter.model.FilterUi
import com.jozefv.whatsnew.feat_articles.presentation.articles.model.ArticleUi



data class ArticlesState(
    val isLoggedIn: Boolean = false,
    val isLoadingArticles: Boolean = false,
    val isLoadingMoreArticles: Boolean = false,
    val isLoadingArticleSave: Boolean = false,
    val refreshedTime: String = "",
    val error: UiText? = null,
    val articles: List<ArticleUi>? = null,
    val selectedFilterChip: FilterUi? = null,
    val filteredArticles: List<ArticleUi>? = null,
    val filterState: FilterState = FilterState(),
    val searchState: SearchState = SearchState()
)

data class FilterState(
    val selectedFilters: List<FilterUi> = emptyList(),
    val filtersForDisplay: List<FilterUi> = emptyList(),
    val excludeFilterCategory: Boolean = false,
)

data class SearchState(
    val isLoadingSearch: Boolean = false,
    val querySuggestions: List<String>? = null,
    val query: String = "",
    val searchWithFilters: Boolean = false,
    val searchWithFiltersVisible: Boolean = false,
    val searchInTitle: Boolean = false,
    val searchExactPhrases: Boolean = false
)