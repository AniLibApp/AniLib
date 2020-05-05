package com.revolgenx.anilib.service

import com.revolgenx.anilib.service.airing.AiringMediaService
import com.revolgenx.anilib.service.airing.AiringMediaServiceImpl
import com.revolgenx.anilib.service.character.CharacterService
import com.revolgenx.anilib.service.character.CharacterServiceImpl
import com.revolgenx.anilib.service.list.MediaListService
import com.revolgenx.anilib.service.list.MediaListServiceImpl
import com.revolgenx.anilib.service.media.*
import com.revolgenx.anilib.service.notification.NotificationService
import com.revolgenx.anilib.service.notification.NotificationServiceImpl
import com.revolgenx.anilib.service.recommendation.RecommendationService
import com.revolgenx.anilib.service.recommendation.RecommendationServiceImpl
import com.revolgenx.anilib.service.staff.StaffService
import com.revolgenx.anilib.service.staff.StaffServiceImpl
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
    factory<NotificationService> { NotificationServiceImpl(get()) }
    factory<AiringMediaService> { AiringMediaServiceImpl(get()) }
    factory<MediaService> { MediaServiceImpl(get()) }
}