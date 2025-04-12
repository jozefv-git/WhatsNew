package com.jozefv.whatsnew.fakes

import com.jozefv.whatsnew.core.domain.SessionStorage
import com.jozefv.whatsnew.core.domain.model.AuthUser
import com.jozefv.whatsnew.core.domain.util.ErrorResult
import com.jozefv.whatsnew.core.domain.util.JustError
import com.jozefv.whatsnew.core.domain.util.ResultHandler
// Fake should behave like a real implementation - however, it is simplified
// So we don't use persistent storage
// With fakes we have "real implementation" without actual DB involvement
class SessionStorageFake: SessionStorage {
   private var authUser: AuthUser? = null
    override suspend fun getUser(): AuthUser? {
       return authUser
    }

    override suspend fun saveUser(user: AuthUser?): JustError<ErrorResult> {
        authUser = user
        return ResultHandler.Success(Unit)
    }
}