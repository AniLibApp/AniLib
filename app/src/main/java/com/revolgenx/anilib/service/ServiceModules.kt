package com.revolgenx.anilib.service

import com.revolgenx.anilib.service.list.MediaListService
import com.revolgenx.anilib.service.list.MediaListServiceImpl
import com.revolgenx.anilib.service.studio.StudioService
import com.revolgenx.anilib.service.studio.StudioServiceImpl
import com.revolgenx.anilib.service.user.UserService
import com.revolgenx.anilib.service.user.UserServiceImpl
import com.revolgenx.anilib.service.user.UserStatsService
import com.revolgenx.anilib.service.user.UserStatsServiceImpl
import org.koin.dsl.module

val serviceModule = module {
    factory<ToggleService> { ToggleServiceImpl(get()) }
    factory<MediaListEntryService> { MediaListEntryServiceImpl(get(), get()) }
    factory<RecommendationService> { RecommendationServiceImpl(get()) }
    factory<MediaBrowseService> { MediaBrowseServiceImpl(get()) }
    factory<CharacterService> { CharacterServiceImpl(get()) }
    factory<StaffService> { StaffServiceImpl(get()) }
    factory<StudioService> { StudioServiceImpl(get()) }
    factory<BrowseService> { BrowseServiceImpl(get()) }
    factory<MediaListService> { MediaListServiceImpl(get()) }
    factory<UserService> { UserServiceImpl(get()) }
    factory<UserStatsService> { UserStatsServiceImpl(get()) }
}