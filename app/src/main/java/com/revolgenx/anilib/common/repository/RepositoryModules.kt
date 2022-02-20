package com.revolgenx.anilib.common.repository

import com.revolgenx.anilib.common.repository.network.BaseGraphRepository
import com.revolgenx.anilib.common.repository.network.GraphRepositoryImpl
import com.revolgenx.anilib.common.repository.network.NetworkProvider
import org.koin.dsl.module

val networkModules = module {
    single { NetworkProvider.provideApolloClient(get()) }
    single { NetworkProvider.provideOkHttpClient() }
}

val repositoryModules = module {
    factory<BaseGraphRepository> { GraphRepositoryImpl(get()) }
}