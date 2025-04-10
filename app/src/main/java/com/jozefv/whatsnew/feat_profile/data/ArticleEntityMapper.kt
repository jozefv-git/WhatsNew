package com.jozefv.whatsnew.feat_profile.data

import com.jozefv.whatsnew.core.domain.model.ArticleLocal
import com.jozefv.whatsnew.feat_profile.data.database.ArticleEntities.ArticleEntity


fun ArticleLocal.toArticleEntity(): ArticleEntity {
    return ArticleEntity(
        title = title,
        imageByteArray = image,
        description = description,
        publishedDate = publishedDate,
        sourceUrl = sourceUrl,
        sourceName = sourceName,
        articleLink = articleLink,
        filters = filters
    )
}

fun ArticleEntity.toArticleLocal(): ArticleLocal {
    return ArticleLocal(
        title = title,
        image = imageByteArray,
        description = description,
        publishedDate = publishedDate,
        sourceName = sourceName,
        sourceUrl = sourceUrl,
        articleLink = articleLink,
        filters = filters
    )
}