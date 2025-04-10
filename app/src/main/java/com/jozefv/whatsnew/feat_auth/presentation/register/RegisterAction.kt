package com.jozefv.whatsnew.feat_auth.presentation.register

sealed interface RegisterAction {
    data object OnRegisterClick: RegisterAction
    data object OnEraseNick: RegisterAction
    data object OnEraseEmail: RegisterAction
    data object OnShowPassword: RegisterAction
    data class OnNickInput(val value: String): RegisterAction
    data class OnEmailInput(val value: String): RegisterAction
    data class OnPasswordInput(val value: String): RegisterAction
}