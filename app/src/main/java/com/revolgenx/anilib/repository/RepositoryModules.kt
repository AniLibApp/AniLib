package com.revolgenx.anilib.repository

import com.revolgenx.anilib.repository.network.NetworkProvider
import com.revolgenx.anilib.repository.network.GraphRepository
import org.koin.dsl.module

val networkModules = module {
    single { NetworkProvider.provideApolloClient(get()) }
    single { NetworkProvider.provideOkHttpClient() }
}

val repositoryModules = module {
    factory { GraphRepository(get()) }
}