package com.jozefv.whatsnew.feat_profile.presentation.model

import com.jozefv.whatsnew.core.domain.model.ArticleLocal

val previewArticleLocal = ArticleLocal(
    title = "Best game starting",
    image = null,
    description = "Welcome to the best hockey game ever!",
    publishedDate = "25.4",
    sourceName = "ProHockeyleague",
    sourceUrl = "www.prohockeyleague.find",
    articleLink = "www.prohockeyleague.find/article/best-game-ever",
    filters = listOf("Dogs","Cats","Sauna","Aurora","Other","More","Top")
).toArticleLocalUi()