package com.revolgenx.anilib.common.data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.common.data.constant.Config
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object Apollo {
    fun provideApolloClient(appPreferencesDataStore: AppPreferencesDataStore): ApolloClient =
        ApolloClient.Builder()
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
                            val token = appPreferencesDataStore.token.get()
                            if (token != null) {
                                var tokenExpiresAt = appPreferencesDataStore.tokenExpiresAt.get()
                                if (tokenExpiresAt == null) {
                                    tokenExpiresAt = runBlocking {
                                        appPreferencesDataStore.setTokenExpiresAt(token)
                                        appPreferencesDataStore.tokenExpiresAt.get()!!
                                    }
                                }

                                if (tokenExpiresAt < System.currentTimeMillis()) {
                                    appPreferencesDataStore.tokenHasExpired.value = true
                                }

                                req.newBuilder()
                                    .addHeader(
                                        "Authorization",
                                        "Bearer $token"
                                    )
                                    .build()
                            } else req

                        })
                    }
                    .build())
            .serverUrl(Config.API_URL)
            .build()
}