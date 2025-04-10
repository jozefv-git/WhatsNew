package com.jozefv.whatsnew.core.domain.util

sealed interface ErrorResult {
    enum class NetworkError : ErrorResult {
        UNAUTHORIZED,
        TOO_MANY_REQUESTS,
        INTERNAL_ERROR,
        NO_INTERNET,
        SERIALIZATION,
        OTHER
    }

    enum class LocalError : ErrorResult {
        DISK_FULL,
        USER_DOES_NOT_EXISTS,
        OTHER
    }
}