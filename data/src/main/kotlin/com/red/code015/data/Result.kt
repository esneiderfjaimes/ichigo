package com.red.code015.data

sealed class Result<T> {
    data class Data<T>(val value: T) : Result<T>()
    data class Exception<T>(val throwable: Throwable) : Result<T>()
    data class DataWithException<T>(val value: T, val throwable: Throwable) : Result<T>()
    object NoData : Result<Nothing>()
}

class ForbiddenException(message: String) : Exception(message)
class APIException(message: String) : Exception(message)