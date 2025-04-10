package com.jozefv.whatsnew.feat_articles.data.local.search

import android.content.SharedPreferences
import com.jozefv.whatsnew.core.domain.util.CoroutineDispatchers
import com.jozefv.whatsnew.core.domain.util.ResultHandler
import com.jozefv.whatsnew.core.domain.util.ErrorResult
import com.jozefv.whatsnew.core.domain.util.JustError
import com.jozefv.whatsnew.feat_articles.domain.search.SuggestionsStorage
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SharedPrefSuggestionsStorage(
    private val prefs: SharedPreferences,
    private val coroutineDispatchers: CoroutineDispatchers
) : SuggestionsStorage {
    override suspend fun saveSuggestions(suggestions: List<String>): JustError<ErrorResult> {
        return withContext(coroutineDispatchers.io) {
            val json = Json.encodeToString(suggestions)
            val result = prefs.edit()
                .putString(SUGGESTIONS_KEY, json)
                .commit()
            if (!result) {
                ResultHandler.Error(ErrorResult.LocalError.OTHER)
            } else {
                ResultHandler.Success(Unit)
            }
        }
    }

    override suspend fun getSuggestions(): List<String>? {
        return withContext(coroutineDispatchers.io) {
            val json = prefs.getString(SUGGESTIONS_KEY, null)
            json?.let {
                Json.decodeFromString<List<String>>(it)
            }
        }
    }

    override suspend fun deleteAllSuggestions() {
        withContext(coroutineDispatchers.io) {
            prefs.edit()
                .remove(SUGGESTIONS_KEY)
                .commit()
        }
    }

    override suspend fun saveSearchWithFilters(value: Boolean): JustError<ErrorResult> {
        return withContext(coroutineDispatchers.io) {
            val result = prefs.edit()
                .putBoolean(SEARCH_WITH_FILTERS, value)
                .commit()
            if (!result) {
                ResultHandler.Error(ErrorResult.LocalError.OTHER)
            } else {
                ResultHandler.Success(Unit)
            }
        }
    }

    override suspend fun getSearchWithFilters(): Boolean {
        return withContext(coroutineDispatchers.io) {
            prefs.getBoolean(SEARCH_WITH_FILTERS, false)
        }
    }

    override suspend fun saveSearchInTitle(value: Boolean): JustError<ErrorResult> {
        return withContext(coroutineDispatchers.io) {
            val result = prefs.edit()
                .putBoolean(SEARCH_IN_TITLE, value)
                .commit()
            if (!result) {
                ResultHandler.Error(ErrorResult.LocalError.OTHER)
            } else {
                ResultHandler.Success(Unit)
            }
        }
    }

    override suspend fun getSearchInTitle(): Boolean {
        return withContext(coroutineDispatchers.io) {
            prefs.getBoolean(SEARCH_IN_TITLE, false)
        }
    }

    override suspend fun saveSearchExactPhrases(value: Boolean): JustError<ErrorResult> {
        return withContext(coroutineDispatchers.io) {
            val result = prefs.edit()
                .putBoolean(SEARCH_EXACT_PHRASES, value)
                .commit()
            if (!result) {
                ResultHandler.Error(ErrorResult.LocalError.OTHER)
            } else {
                ResultHandler.Success(Unit)
            }
        }
    }

    override suspend fun getSearchExactPhrases(): Boolean {
        return withContext(coroutineDispatchers.io) {
            prefs.getBoolean(SEARCH_EXACT_PHRASES, false)
        }
    }

    companion object {
        private const val SUGGESTIONS_KEY = "SUGGESTIONS_KEY"
        private const val SEARCH_WITH_FILTERS = "SEARCH_WITH_FILTERS"
        private const val SEARCH_IN_TITLE = "SEARCH_IN_TITLE"
        private const val SEARCH_EXACT_PHRASES = "SEARCH_EXACT_PHRASES"
    }
}