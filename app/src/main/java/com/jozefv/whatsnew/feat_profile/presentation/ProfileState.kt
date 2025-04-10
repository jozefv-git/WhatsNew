package com.jozefv.whatsnew.feat_profile.presentation

import com.jozefv.whatsnew.feat_profile.presentation.model.ArticleLocalUi


data class ProfileState(
    val nick: String = "",
    val email: String = "",
    val articles: List<ArticleLocalUi> = emptyList(),
    val filteredArticles: List<ArticleLocalUi> = emptyList(),
    val query: String = ""
)
