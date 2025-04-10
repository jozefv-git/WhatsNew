package com.jozefv.whatsnew.core.domain

import com.jozefv.whatsnew.core.domain.model.AuthUser
import com.jozefv.whatsnew.core.domain.util.ErrorResult
import com.jozefv.whatsnew.core.domain.util.JustError

interface SessionStorage {
    suspend fun getUser(): AuthUser?
    suspend fun saveUser(user: AuthUser?): JustError<ErrorResult>
}