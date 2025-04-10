package com.jozefv.whatsnew.feat_auth.presentation.register

import com.jozefv.whatsnew.core.presentation.util.UiText

data class RegisterState(
    val nick: String = "",
    val nickError: UiText? = null,
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val hidePassword: Boolean = true,
    val canRegister: Boolean = false
)
