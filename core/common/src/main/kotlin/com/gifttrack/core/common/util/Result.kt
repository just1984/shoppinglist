package com.gifttrack.core.common.util

/**
 * A generic wrapper for handling success and error states.
 *
 * This is commonly used for network operations or any operation
 * that can fail.
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

/**
 * Returns the success data or null if this is not a success result.
 */
fun <T> Result<T>.getOrNull(): T? = when (this) {
    is Result.Success -> data
    else -> null
}

/**
 * Returns true if this is a success result.
 */
fun <T> Result<T>.isSuccess(): Boolean = this is Result.Success

/**
 * Returns true if this is an error result.
 */
fun <T> Result<T>.isError(): Boolean = this is Result.Error
