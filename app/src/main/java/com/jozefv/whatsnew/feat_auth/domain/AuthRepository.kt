package com.jozefv.whatsnew.feat_auth.domain

import com.jozefv.whatsnew.core.domain.util.ErrorResult
import com.jozefv.whatsnew.core.domain.util.JustError

interface AuthRepository {
    suspend fun login(email: String, password: String): JustError<ErrorResult>
    suspend fun register(nick: String, email: String, password: String): JustError<ErrorResult>
}