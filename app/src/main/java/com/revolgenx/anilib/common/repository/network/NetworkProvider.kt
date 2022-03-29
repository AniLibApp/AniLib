package com.revolgenx.anilib.common.repository.network

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.preference.token
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

object NetworkProvider {
    private const val ANILIST_API_URL = "https://graphql.anilist.co"

    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().build()
    }

    fun provideApolloClient(context: Context): ApolloClient = ApolloClient.Builder()
        .okHttpClient(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addInterceptor {
                    if (!loggedIn()) {
                        it.proceed(it.request())
                    } else {
                        val request = it.request()
                        val newRequest: Request = request.newBuilder()
                            .addHeader(
                                "Authorization",
                                "Bearer ${token()}"
                            )
                            .build()
                        it.proceed(newRequest)
                    }


                }
                .build())
        .serverUrl(ANILIST_API_URL)
        .build()
}