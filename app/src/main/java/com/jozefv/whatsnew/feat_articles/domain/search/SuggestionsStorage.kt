package com.jozefv.whatsnew.feat_articles.domain.search

import com.jozefv.whatsnew.core.domain.util.ErrorResult
import com.jozefv.whatsnew.core.domain.util.JustError

interface SuggestionsStorage {
    suspend fun saveSuggestions(suggestions: List<String>): JustError<ErrorResult>
    suspend fun getSuggestions(): List<String>?
    suspend fun deleteAllSuggestions()
    suspend fun saveSearchWithFilters(value: Boolean) : JustError<ErrorResult>
    suspend fun getSearchWithFilters(): Boolean
    suspend fun saveSearchInTitle(value: Boolean) : JustError<ErrorResult>
    suspend fun getSearchInTitle(): Boolean
    suspend fun saveSearchExactPhrases(value: Boolean) : JustError<ErrorResult>
    suspend fun getSearchExactPhrases(): Boolean
}