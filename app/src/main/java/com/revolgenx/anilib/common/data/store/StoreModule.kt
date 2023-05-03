package com.revolgenx.anilib.common.data.store

import android.content.Context
import org.koin.core.qualifier.named
import org.koin.dsl.module

val storeModules = module {
    single{ get<Context>().appPreferenceDataStore }
    single(named(StoreFileName.seasonFieldFileName)) { get<Context>().seasonFieldDataStore }
    single { AuthDataStore(get()) }
}