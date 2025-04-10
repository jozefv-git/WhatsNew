package com.jozefv.whatsnew.feat_articles.data.remote.articles.dto

import kotlinx.serialization.Serializable

// DTO for communication with https://newsdata.io/documentation/#about-newdata-api
// Data class representation of the remote JSON object from the API
// Serializable is necessary in order to parse from JSON to Kotlin data class
// Value names must be called same as specified by the API, otherwise another annotations would be needed
// Non used fields from the API will be ignored thanks for "ignoreUnknownKeys = true" in the HttpClientFactory
// Serialize in the data class as we want to have domain layer independent
@Serializable
data class ArticleDataDto(
    val results: List<ArticleDto>? = null,
    val status: String,
    val totalResults: Int,
    val nextPage: String?
)