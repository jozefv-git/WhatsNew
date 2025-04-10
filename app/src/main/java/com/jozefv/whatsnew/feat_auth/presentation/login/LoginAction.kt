package com.jozefv.whatsnew.feat_auth.presentation.login


sealed interface LoginAction {
    data class OnEmailInput(val email: String): LoginAction
    data class OnPasswordInput(val password: String): LoginAction
    data object OnEraseEmail: LoginAction
    data object OnShowPassword : LoginAction
    data object OnSignClick : LoginAction
}