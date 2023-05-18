package com.revolgenx.anilib.common.data.store

import android.content.Context
import org.koin.core.qualifier.named
import org.koin.dsl.module

val storeModules = module {
    single{ get<Context>().appDataStore }
    single(named(seasonFilterStoreFileName)) { get<Context>().seasonFilterDataStore }
    single(named(animeListFilterStoreFileName)) { get<Context>().animeListFilterDataStore }
    single(named(mangaListFilterStoreFileName)) { get<Context>().mangaListFilterDataStore }
}