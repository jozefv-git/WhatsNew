package com.jozefv.whatsnew.app

data class MainState(
    val isAuthenticating: Boolean = false,
    val isFirstSession: Boolean = false,
    val isLoggedIn: Boolean = false
)
