package com.revolgenx.anilib.common.data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.revolgenx.anilib.app.preference.Auth
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.revolgenx.anilib.BuildConfig

object Apollo {
    fun provideApolloClient(): ApolloClient = ApolloClient.Builder()
        .okHttpClient(
            OkHttpClient.Builder().apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }
                .addInterceptor {
                    it.proceed(it.request().let { req ->
                        if (Auth.loggedIn) {
                            req.newBuilder()
                                .addHeader(
                                    "Authorization",
                                    "Bearer ${Auth.token}"
                                )
                                .build()
                        } else req
                    })
                }
                .build())
        .serverUrl(BuildConfig.API_URL)
        .build()
}