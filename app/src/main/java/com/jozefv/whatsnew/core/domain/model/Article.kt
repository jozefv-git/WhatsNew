package com.jozefv.whatsnew.core.domain.model

import java.time.ZonedDateTime

data class Article(
    val title: String?,
    val imageUrl: String?,
    val description: String?,
    val publishedDate: ZonedDateTime,
    val sourceName: String?,
    val sourceUrl: String?,
    val articleLink: String,
    val category: List<String>? = null,
    val country: List<String>? = null,
    val language: String? = null,
    val filters: List<String>? = null
)
