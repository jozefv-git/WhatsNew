package com.jozefv.whatsnew.feat_articles.domain.articles

import com.jozefv.whatsnew.core.domain.model.Article
import com.jozefv.whatsnew.core.domain.util.ResultHandler
import com.jozefv.whatsnew.core.domain.util.ErrorResult
import com.jozefv.whatsnew.feat_articles.domain.filter.model.Filter

interface ArticlesRepository {
    suspend fun getArticles(
        isLoggedIn: Boolean,
        allSelectedFilters: List<Filter>?,
        query: String? = null
    ): ResultHandler<List<Article>, ErrorResult>

    suspend fun getMoreArticles(
        isLoggedIn: Boolean,
        allSelectedFilters: List<Filter>?,
        query: String? = null
    ): ResultHandler<List<Article>, ErrorResult>

    suspend fun getRefreshedArticles(
        isLoggedIn: Boolean,
        allSelectedFilters: List<Filter>?,
        query: String? = null
    ): ResultHandler<List<Article>, ErrorResult>
}