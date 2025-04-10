package com.jozefv.whatsnew.core.data

import android.content.SharedPreferences
import com.jozefv.whatsnew.core.domain.model.AuthUser
import com.jozefv.whatsnew.core.domain.util.JustError
import com.jozefv.whatsnew.core.domain.util.ResultHandler
import com.jozefv.whatsnew.core.domain.SessionStorage
import com.jozefv.whatsnew.core.domain.util.CoroutineDispatchers
import com.jozefv.whatsnew.core.domain.util.ErrorResult
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EncryptedSharedPrefSessionStorage(
    private val sharedPreferences: SharedPreferences,
    private val coroutineDispatchers: CoroutineDispatchers
) : SessionStorage {
    override suspend fun getUser(): AuthUser? {
        return withContext(coroutineDispatchers.io) {
            val jsonUser = sharedPreferences.getString(USER_KEY, null)
            jsonUser?.let {
                Json.decodeFromString<AuthUserSerialized>(it).toAuthUser()
            }
        }
    }

    override suspend fun saveUser(user: AuthUser?): JustError<ErrorResult> {
        return withContext(coroutineDispatchers.io) {
            if (user == null) {
                val loggedUser = sharedPreferences.getString(USER_KEY, null)?.let {
                    Json.decodeFromString<AuthUserSerialized>(it)
                }
                val logOutUser = loggedUser?.copy(isLoggedIn = false)
                val jsonLoggedOutUser = Json.encodeToString(logOutUser)
                val result = sharedPreferences
                    .edit()
                    .putString(USER_KEY, jsonLoggedOutUser)
                    .commit()
                return@withContext if (!result) {
                    ResultHandler.Error(ErrorResult.LocalError.OTHER)
                } else {
                    ResultHandler.Success(Unit)
                }
            }
            val jsonUser = Json.encodeToString(user.toAuthSerialized())
            val result = sharedPreferences
                .edit()
                .putString(USER_KEY, jsonUser)
                .commit()
            return@withContext if (!result) {
                ResultHandler.Error(ErrorResult.LocalError.OTHER)
            } else {
                ResultHandler.Success(Unit)
            }
        }
    }

    companion object {
        private const val USER_KEY = "USER_KEY"
    }
}