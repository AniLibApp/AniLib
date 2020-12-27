package com.revolgenx.anilib.radio.repository.api

import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface RadioGithubService {

    companion object : KoinComponent {
        const val BASE_URL = "https://raw.githubusercontent.com/"
        const val REPO_URL = "${BASE_URL}rev0lgenX/AniLib-Radio/repo/"
        const val DEV_URL = "${BASE_URL}rev0lgenX/AniLib-Radio/dev/"

        private val client by lazy {
            val network: NetworkHelper by inject()
            network.client.newBuilder()
                .addNetworkInterceptor { chain ->
                    val originalResponse = chain.proceed(chain.request())
                    originalResponse.newBuilder()
                        .header("Content-Encoding", "gzip")
                        .header("Content-Type", "application/json")
                        .build()
                }
                .build()
        }

        fun create(): RadioGithubService {
            val adapter = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()

            return adapter.create(RadioGithubService::class.java)
        }

    }

    @GET("${REPO_URL}index.json.gz")
    suspend fun getRepo(): List<RadioStation>

    @GET("${DEV_URL}index.json.gz")
    suspend fun getDev(): List<RadioStation>
}