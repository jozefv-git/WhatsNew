package com.jozefv.whatsnew.feat_articles.data.remote.articles.dto

import com.jozefv.whatsnew.core.domain.model.Article
import com.jozefv.whatsnew.feat_articles.domain.articles.toZonedDateTime

fun ArticleDto.toArticle(): Article {
    return Article(
        title = title,
        imageUrl = image_url,
        description = description,
        publishedDate = pubDate.toZonedDateTime(),
        // Link cannot be null as long as article exists
        articleLink = link,
        sourceUrl = source_url,
        sourceName = source_name,
        category = category,
        country = country,
        language = language
    )
}