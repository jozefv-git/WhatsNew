package com.jozefv.whatsnew.feat_articles.data.remote.articles.dto

import kotlinx.serialization.Serializable

/**
 * Result doesn't include all fields. Mostly because of API availability paid limitation.
 *
 * All fields can be found here: https://newsdata.io/documentation/#http_response
 *
 */

@Serializable
data class ArticleDto(
    val article_id: String,
    val category: List<String>,
    val country: List<String>,
    val creator: List<String>?,
    val description: String?,
    val image_url: String?,
    val keywords: List<String>?,
    val language: String,
    val link: String,
    val pubDate: String,
    val pubDateTZ: String?,
    val source_icon: String?,
    val source_name: String?,
    val source_url: String?,
    val title: String?,
    val video_url: String?
)