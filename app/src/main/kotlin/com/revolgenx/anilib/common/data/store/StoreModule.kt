package com.revolgenx.anilib.common.data.store

import android.content.Context
import com.revolgenx.anilib.common.data.store.theme.CustomThemeDataStore
import com.revolgenx.anilib.common.data.store.theme.ThemeDataStore
import com.revolgenx.anilib.common.data.store.theme.WidgetThemeDataStore
import com.revolgenx.anilib.list.data.store.MediaListEntryEventStore
import com.revolgenx.anilib.notification.data.store.NotificationDataStore
import com.revolgenx.anilib.setting.data.store.BillingDataStore
import com.revolgenx.anilib.widget.viewmodel.AiringWidgetResource
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module

val storeModules = module {
    single { androidContext().preferencesDataStore }
    single(named("theme_data_store")) { androidContext().themeDataStore }
    single { ThemeDataStore(get(named("theme_data_store"))) }
    single { CustomThemeDataStore(androidContext().customThemeDataStore(get(named("theme_data_store")))) }
    single { WidgetThemeDataStore(androidContext().widgetThemeDataStore(get(named("theme_data_store")))) }
    single { AppPreferencesDataStore(get()) }
    single { MediaListEntryEventStore() }
    single { NotificationDataStore(get()) }
    single { AiringWidgetResource(get(), get()) }
    single { BillingDataStore(androidContext().preferencesDataStore) }
}

fun Scope.animeListFilterDataStore() = androidContext().animeListFilterDataStore
fun Scope.mangaListFilterDataStore() = get<Context>().mangaListFilterDataStore
