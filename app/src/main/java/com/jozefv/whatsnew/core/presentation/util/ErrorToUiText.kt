package com.jozefv.whatsnew.core.presentation.util

import com.jozefv.whatsnew.R
import com.jozefv.whatsnew.core.domain.util.ResultHandler
import com.jozefv.whatsnew.core.domain.util.ErrorResult

fun ResultHandler.Error<ErrorResult>.toUiText(): UiText {
    return when (error) {
        ErrorResult.LocalError.USER_DOES_NOT_EXISTS -> UiText.StringResource(R.string.not_existing_user)
        ErrorResult.LocalError.DISK_FULL -> UiText.StringResource(R.string.disk_full)
        ErrorResult.LocalError.OTHER -> UiText.StringResource(R.string.local_other)
        ErrorResult.NetworkError.UNAUTHORIZED -> UiText.StringResource(R.string.unauthorised)
        ErrorResult.NetworkError.TOO_MANY_REQUESTS -> UiText.StringResource(R.string.too_many_requests)
        ErrorResult.NetworkError.INTERNAL_ERROR -> UiText.StringResource(R.string.internal_error)
        ErrorResult.NetworkError.NO_INTERNET -> UiText.StringResource(R.string.no_internet)
        // Don't show serialization error - user has nothing to do with it, instead, show only Other error...
        else -> UiText.StringResource(R.string.other_error)
    }
}