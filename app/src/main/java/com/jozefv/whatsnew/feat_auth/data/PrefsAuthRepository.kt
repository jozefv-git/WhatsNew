package com.jozefv.whatsnew.feat_auth.data

import com.jozefv.whatsnew.feat_auth.domain.AuthRepository
import com.jozefv.whatsnew.core.domain.model.AuthUser
import com.jozefv.whatsnew.core.domain.util.ResultHandler
import com.jozefv.whatsnew.core.domain.SessionStorage
import com.jozefv.whatsnew.core.domain.util.ErrorResult
import com.jozefv.whatsnew.core.domain.util.JustError

class PrefsAuthRepository(
    private val prefs: SessionStorage,
) : AuthRepository {
    override suspend fun login(email: String, password: String): JustError<ErrorResult> {
        val user = prefs.getUser()
        return if (user == null) {
            ResultHandler.Error(ErrorResult.LocalError.USER_DOES_NOT_EXISTS)
        } else {
            if (user.email == email && user.password == password) {
                // Login user
                val loggedUser = user.copy(isLoggedIn = true)
                prefs.saveUser(loggedUser)
            } else {
                ResultHandler.Error(ErrorResult.LocalError.USER_DOES_NOT_EXISTS)
            }
        }
    }

    override suspend fun register(
        nick: String,
        email: String,
        password: String
    ): JustError<ErrorResult> {
        return prefs.saveUser(
            AuthUser(
                nick = nick,
                email = email,
                password = password,
                isLoggedIn = true
            )
        )
    }
}