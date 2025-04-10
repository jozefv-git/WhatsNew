package com.jozefv.whatsnew.feat_profile.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jozefv.whatsnew.core.domain.LocalArticlesRepository
import com.jozefv.whatsnew.core.domain.SessionStorage
import com.jozefv.whatsnew.core.domain.util.CoroutineDispatchers
import com.jozefv.whatsnew.feat_profile.presentation.model.toArticleLocalUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class ProfileViewModel(
    private val sessionStorage: SessionStorage,
    private val localArticlesRepository: LocalArticlesRepository,
    private val coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {
    var state by mutableStateOf(ProfileState())
        private set

    private val queryState = MutableStateFlow("")

    init {
        viewModelScope.launch(coroutineDispatchers.mainImmediate) {
            // Null shouldn't happen as profile is available only for logged users
            val user = sessionStorage.getUser()
            state = state.copy(nick = user?.nick ?: "", email = user?.email ?: "")
        }
        // Fetch our saved news, flow triggers every time we add new article to the db
        localArticlesRepository.getArticles().onEach { article ->
            state = state.copy(articles = article.map { it.toArticleLocalUi() })
        }
            .launchIn(viewModelScope.plus(coroutineDispatchers.mainImmediate)) // Trigger getArticles() flow and constantly listen to its change
        // React to change of query with delay of 300ms - so if user types fast, don't execute our ROOM
        // With every query change
        queryState.debounce(300).onEach { query ->
            if (query.isNotBlank()) {
                localArticlesRepository.searchArticles(tokenPrefixQuery(query)).onEach { articles ->
                    state = state.copy(filteredArticles = articles.map { it.toArticleLocalUi() })
                }.launchIn(viewModelScope.plus(coroutineDispatchers.mainImmediate))
            }
        }.launchIn(viewModelScope.plus(coroutineDispatchers.mainImmediate))
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.OnClearQuery -> {
                state = state.copy(query = "")
            }

            is ProfileAction.OnQueryChange -> {
                val query = action.query
                queryState.value = query
                state = state.copy(query = query)
            }

            is ProfileAction.OnRemoveArticle -> {
                viewModelScope.launch(coroutineDispatchers.mainImmediate) {
                    localArticlesRepository.removeArticle(action.articleLocalUi.articleLink)
                }
            }

            ProfileAction.OnDeleteAllArticles -> {
                viewModelScope.launch(coroutineDispatchers.mainImmediate) {
                    localArticlesRepository.removeAllArticles()
                }
            }

            ProfileAction.OnLogout -> {
                viewModelScope.launch(coroutineDispatchers.mainImmediate) {
                    sessionStorage.saveUser(null)
                }
            }
        }
    }

    private fun tokenPrefixQuery(query: String, ftsSearch: Boolean = true): String {
        return if (ftsSearch) "*$query*" else "%$query%"
    }
}