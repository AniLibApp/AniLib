package com.revolgenx.anilib.service

import org.koin.dsl.module

val serviceModule = module {
    factory<ToggleService> { ToggleServiceImpl(get()) }
    factory<MediaListEntryService> { MediaListEntryServiceImpl(get(), get()) }
    factory<RecommendationService> { RecommendationServiceImpl(get()) }
}