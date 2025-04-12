package com.jozefv.whatsnew.feat_articles.data.remote.articles

import com.jozefv.whatsnew.core.domain.util.ResultHandler
import com.jozefv.whatsnew.feat_articles.data.remote.articles.dto.ArticleDataDto
import com.jozefv.whatsnew.feat_articles.data.remote.articles.dto.toArticle
import com.jozefv.whatsnew.feat_articles.domain.articles.ArticlesRepository
import com.jozefv.whatsnew.core.domain.model.Article
import com.jozefv.whatsnew.core.domain.util.ErrorResult
import com.jozefv.whatsnew.feat_articles.data.remote.articles.ApiDefaults.DEFAULT_URL
import com.jozefv.whatsnew.feat_articles.data.remote.articles.ApiDefaults.OPTIONAL_CATEGORY_PARAM
import com.jozefv.whatsnew.feat_articles.data.remote.articles.ApiDefaults.OPTIONAL_COUNTRY_PARAM
import com.jozefv.whatsnew.feat_articles.data.remote.articles.ApiDefaults.OPTIONAL_EXCLUDE_CATEGORY_PARAM
import com.jozefv.whatsnew.feat_articles.data.remote.articles.ApiDefaults.OPTIONAL_LANGUAGE_PARAM
import com.jozefv.whatsnew.feat_articles.data.remote.articles.ApiDefaults.OPTIONAL_NEXT_PAGE_PARAM
import com.jozefv.whatsnew.feat_articles.data.remote.articles.ApiDefaults.OPTIONAL_Q_IN_TITLE_PARAM
import com.jozefv.whatsnew.feat_articles.data.remote.articles.ApiDefaults.OPTIONAL_Q_PARAM
import com.jozefv.whatsnew.feat_articles.data.remote.articles.TransformToApiCodes.categoryToApiCode
import com.jozefv.whatsnew.feat_articles.data.remote.articles.TransformToApiCodes.countryToApiCode
import com.jozefv.whatsnew.feat_articles.data.remote.articles.TransformToApiCodes.languageToApiCode
import com.jozefv.whatsnew.feat_articles.domain.filter.model.Filter
import com.jozefv.whatsnew.feat_articles.domain.filter.FilterCategories
import com.jozefv.whatsnew.feat_articles.domain.filter.FiltersStorage
import com.jozefv.whatsnew.feat_articles.domain.search.SuggestionsStorage
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import java.net.URLEncoder


class ArticlesRepositoryKtorImpl(
    private val httpClient: HttpClient,
    private val suggestionsStorage: SuggestionsStorage,
    private val filtersStorage: FiltersStorage
) : ArticlesRepository {
    private val validArticles = mutableListOf<Article>()

    // Simple pagination
    private var nextPage: String? = null


    override suspend fun getArticles(
        isLoggedIn: Boolean,
        allSelectedFilters: List<Filter>?,
        query: String?
    ): ResultHandler<List<Article>, ErrorResult> {
        val filters = if (isLoggedIn) {
            filtersStorage.getFilters() ?: emptyList()
        } else {
            allSelectedFilters ?: emptyList()
        }
        val excludeCategories = filtersStorage.getExcludeCategory()
        println("GetArticlesFilters: $filters")
        val url = if (query != null) {
            getUrlWithQuery(DEFAULT_URL, filters, query)
        } else {
            if (filters.isEmpty()) DEFAULT_URL else getUrlWithFilters(
                baseUrl = DEFAULT_URL,
                excludeCategories = excludeCategories,
                filters = filters
            )
        }
        println("Actual GetArticles url:$url")
        return when (val response =
            responseToResult<ArticleDataDto>(request = { httpClient.get(url) })) {
            is ResultHandler.Success -> {
                val validResults = response.data.results?.filter { it.description != null }
                    ?: return ResultHandler.Error(ErrorResult.NetworkError.OTHER)
                // Always have the most actual data
                removeOldDataFlags()
                // Store page for a simple pagination
                nextPage = response.data.nextPage
                validArticles.addAll(validResults.map { it.toArticle() })
                println("Actual GetArticles result: $validArticles")
                ResultHandler.Success(validArticles)
            }

            is ResultHandler.Error -> {
                println("Error: ${response.error}")
                ResultHandler.Error(response.error)

            }
        }
    }

    override suspend fun getMoreArticles(
        isLoggedIn: Boolean,
        allSelectedFilters: List<Filter>?,
        query: String?
    ): ResultHandler<List<Article>, ErrorResult> {
        val filters = if (isLoggedIn) {
            filtersStorage.getFilters() ?: emptyList()
        } else {
            allSelectedFilters ?: emptyList()
        }
        val excludeCategory = filtersStorage.getExcludeCategory()
        println("GetMoreArticlesFilters: $filters")
        val urlNextPage =
            if (nextPage != null) {
                "$DEFAULT_URL$OPTIONAL_NEXT_PAGE_PARAM$nextPage"
            } else {
                DEFAULT_URL
            }
        val url = if (query != null) {
            getUrlWithQuery(urlNextPage, filters, query)
        } else {
            getUrlWithFilters(
                baseUrl = urlNextPage,
                excludeCategories = excludeCategory,
                filters = filters
            )
        }
        println("Actual GetMoreArticles url:$url")

        return when (val response =
            responseToResult<ArticleDataDto>(request = { httpClient.get(url) })) {
            is ResultHandler.Success -> {
                val validResults = response.data.results?.filter { it.description != null }
                    ?: return ResultHandler.Error(ErrorResult.NetworkError.OTHER)
                // Store page for a simple pagination
                nextPage = response.data.nextPage
                validArticles.addAll(validResults.map { it.toArticle() })
                println("Actual GetMoreArticles result: $validArticles")
                ResultHandler.Success(validArticles)
            }

            is ResultHandler.Error -> {
                println("Error: ${response.error}")
                ResultHandler.Error(response.error)

            }
        }
    }

    override suspend fun getRefreshedArticles(
        isLoggedIn: Boolean,
        allSelectedFilters: List<Filter>?,
        query: String?
    ): ResultHandler<List<Article>, ErrorResult> {
        //val filters = sharedPrefFilterStorage.getFilters() ?: emptyList()
        val filters = if (isLoggedIn) {
            filtersStorage.getFilters() ?: emptyList()
        } else {
            allSelectedFilters ?: emptyList()
        }
        val excludeCategory = filtersStorage.getExcludeCategory()
        println("GetRefreshedArticlesFilters: $filters")

        val url = if (query != null) {
            getUrlWithQuery(DEFAULT_URL, filters, query)
        } else {
            getUrlWithFilters(
                baseUrl = DEFAULT_URL,
                excludeCategories = excludeCategory,
                filters = filters
            )
        }
        println("Actual GetRefreshedArticles url:$url")

        return when (val response =
            responseToResult<ArticleDataDto>(request = { httpClient.get(url) })) {
            is ResultHandler.Success -> {
                val validResults = response.data.results?.filter { it.description != null }
                    ?: return ResultHandler.Error(ErrorResult.NetworkError.OTHER)
                // Always have the most actual data
                removeOldDataFlags()
                nextPage = response.data.nextPage
                validArticles.addAll(validResults.map { it.toArticle() })
                println("Actual GetRefreshedArticles result: $validArticles")
                ResultHandler.Success(validArticles)
            }

            is ResultHandler.Error -> {
                println("Error: ${response.error}")
                ResultHandler.Error(response.error)
            }
        }
    }


    private fun removeOldDataFlags() {
        nextPage = null
        validArticles.clear()
    }

    private suspend inline fun <reified T> responseToResult(request: () -> HttpResponse): ResultHandler<T, ErrorResult> {
        // Catch error related to the request - before we can get response from the server
        val response = try {
            request()
        } catch (e: UnresolvedAddressException) {
            return ResultHandler.Error(ErrorResult.NetworkError.NO_INTERNET)
        } catch (e: Exception) {
            return ResultHandler.Error(ErrorResult.NetworkError.OTHER)
        }
        // Check errors returned from the server - after request was successful
        return when (response.status.value) {
            in 200..299 -> {
                try {
                    ResultHandler.Success(response.body<T>())
                } catch (e: NoTransformationFoundException) {
                    // Unable to parse with kotlinx serialization - to specified data class
                    ResultHandler.Error(ErrorResult.NetworkError.SERIALIZATION)
                }
            }

            401 -> ResultHandler.Error(ErrorResult.NetworkError.UNAUTHORIZED)
            404 -> ResultHandler.Error(ErrorResult.NetworkError.NO_INTERNET)
            429 -> ResultHandler.Error(ErrorResult.NetworkError.TOO_MANY_REQUESTS)
            500 -> ResultHandler.Error(ErrorResult.NetworkError.INTERNAL_ERROR)
            else -> ResultHandler.Error(ErrorResult.NetworkError.OTHER)
        }
    }


    private suspend fun getUrlWithQuery(
        baseUrl: String,
        filters: List<Filter>,
        query: String
    ): String {
        val searchWithFilters = suggestionsStorage.getSearchWithFilters()
        val searchInTitle = suggestionsStorage.getSearchInTitle()
        val searchScopeUrl =
            if (searchInTitle) OPTIONAL_Q_IN_TITLE_PARAM else OPTIONAL_Q_PARAM
        val searchExactPhrases = suggestionsStorage.getSearchExactPhrases()
        val excludeCategory = filtersStorage.getExcludeCategory()
        val searchQuery =
            if (searchExactPhrases) "\"${encodedQuery(query)}\"" else encodedQuery(query)
        return if (filters.isEmpty() || !searchWithFilters) {
            "$baseUrl$searchScopeUrl$searchQuery"
        } else {
            "${
                getUrlWithFilters(
                    baseUrl = baseUrl,
                    excludeCategories = excludeCategory,
                    filters = filters
                )
            }$searchScopeUrl$searchQuery"
        }
    }

    private fun getUrlWithFilters(
        baseUrl: String,
        excludeCategories: Boolean,
        filters: List<Filter>
    ): String {
        val countries = filters.filter { it.filterCategory == FilterCategories.COUNTRY_FILTERS }
        val category = filters.filter { it.filterCategory == FilterCategories.CATEGORY_FILTERS }
        val language = filters.filter { it.filterCategory == FilterCategories.LANGUAGE_FILTERS }
        val countryCodes =
            filtersAsCodeString(countries, transformToApiCode = { countryToApiCode(it) })
        val categoryCodes =
            filtersAsCodeString(category, transformToApiCode = { categoryToApiCode(it) })
        val languageCodes =
            filtersAsCodeString(language, transformToApiCode = { languageToApiCode(it) })

        val validCategoryParam =
            if (excludeCategories) OPTIONAL_EXCLUDE_CATEGORY_PARAM else OPTIONAL_CATEGORY_PARAM

        val countryUrl = countryCodes?.let { "$OPTIONAL_COUNTRY_PARAM$it" }
        val categoryUrl = categoryCodes?.let { "$validCategoryParam$it" }
        val languageUrl = languageCodes?.let { "$OPTIONAL_LANGUAGE_PARAM$it" }
        var completeUrl = baseUrl
        listOf(countryUrl, categoryUrl, languageUrl).forEach { url ->
            if (url != null) {
                completeUrl += "$url"
            }
        }
        return completeUrl
    }

    private fun filtersAsCodeString(
        filters: List<Filter>,
        transformToApiCode: (filter: String) -> String
    ): String? {
        if (filters.isEmpty()) return null
        return filters.joinToString { transformToApiCode(it.filterName) }.replace(" ", "")
    }

    private fun encodedQuery(query: String): String {
        return URLEncoder.encode(query, "UTF-8")
    }
}
