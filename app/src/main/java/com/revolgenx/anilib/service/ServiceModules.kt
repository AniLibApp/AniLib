package com.revolgenx.anilib.service

import com.revolgenx.anilib.service.studio.StudioService
import com.revolgenx.anilib.service.studio.StudioServiceImpl
import org.koin.dsl.module

val serviceModule = module {
    factory<ToggleService> { ToggleServiceImpl(get()) }
    factory<MediaListEntryService> { MediaListEntryServiceImpl(get(), get()) }
    factory<RecommendationService> { RecommendationServiceImpl(get()) }
    factory<MediaBrowseService> { MediaBrowseServiceImpl(get()) }
    factory<CharacterService> { CharacterServiceImpl(get()) }
    factory<StaffService> { StaffServiceImpl(get()) }
    factory<StudioService> { StudioServiceImpl(get()) }
    factory<AdvanceSearchService> { AdvanceSearchServiceImpl(get()) }
}