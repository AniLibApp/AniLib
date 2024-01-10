package com.revolgenx.anilib.common.data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.common.data.constant.Config
import com.revolgenx.anilib.common.data.store.AuthPreferencesDataStore
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object Apollo {
    fun provideApolloClient(authPreferencesDataStore: AuthPreferencesDataStore): ApolloClient = ApolloClient.Builder()
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
                        runBlocking {
                            val token = authPreferencesDataStore.token.get()
                            if (token != null) {
                                req.newBuilder()
                                    .addHeader(
                                        "Authorization",
                                        "Bearer $token"
                                    )
                                    .build()
                            } else req
                        }
                    })
                }
                .build())
        .serverUrl(Config.API_URL)
        .build()
}