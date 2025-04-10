package com.jozefv.whatsnew.feat_profile.data

import android.database.sqlite.SQLiteFullException
import com.jozefv.whatsnew.core.domain.model.ArticleLocal
import com.jozefv.whatsnew.core.domain.util.JustError
import com.jozefv.whatsnew.core.domain.LocalArticlesRepository
import com.jozefv.whatsnew.core.domain.util.ResultHandler
import com.jozefv.whatsnew.core.domain.util.ErrorResult
import com.jozefv.whatsnew.feat_profile.data.database.ArticleDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalArticlesRepository(
    private val articleDao: ArticleDao
) : LocalArticlesRepository {
    override fun getArticles(): Flow<List<ArticleLocal>> {
        return articleDao.getArticles().map { list -> list.map { it.toArticleLocal() } }
    }

    override fun searchArticles(query: String): Flow<List<ArticleLocal>> {
        return articleDao.getFilteredArticles(query = query)
            .map { list -> list.map { it.toArticleLocal() } }
    }

    override suspend fun upsertArticle(article: ArticleLocal): JustError<ErrorResult> {
        return try {
            articleDao.upsertArticle(article.toArticleEntity())
            ResultHandler.Success(Unit)
        } catch (e: SQLiteFullException) {
            ResultHandler.Error(ErrorResult.LocalError.DISK_FULL)
        }
    }

    override suspend fun removeArticle(articleLink: String) {
        articleDao.deleteArticle(articleLink)
    }

    override suspend fun removeAllArticles() {
        articleDao.deleteAllArticles()
    }
}