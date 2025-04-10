package com.jozefv.whatsnew.feat_articles.presentation

import com.jozefv.whatsnew.feat_articles.presentation.filter.model.FilterUi
import com.jozefv.whatsnew.feat_articles.presentation.articles.model.ArticleUi

sealed interface ArticlesAction {
    data object OnLoadMoreArticles : ArticlesAction
    data object OnRefresh : ArticlesAction
    data class OnSaveArticle(val article: ArticleUi) : ArticlesAction
}

sealed interface InfoChipAction : ArticlesAction {
    data class OnFilterArticles(val action: Action, val filter: FilterUi) : InfoChipAction
    data object OnRemoveSearchQuery : InfoChipAction
}

sealed interface FilterAction : ArticlesAction {
    data object OnFilterOpen : FilterAction
    data object OnFilter : FilterAction
    data object OnRemoveAllFilters : FilterAction
    data object OnExcludeCategories : FilterAction
    data class OnSelectFilter(val action: Action, val filter: FilterUi) : FilterAction

}

sealed interface SearchAction : ArticlesAction {
    data object OnSearchOpen : SearchAction
    data object OnSearchWithFilters : SearchAction
    data object OnSearchInTitle : SearchAction
    data object OnSearchExactPhrases : SearchAction
    data object OnQuerySearch : SearchAction
    data object OnDeleteAllQuerySuggestions : SearchAction
    data class OnQuery(val query: String) : SearchAction
}

enum class Action {
    ADD,
    REMOVE
}
