package com.revolgenx.anilib.common.data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.common.data.store.AppDataStore
import com.revolgenx.anilib.common.data.store.token
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object Apollo {
    fun provideApolloClient(appDataStore: AppDataStore): ApolloClient = ApolloClient.Builder()
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
                            val token = appDataStore.token().first()
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
        .serverUrl(BuildConfig.API_URL)
        .build()
}