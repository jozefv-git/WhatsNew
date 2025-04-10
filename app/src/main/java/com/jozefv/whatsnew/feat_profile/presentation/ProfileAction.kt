package com.jozefv.whatsnew.feat_profile.presentation

import com.jozefv.whatsnew.feat_profile.presentation.model.ArticleLocalUi


sealed interface ProfileAction {
    data object OnLogout : ProfileAction
    data object OnDeleteAllArticles: ProfileAction
    data object OnClearQuery: ProfileAction
    // Article link is unique, so we can use it as identifier
    data class OnRemoveArticle(val articleLocalUi: ArticleLocalUi): ProfileAction
    data class OnQueryChange(val query: String): ProfileAction
}