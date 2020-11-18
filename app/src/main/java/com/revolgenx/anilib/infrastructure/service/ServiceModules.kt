package com.revolgenx.anilib.infrastructure.service

import com.revolgenx.anilib.infrastructure.service.airing.AiringMediaService
import com.revolgenx.anilib.infrastructure.service.airing.AiringMediaServiceImpl
import com.revolgenx.anilib.infrastructure.service.character.CharacterService
import com.revolgenx.anilib.infrastructure.service.character.CharacterServiceImpl
import com.revolgenx.anilib.infrastructure.service.list.MediaListService
import com.revolgenx.anilib.infrastructure.service.list.MediaListServiceImpl
import com.revolgenx.anilib.infrastructure.service.media.*
import com.revolgenx.anilib.infrastructure.service.notification.NotificationService
import com.revolgenx.anilib.infrastructure.service.notification.NotificationServiceImpl
import com.revolgenx.anilib.infrastructure.service.recommendation.RecommendationService
import com.revolgenx.anilib.infrastructure.service.recommendation.RecommendationServiceImpl
import com.revolgenx.anilib.infrastructure.service.review.ReviewService
import com.revolgenx.anilib.infrastructure.service.review.ReviewServiceImpl
import com.revolgenx.anilib.infrastructure.service.staff.StaffService
import com.revolgenx.anilib.infrastructure.service.staff.StaffServiceImpl
import com.revolgenx.anilib.infrastructure.service.studio.StudioService
import com.revolgenx.anilib.infrastructure.service.studio.StudioServiceImpl
import com.revolgenx.anilib.infrastructure.service.user.UserService
import com.revolgenx.anilib.infrastructure.service.user.UserServiceImpl
import com.revolgenx.anilib.infrastructure.service.user.UserStatsService
import com.revolgenx.anilib.infrastructure.service.user.UserStatsServiceImpl
import org.koin.dsl.module

val serviceModule = module {
    factory<ToggleService> { ToggleServiceImpl(get()) }
    factory<MediaListEntryService> { MediaListEntryServiceImpl(get(), get()) }
    factory<RecommendationService> { RecommendationServiceImpl(get()) }
    factory<MediaBrowseService> { MediaBrowseServiceImpl(get()) }
    factory<CharacterService> { CharacterServiceImpl(get()) }
    factory<StaffService> { StaffServiceImpl(get()) }
    factory<StudioService> { StudioServiceImpl(get()) }
    factory<SearchService> { SearchServiceImpl(get()) }
    factory<MediaListService> { MediaListServiceImpl(get()) }
    factory<UserService> { UserServiceImpl(get()) }
    factory<UserStatsService> { UserStatsServiceImpl(get()) }
    factory<NotificationService> { NotificationServiceImpl(get()) }
    factory<AiringMediaService> { AiringMediaServiceImpl(get()) }
    factory<MediaService> { MediaServiceImpl(get()) }
    factory<ReviewService> { ReviewServiceImpl(get()) }
}