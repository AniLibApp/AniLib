package com.revolgenx.anilib.infrastructure.repository

import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.GraphRepositoryImpl
import com.revolgenx.anilib.infrastructure.repository.network.NetworkProvider
import org.koin.dsl.module

val networkModules = module {
    single { NetworkProvider.provideApolloClient(get()) }
    single { NetworkProvider.provideOkHttpClient() }
}

val repositoryModules = module {
    factory<BaseGraphRepository> { GraphRepositoryImpl(get()) }
}