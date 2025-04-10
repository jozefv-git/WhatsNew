package com.jozefv.whatsnew.core.domain.model

 class ArticleLocal(
    val title: String?,
    val image: ByteArray?,
    val description: String?,
    val publishedDate: String,
    val sourceName: String?,
    val sourceUrl: String?,
    val articleLink: String,
    val filters: List<String>? = null,
)
