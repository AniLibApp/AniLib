package com.revolgenx.anilib.radio.repository.api

import com.revolgenx.anilib.BuildConfig
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import java.io.IOException

class NetworkHelper {
    val client by lazy {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            }
            builder.addInterceptor(httpLoggingInterceptor)
        }
        builder.build()
    }
}


sealed class ResourceWrapper<out T> {
    data class Success<out T>(val value: T) : ResourceWrapper<T>()
    data class GenericError(val code: Int? = null, val error: ErrorResponse? = null) :
        ResourceWrapper<Nothing>()

    data class UnknownError(val error: ErrorResponse? = null) : ResourceWrapper<Nothing>()

    object Loading : ResourceWrapper<Nothing>()
    object NetworkError : ResourceWrapper<Nothing>()
}

data class ErrorResponse(
    val errorMsg: String?,
    val causes: Map<String, String>? = null
)


suspend fun <T> requestApi(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResourceWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResourceWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ResourceWrapper.NetworkError
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    ResourceWrapper.GenericError(code, errorResponse)
                }
                else -> {
                    ResourceWrapper.UnknownError(ErrorResponse(throwable.message))
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
    return try {
        throwable.response()?.errorBody()?.source()?.let {
            val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponse::class.java)
            moshiAdapter.fromJson(it)
        }
    } catch (exception: Exception) {
        ErrorResponse(throwable.message)
    }
}