package com.jozefv.whatsnew.core.data

import com.jozefv.whatsnew.core.domain.model.AuthUser
import kotlinx.serialization.Serializable

fun AuthUser.toAuthSerialized(): AuthUserSerialized {
    return AuthUserSerialized(
        nick = nick,
        email = email,
        password = password,
        isLoggedIn = isLoggedIn
    )
}

fun AuthUserSerialized.toAuthUser(): AuthUser {
    return AuthUser(
        nick = nick,
        email = email,
        password = password,
        isLoggedIn = isLoggedIn
    )
}


@Serializable
data class AuthUserSerialized(
    val nick: String,
    val email: String,
    val password: String,
    val isLoggedIn: Boolean
)