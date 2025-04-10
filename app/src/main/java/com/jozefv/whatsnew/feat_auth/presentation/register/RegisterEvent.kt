package com.jozefv.whatsnew.feat_auth.presentation.register

import com.jozefv.whatsnew.core.presentation.util.UiText

sealed interface RegisterEvent {
    data object OnRegistrationSuccess: RegisterEvent
    data class ErrorEvent(val value: UiText): RegisterEvent
}