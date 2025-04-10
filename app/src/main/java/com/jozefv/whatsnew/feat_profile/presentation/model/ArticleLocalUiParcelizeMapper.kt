package com.jozefv.whatsnew.feat_profile.presentation.model

import android.os.Parcelable
import com.jozefv.whatsnew.core.domain.model.ArticleLocal
import kotlinx.parcelize.Parcelize

@Parcelize
class ArticleLocalUi(
    val title: String?,
    val image: ByteArray?,
    val description: String?,
    val publishedDate: String,
    val sourceName: String?,
    val sourceUrl: String?,
    val articleLink: String,
    val filters: List<String>?
) : Parcelable

fun ArticleLocal.toArticleLocalUi(): ArticleLocalUi {
    return ArticleLocalUi(
        title = title,
        image = image,
        description = description,
        publishedDate = publishedDate,
        sourceName = sourceName,
        sourceUrl = sourceUrl,
        articleLink = articleLink,
        filters = filters
    )
}
