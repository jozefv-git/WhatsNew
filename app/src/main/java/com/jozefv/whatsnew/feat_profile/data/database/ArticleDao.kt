package com.jozefv.whatsnew.feat_profile.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

// DAO - data access object for our table
// All functions that we want to have with our ROOM DB
@Dao
interface ArticleDao {
    // Insert a new article, if article with the same id exists - update it
    @Upsert
    suspend fun upsertArticle(article: ArticleEntities.ArticleEntity)

    // Most recent articles will be on the top - listen to the db reactively by collecting the flow
    @Query("SELECT * FROM ARTICLES ORDER BY publishedDate DESC")
    fun getArticles(): Flow<List<ArticleEntities.ArticleEntity>>

    // Doesn't need to be suspending as flow doesn't return anything until is collected - which must happen in coroutine
    // FTS4 (full-text) searching - instead of "LIKE" keyword - and prefixes %query% - we need to use MATCH to enable fts and
    // query as *query*
    @Query(
        """SELECT * FROM articles 
        JOIN articles_fts ON articles_fts.title == articles.title 
        AND articles_fts.description == articles.description AND articles_fts.filters == articles.filters
        WHERE articles_fts.title MATCH :query OR articles_fts.description MATCH :query 
        OR articles_fts.filters MATCH :query """
    )
    fun getFilteredArticles(query: String): Flow<List<ArticleEntities.ArticleEntity>>

    // Article link must be unique and we can use it as article identifier
    @Query("DELETE FROM ARTICLES WHERE articleLink=:articleLink")
    suspend fun deleteArticle(articleLink: String)

    @Query("DELETE FROM ARTICLES")
    suspend fun deleteAllArticles()


}