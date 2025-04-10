package com.jozefv.whatsnew.core.domain.model

data class AuthUser(
    val nick: String,
    val email: String,
    val password: String,
    val isLoggedIn: Boolean
)
