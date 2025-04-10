package com.jozefv.whatsnew.feat_articles.domain.filter

import com.jozefv.whatsnew.core.domain.util.ErrorResult
import com.jozefv.whatsnew.core.domain.util.JustError
import com.jozefv.whatsnew.feat_articles.domain.filter.model.Filter

interface FiltersStorage {
    suspend fun saveFilters(filters: List<Filter>?): JustError<ErrorResult>
    suspend fun getFilters(): List<Filter>?
    suspend fun saveExcludeCategory(value: Boolean): JustError<ErrorResult>
    suspend fun getExcludeCategory(): Boolean
}