package com.revolgenx.anilib.common.data.store

import android.content.Context
import com.revolgenx.anilib.season.data.store.StoreFileName
import com.revolgenx.anilib.season.data.store.seasonFilterStore
import org.koin.core.qualifier.named
import org.koin.dsl.module

val storeModules = module {
    single(named(StoreFileName.seasonFilterFileName)) { get<Context>().seasonFilterStore }
}