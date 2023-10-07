package com.revolgenx.anilib.common.data.store

import android.content.Context
import com.revolgenx.anilib.home.explore.data.store.ExploreMediaDataStore
import com.revolgenx.anilib.home.explore.data.store.exploreNewlyAddedDataStore
import com.revolgenx.anilib.home.explore.data.store.explorePopularDataStore
import com.revolgenx.anilib.home.explore.data.store.exploreTrendingDataStore
import com.revolgenx.anilib.home.season.data.store.SeasonDataStore
import com.revolgenx.anilib.home.season.data.store.seasonFilterDataStore
import com.revolgenx.anilib.list.data.store.animeListFilterDataStore
import com.revolgenx.anilib.list.data.store.mangaListFilterDataStore
import com.revolgenx.anilib.setting.data.store.MediaSettingsDataStore
import org.koin.core.scope.Scope
import org.koin.dsl.module

val storeModules = module {
    single { get<Context>().preferencesDataStore }
    single { AppDataStore(get()) }
    single { AuthDataStore(get()) }
    single { ThemeDataStore(get()) }
    single { MediaSettingsDataStore(get()) }
    single { SeasonDataStore(seasonFilterDataStore()) }
    single { ExploreMediaDataStore.ExploreTrendingDataStore(exploreTrendingDataStore()) }
    single { ExploreMediaDataStore.ExplorePopularDataStore(explorePopularDataStore()) }
    single { ExploreMediaDataStore.ExploreNewlyAddedDataStore(exploreNewlyAddedDataStore()) }
}

fun Scope.seasonFilterDataStore() = get<Context>().seasonFilterDataStore
fun Scope.animeListFilterDataStore() = get<Context>().animeListFilterDataStore
fun Scope.mangaListFilterDataStore() = get<Context>().mangaListFilterDataStore
fun Scope.exploreTrendingDataStore() = get<Context>().exploreTrendingDataStore
fun Scope.explorePopularDataStore() = get<Context>().explorePopularDataStore
fun Scope.exploreNewlyAddedDataStore() = get<Context>().exploreNewlyAddedDataStore