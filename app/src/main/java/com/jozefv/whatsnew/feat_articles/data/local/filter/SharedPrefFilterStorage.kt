package com.jozefv.whatsnew.feat_articles.data.local.filter

import android.content.SharedPreferences
import com.jozefv.whatsnew.core.domain.util.CoroutineDispatchers
import com.jozefv.whatsnew.core.domain.util.ResultHandler
import com.jozefv.whatsnew.core.domain.util.ErrorResult
import com.jozefv.whatsnew.core.domain.util.JustError
import com.jozefv.whatsnew.feat_articles.domain.filter.model.Filter
import com.jozefv.whatsnew.feat_articles.domain.filter.FiltersStorage
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SharedPrefFilterStorage(
    private val sharedPreferences: SharedPreferences,
    private val coroutineDispatchers: CoroutineDispatchers
) : FiltersStorage {
    override suspend fun saveFilters(filters: List<Filter>?): JustError<ErrorResult> {
        return withContext(coroutineDispatchers.io) {
            // When null, delete our filters
            if (filters == null) {
                val result = sharedPreferences
                    .edit()
                    .remove(FILTERS_KEY)
                    .commit() // Blocking call
                return@withContext if (!result) {
                    ResultHandler.Error(ErrorResult.LocalError.OTHER)
                } else {
                    ResultHandler.Success(Unit)
                }
            }
            val json = Json.encodeToString(filters.map { it.toFilterSerializable() })
            val result = sharedPreferences
                .edit()
                .putString(FILTERS_KEY, json)
                .commit()
            return@withContext if (!result) {
                ResultHandler.Error(ErrorResult.LocalError.OTHER)
            } else {
                ResultHandler.Success(Unit)
            }
        }
    }

    override suspend fun getFilters(): List<Filter>? {
        return withContext(coroutineDispatchers.io) {
            val json = sharedPreferences.getString(FILTERS_KEY, null)
            json?.let {
                Json.decodeFromString<List<FilterSerializable>>(it)
                    .map { filter -> filter.toFilter() }
            }
        }
    }

    override suspend fun saveExcludeCategory(value: Boolean): JustError<ErrorResult> {
        return withContext(coroutineDispatchers.io) {
            val result = sharedPreferences
                .edit()
                .putBoolean(EXCLUDED_CATEGORY_KEY, value)
                .commit()
            return@withContext if (!result) {
                ResultHandler.Error(ErrorResult.LocalError.OTHER)
            } else {
                ResultHandler.Success(Unit)
            }
        }
    }

    override suspend fun getExcludeCategory(): Boolean {
        return withContext(coroutineDispatchers.io) {
            sharedPreferences.getBoolean(EXCLUDED_CATEGORY_KEY, false)
        }
    }

    companion object {
        private const val FILTERS_KEY = "FILTERS_KEY"
        private const val EXCLUDED_CATEGORY_KEY = "EXCLUDED_CATEGORY_KEY"
    }
}