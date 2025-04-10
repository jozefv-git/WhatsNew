package com.jozefv.whatsnew.stubs

import com.jozefv.whatsnew.core.domain.util.ErrorResult
import com.jozefv.whatsnew.core.domain.util.JustError
import com.jozefv.whatsnew.core.domain.util.ResultHandler
import com.jozefv.whatsnew.feat_articles.domain.search.SuggestionsStorage
import kotlinx.coroutines.delay
// Delays - so we can properly test our state switching - without it - state can switch immediately as nothing is blocking it within stubs
// Stub - returns "sample real-world" data to verify correct test functionality
object SuggestionStorageStub: SuggestionsStorage {
    override suspend fun saveSuggestions(suggestions: List<String>): JustError<ErrorResult> {
        delay(1000)
        return ResultHandler.Success(Unit)
    }

    override suspend fun getSuggestions(): List<String>? {
        delay(1000)
        return null
    }

    override suspend fun deleteAllSuggestions() {
        delay(1000)
    }

    override suspend fun saveSearchWithFilters(value: Boolean): JustError<ErrorResult> {
        delay(1000)
        return ResultHandler.Success(Unit)
    }

    override suspend fun getSearchWithFilters(): Boolean {
        delay(1000)
        return true
    }

    override suspend fun saveSearchInTitle(value: Boolean): JustError<ErrorResult> {
        delay(1000)
        return ResultHandler.Success(Unit)
    }

    override suspend fun getSearchInTitle(): Boolean {
        delay(1000)
        return true
    }

    override suspend fun saveSearchExactPhrases(value: Boolean): JustError<ErrorResult> {
        delay(1000)
        return ResultHandler.Success(Unit)
    }

    override suspend fun getSearchExactPhrases(): Boolean {
        delay(1000)
        return true
    }
}