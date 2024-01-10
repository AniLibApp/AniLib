package com.revolgenx.anilib.common.data.store

import android.content.Context
import com.revolgenx.anilib.common.data.store.animeListFilterDataStore
import com.revolgenx.anilib.common.data.store.mangaListFilterDataStore
import com.revolgenx.anilib.setting.data.store.MediaSettingsPreferencesDataStore
import org.koin.core.scope.Scope
import org.koin.dsl.module

val storeModules = module {
    single { get<Context>().preferencesDataStore }
    single { AppPreferencesDataStore(get()) }
    single { AuthPreferencesDataStore(get()) }
    single { ThemePreferencesDataStore(get()) }
    single { MediaSettingsPreferencesDataStore(get()) }
}

fun Scope.animeListFilterDataStore() = get<Context>().animeListFilterDataStore
fun Scope.mangaListFilterDataStore() = get<Context>().mangaListFilterDataStore
