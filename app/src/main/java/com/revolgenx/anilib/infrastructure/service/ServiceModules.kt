package com.revolgenx.anilib.infrastructure.service

import com.revolgenx.anilib.airing.service.AiringMediaService
import com.revolgenx.anilib.airing.service.AiringMediaServiceImpl
import com.revolgenx.anilib.character.service.CharacterService
import com.revolgenx.anilib.character.service.CharacterServiceImpl
import com.revolgenx.anilib.entry.service.MediaEntryService
import com.revolgenx.anilib.entry.service.MediaEntryServiceImpl
import com.revolgenx.anilib.infrastructure.service.list.MediaListService
import com.revolgenx.anilib.infrastructure.service.list.MediaListServiceImpl
import com.revolgenx.anilib.infrastructure.service.media.*
import com.revolgenx.anilib.notification.service.NotificationService
import com.revolgenx.anilib.notification.service.NotificationServiceImpl
import com.revolgenx.anilib.home.recommendation.service.RecommendationService
import com.revolgenx.anilib.home.recommendation.service.RecommendationServiceImpl
import com.revolgenx.anilib.review.service.ReviewService
import com.revolgenx.anilib.review.service.ReviewServiceImpl
import com.revolgenx.anilib.app.setting.service.SettingService
import com.revolgenx.anilib.app.setting.service.SettingServiceImpl
import com.revolgenx.anilib.infrastructure.service.favourite.FavouriteService
import com.revolgenx.anilib.infrastructure.service.favourite.FavouriteServiceImpl
import com.revolgenx.anilib.staff.service.StaffService
import com.revolgenx.anilib.staff.service.StaffServiceImpl
import com.revolgenx.anilib.studio.service.StudioService
import com.revolgenx.anilib.studio.service.StudioServiceImpl
import com.revolgenx.anilib.infrastructure.service.toggle.ToggleService
import com.revolgenx.anilib.infrastructure.service.toggle.ToggleServiceImpl
import com.revolgenx.anilib.user.service.UserService
import com.revolgenx.anilib.user.service.UserServiceImpl
import com.revolgenx.anilib.user.service.UserStatsService
import com.revolgenx.anilib.user.service.UserStatsServiceImpl
import com.revolgenx.anilib.list.service.MediaListCollectionService
import com.revolgenx.anilib.media.service.MediaInfoService
import com.revolgenx.anilib.media.service.MediaInfoServiceImpl
import com.revolgenx.anilib.search.service.SearchService
import com.revolgenx.anilib.search.service.SearchServiceImpl
import org.koin.dsl.module

val serviceModule = module {
    factory<FavouriteService> { FavouriteServiceImpl(get()) }
    factory<MediaEntryService> { MediaEntryServiceImpl(get(), get()) }
    factory<RecommendationService> { RecommendationServiceImpl(get()) }
    factory<MediaInfoService> { MediaInfoServiceImpl(get()) }
    factory<CharacterService> { CharacterServiceImpl(get()) }
    factory<StaffService> { StaffServiceImpl(get()) }
    factory<StudioService> { StudioServiceImpl(get()) }
    factory<SearchService> { SearchServiceImpl(get()) }
    factory<MediaListService> { MediaListServiceImpl(get()) }
    factory<UserService> { UserServiceImpl(get()) }
    factory<UserStatsService> { UserStatsServiceImpl(get()) }
    factory<NotificationService> { NotificationServiceImpl(get()) }
    factory<AiringMediaService> { AiringMediaServiceImpl(get(), get()) }
    factory<MediaService> { MediaServiceImpl(get()) }
    factory<ReviewService> { ReviewServiceImpl(get()) }
    factory<SettingService> { SettingServiceImpl(get()) }
    factory<ToggleService> { ToggleServiceImpl(get()) }
    factory { MediaListCollectionService(get()) }
}