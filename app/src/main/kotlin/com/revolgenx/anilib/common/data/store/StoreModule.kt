package com.revolgenx.anilib.common.data.store

import android.content.Context
import com.revolgenx.anilib.setting.data.store.MediaSettingsDataStore
import org.koin.core.qualifier.named
import org.koin.dsl.module

val storeModules = module {
    single { get<Context>().preferencesDataStore }
    single(named(seasonFilterStoreFileName)) { get<Context>().seasonFilterDataStore }
    single(named(animeListFilterStoreFileName)) { get<Context>().animeListFilterDataStore }
    single(named(mangaListFilterStoreFileName)) { get<Context>().mangaListFilterDataStore }
    single { AppDataStore(get()) }
    single { AuthDataStore(get()) }
    single { ThemeDataStore(get()) }
    single { MediaSettingsDataStore(get()) }
}