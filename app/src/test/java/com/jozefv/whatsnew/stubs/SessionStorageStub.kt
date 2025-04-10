package com.jozefv.whatsnew.stubs

import com.jozefv.whatsnew.core.domain.SessionStorage
import com.jozefv.whatsnew.core.domain.model.AuthUser
import com.jozefv.whatsnew.core.domain.util.ErrorResult
import com.jozefv.whatsnew.core.domain.util.JustError
import com.jozefv.whatsnew.core.domain.util.ResultHandler
import kotlinx.coroutines.delay

// Delays - so we can properly test our state switching - without it - state can switch immediately as nothing is blocking it within stubs
// Stub - returns "sample real-world" data to verify correct test functionality
object SessionStorageStub : SessionStorage {
    override suspend fun getUser(): AuthUser? {
        delay(1000)
        return authUserStub
    }

    override suspend fun saveUser(user: AuthUser?): JustError<ErrorResult> {
        delay(1000)
        return ResultHandler.Success(Unit)
    }

    val authUserStub = AuthUser(
        nick = "Jozef",
        email = "email@example.com",
        password = "password123",
        isLoggedIn = true
    )
}