package com.jozefv.whatsnew.feat_articles.presentation.articles.model

import com.jozefv.whatsnew.core.domain.model.Article
import java.time.ZonedDateTime

val previewArticle = Article(
    title = "Best game starting",
    imageUrl = null,
    description = "Welcome to the best hockey game ever!",
    publishedDate = ZonedDateTime.now(),
    sourceName = "ProHockeyleague",
    sourceUrl = "www.prohockeyleague.find",
    articleLink = "www.prohockeyleague.find/article/best-game-ever",
    category = listOf("science","tourism","sport"),
    country = listOf("cz","sk","fi","us"),
    language = "en"
).toArticleUi()