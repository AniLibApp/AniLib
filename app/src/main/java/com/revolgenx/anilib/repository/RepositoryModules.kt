package com.revolgenx.anilib.repository

import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.network.NetworkProvider
import com.revolgenx.anilib.repository.network.GraphRepositoryImpl
import org.koin.dsl.module

val networkModules = module {
    single { NetworkProvider.provideApolloClient(get()) }
    single { NetworkProvider.provideOkHttpClient() }
}

val repositoryModules = module {
    factory<BaseGraphRepository> { GraphRepositoryImpl(get()) }
}