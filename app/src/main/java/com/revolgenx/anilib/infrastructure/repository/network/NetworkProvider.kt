package com.revolgenx.anilib.infrastructure.repository.network

import android.content.Context
import com.apollographql.apollo.ApolloClient
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

    fun provideApolloClient(context: Context): ApolloClient = ApolloClient.builder()
        .okHttpClient(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addInterceptor {
                    if (!context.loggedIn()) {
                        it.proceed(it.request())
                    } else {
                        val request = it.request()
                        val newRequest: Request
                        newRequest = request.newBuilder()
                            .addHeader(
                                "Authorization",
                                "Bearer ${context.token()}"
                            )
                            .build()
                        it.proceed(newRequest)
                    }


                }
                .build())
        .serverUrl(ANILIST_API_URL)
        .build()
}