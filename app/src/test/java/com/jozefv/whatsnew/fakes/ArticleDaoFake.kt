package com.jozefv.whatsnew.fakes

import com.jozefv.whatsnew.feat_profile.data.database.ArticleDao
import com.jozefv.whatsnew.feat_profile.data.database.ArticleEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
// Fake should behave like a real implementation - however, it is simplified
// So we don't use persistent storage - but only a list
// With fakes we have "real implementation" without actual DB involvement
object ArticleDaoFake : ArticleDao {
    private val articles = mutableListOf<ArticleEntities.ArticleEntity>()
    override suspend fun upsertArticle(article: ArticleEntities.ArticleEntity) {
        articles.add(article)
    }

    override fun getArticles(): Flow<List<ArticleEntities.ArticleEntity>> {
        return flow {
            emit(articles.toList())
        }
    }

    override fun getFilteredArticles(query: String): Flow<List<ArticleEntities.ArticleEntity>> {
        val filteredArticles = articles.filter {
            it.title == query ||
                    it.description == query ||
                    it.filters?.any { filter -> filter == query } ?: false
        }
        return flow {
            emit(filteredArticles)
        }
    }

    override suspend fun deleteArticle(articleLink: String) {
        val articleForRemove = articles.first { it.articleLink == articleLink }
        articles.remove(articleForRemove)
    }

    override suspend fun deleteAllArticles() {
        articles.clear()
    }
}