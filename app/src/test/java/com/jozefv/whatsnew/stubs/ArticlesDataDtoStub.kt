package com.jozefv.whatsnew.stubs

import com.jozefv.whatsnew.feat_articles.data.remote.articles.dto.ArticleDataDto
import com.jozefv.whatsnew.feat_articles.data.remote.articles.dto.ArticleDto

// Stub - returns "sample real-world" data to verify correct test functionality
object ArticlesDataDtoStub {
    fun articleDataDaoStub(sizeOfResults: Int, nextPage: String): ArticleDataDto {
        return ArticleDataDto(
            results = List(sizeOfResults) { articleDtoStub },
            status = "Success",
            totalResults = sizeOfResults,
            nextPage = nextPage
        )
    }

    val articleDtoStub = ArticleDto(
        article_id = "article-id",
        category = listOf("top"),
        country = listOf("slovakia"),
        creator = listOf("secret-creator"),
        description = "Article-description",
        image_url = "image-url",
        keywords = listOf("top", "cars"),
        language = "sk",
        link = "article-link",
        pubDate = "2025-03-09 06:00:40",
        pubDateTZ = null,
        source_icon = null,
        source_name = "source-name",
        source_url = "source-url",
        title = "Super cars",
        video_url = null
    )

    const val SIZE_OF_RESULTS = 5
    const val NEXT_PAGE = "2"
}

