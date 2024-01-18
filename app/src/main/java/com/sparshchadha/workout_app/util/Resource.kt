package com.sparshchadha.workout_app.util

sealed class Resource<T>(val data: T? = null, error: Throwable? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(data: T? = null, error: Throwable) : Resource<T>(data, error)
}