package com.jozefv.whatsnew.core.domain

import com.jozefv.whatsnew.core.domain.model.ArticleLocal
import com.jozefv.whatsnew.core.domain.util.ErrorResult
import com.jozefv.whatsnew.core.domain.util.JustError
import kotlinx.coroutines.flow.Flow

interface LocalArticlesRepository {
    fun getArticles(): Flow<List<ArticleLocal>>
    fun searchArticles(query: String): Flow<List<ArticleLocal>>
    suspend fun upsertArticle(article: ArticleLocal): JustError<ErrorResult>
    suspend fun removeArticle(articleLink: String)
    suspend fun removeAllArticles()
}