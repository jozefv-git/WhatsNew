package com.jozefv.whatsnew.feat_auth.presentation.login

import com.jozefv.whatsnew.core.presentation.util.UiText

sealed interface LoginEvent {
    data object LoginSuccess: LoginEvent
    data class ErrorEvent(val value: UiText): LoginEvent
}