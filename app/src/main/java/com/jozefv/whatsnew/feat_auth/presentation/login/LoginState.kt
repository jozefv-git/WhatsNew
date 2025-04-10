package com.jozefv.whatsnew.feat_auth.presentation.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val hidePassword: Boolean = true,
    val isSigningIn: Boolean = false,
    val canSignIn: Boolean = false
)
