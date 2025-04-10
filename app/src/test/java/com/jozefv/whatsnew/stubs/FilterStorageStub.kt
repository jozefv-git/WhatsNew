package com.jozefv.whatsnew.stubs

import com.jozefv.whatsnew.core.domain.util.ErrorResult
import com.jozefv.whatsnew.core.domain.util.JustError
import com.jozefv.whatsnew.core.domain.util.ResultHandler
import com.jozefv.whatsnew.feat_articles.domain.filter.FiltersStorage
import com.jozefv.whatsnew.feat_articles.domain.filter.model.Filter
import kotlinx.coroutines.delay

// Delays - so we can properly test our state switching - without it - state can switch immediately as nothing is blocking it within stubs
// Stub - returns "sample real-world" data to verify correct test functionality
object FilterStorageStub: FiltersStorage {
    override suspend fun saveFilters(filters: List<Filter>?): JustError<ErrorResult> {
        delay(1000)
        return ResultHandler.Success(Unit)
    }

    override suspend fun getFilters(): List<Filter>? {
        delay(1000)
        return null
    }

    override suspend fun saveExcludeCategory(value: Boolean): JustError<ErrorResult> {
        delay(1000)
        return ResultHandler.Success(Unit)
    }

    override suspend fun getExcludeCategory(): Boolean {
        delay(1000)
        return true
    }
}