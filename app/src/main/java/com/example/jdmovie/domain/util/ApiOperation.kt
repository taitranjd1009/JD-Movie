package com.example.jdmovie.domain.util

//sealed interface ApiOperation<T>(val data: T? = null, val message: String? = null) {
//    class Success<T>(data: T?): ApiOperation<T>(data)
//    class Failure<T>(message: String, data: T? = null): ApiOperation<T>(data, message)
//}
sealed interface ApiOperation<T> {
    data class Success<T>(val data: T) : ApiOperation<T>
    data class Failure<T>(val exception: Exception) : ApiOperation<T>

    fun <R> mapSuccess(transform: (T) -> R): ApiOperation<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Failure -> Failure(exception)
        }
    }

    suspend fun onSuccess(block: suspend (T) -> Unit): ApiOperation<T> {
        if (this is Success) block(data)
        return this
    }

    fun onFailure(block: (Exception) -> Unit): ApiOperation<T> {
        if (this is Failure) block(exception)
        return this
    }
}

