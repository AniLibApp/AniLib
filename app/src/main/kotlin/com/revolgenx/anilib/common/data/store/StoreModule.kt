package com.revolgenx.anilib.common.data.store

import android.content.Context
import com.revolgenx.anilib.browse.data.store.BrowsePreferencesDataStore
import com.revolgenx.anilib.common.data.store.theme.ThemeDataStore
import com.revolgenx.anilib.list.data.store.MediaListEntryEventStore
import com.revolgenx.anilib.setting.data.store.MediaSettingsPreferencesDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope
import org.koin.dsl.module

val storeModules = module {
    single { androidContext().preferencesDataStore }
    single { ThemeDataStore(androidContext().themeDataStore) }
    single { GeneralPreferencesDataStore(get()) }
    single { AuthPreferencesDataStore(get()) }
    single { MediaSettingsPreferencesDataStore(get()) }
    single { BrowsePreferencesDataStore(get()) }
    single { MediaListEntryEventStore() }
}

fun Scope.animeListFilterDataStore() = androidContext().animeListFilterDataStore
fun Scope.mangaListFilterDataStore() = get<Context>().mangaListFilterDataStore
