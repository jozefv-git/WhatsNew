package com.jozefv.whatsnew.core.domain.util

sealed interface ResultHandler<out D,out E> {
    data class Success<out D>(val data: D): ResultHandler<D, Nothing>
    data class Error<out E>(val error: ErrorResult): ResultHandler<Nothing, E>
}

typealias JustError<E> = ResultHandler<Unit, E>