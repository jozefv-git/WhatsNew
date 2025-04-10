package com.jozefv.whatsnew.feat_articles.presentation.articles.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcelable
import com.jozefv.whatsnew.core.domain.model.Article
import com.jozefv.whatsnew.core.domain.model.ArticleLocal
import com.jozefv.whatsnew.core.domain.util.CoroutineDispatchers
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import java.io.ByteArrayOutputStream
import java.net.URL
import java.time.format.DateTimeFormatter

@Parcelize
data class ArticleUi(
    val title: String?,
    val imageUrl: String?,
    val description: String?,
    val publishedDate: String,
    val sourceName: String?,
    val sourceUrl: String?,
    val articleLink: String,
    val filters: List<String>?
) : Parcelable

// We need this conversion for adaptive layout detail list navigation
// It needs to be parcelable to support saving and restoring the selected list item
// https://developer.android.com/develop/ui/compose/layouts/adaptive/list-detail
// !!! If we don't use parcelable - There may be a crash !!!
// Mapping will be done in VM - so we don't break our layer pattern
fun Article.toArticleUi(): ArticleUi {
    // API returns countries and categories in lowerCase, we need to convert it
    val countries =
        country?.map { country -> country.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } }
    val categories =
        category?.map { category -> category.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } }
    return ArticleUi(
        title = title,
        imageUrl = imageUrl,
        description = description,
        publishedDate = DateTimeFormatter.ofPattern("dd.MM").format(publishedDate),
        sourceUrl = sourceUrl,
        articleLink = articleLink,
        sourceName = sourceName,
        filters = if (countries != null && categories != null) {
            listOf(countries, categories).flatten()
        } else null
    )
}

suspend fun ArticleUi.toArticleLocal(coroutineDispatchers: CoroutineDispatchers): ArticleLocal {
    // This can take some time to proceed
    return withContext(coroutineDispatchers.io) {
        // if for some reason image doesn't exist
        val stream = try {
            val imageByteArray = URL(imageUrl).readBytes()
            val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
            val stream = ByteArrayOutputStream()
            // .use will close the stream when its done
            // WEBP_LOSSLESS - remove unnecessary meta-data, however, we will use JPEG
            stream.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, it)
            }
            stream.toByteArray()
        } catch (e: Exception) {
            println("Image url conversion exception: ${e.message}")
            null
        }
        ArticleLocal(
            title = title,
            image = stream,
            description = description,
            publishedDate = publishedDate,
            sourceName = sourceName,
            sourceUrl = sourceUrl,
            articleLink = articleLink,
            filters = filters
        )
    }
}