package com.revolgenx.anilib.common.data.repository

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val repositoryModules = module {
    single { Apollo.provideApolloClient() }
    factoryOf(::ApolloRepositoryImpl) { bind<ApolloRepository>() }
}