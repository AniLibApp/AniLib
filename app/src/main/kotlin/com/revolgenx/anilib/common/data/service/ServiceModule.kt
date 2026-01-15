package com.revolgenx.anilib.common.data.service

import android.content.Context
import com.revolgenx.anilib.airing.data.service.AiringScheduleService
import com.revolgenx.anilib.airing.data.service.AiringScheduleServiceImpl
import com.revolgenx.anilib.browse.data.service.BrowseService
import com.revolgenx.anilib.browse.data.service.BrowseServiceImpl
import com.revolgenx.anilib.character.data.service.CharacterService
import com.revolgenx.anilib.character.data.service.CharacterServiceImpl
import com.revolgenx.anilib.common.data.exporter.MALExportService
import com.revolgenx.anilib.common.data.store.airingScheduleWidgetDataStore
import com.revolgenx.anilib.common.data.store.genreCollectionDataStore
import com.revolgenx.anilib.common.data.store.mediaTagCollectionDataStore
import com.revolgenx.anilib.entry.data.service.MediaListEntryService
import com.revolgenx.anilib.entry.data.service.MediaListEntryServiceImpl
import com.revolgenx.anilib.home.recommendation.data.service.RecommendationService
import com.revolgenx.anilib.home.recommendation.data.service.RecommendationServiceImpl
import com.revolgenx.anilib.list.data.service.MediaListService
import com.revolgenx.anilib.list.data.service.MediaListServiceImpl
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.data.service.MediaServiceImpl
import com.revolgenx.anilib.notification.data.service.NotificationService
import com.revolgenx.anilib.notification.data.service.NotificationServiceImpl
import com.revolgenx.anilib.review.data.service.ReviewService
import com.revolgenx.anilib.review.data.service.ReviewServiceImpl
import com.revolgenx.anilib.setting.data.service.SettingsService
import com.revolgenx.anilib.setting.data.service.SettingsServiceImpl
import com.revolgenx.anilib.social.data.service.ActivityUnionService
import com.revolgenx.anilib.social.data.service.ActivityUnionServiceImpl
import com.revolgenx.anilib.staff.data.service.StaffService
import com.revolgenx.anilib.staff.data.service.StaffServiceImpl
import com.revolgenx.anilib.studio.data.service.StudioService
import com.revolgenx.anilib.studio.data.service.StudioServiceImpl
import com.revolgenx.anilib.user.data.service.UserService
import com.revolgenx.anilib.user.data.service.UserServiceImpl
import com.revolgenx.anilib.widget.data.service.AiringScheduleWidgetService
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val serviceModules = module {
    factoryOf(::MediaServiceImpl) { bind<MediaService>() }
    factoryOf(::CharacterServiceImpl) { bind<CharacterService>() }
    factoryOf(::StaffServiceImpl) { bind<StaffService>() }
    factoryOf(::AiringScheduleServiceImpl) { bind<AiringScheduleService>() }
    factoryOf(::MediaListServiceImpl) { bind<MediaListService>() }
    factoryOf(::UserServiceImpl) { bind<UserService>() }
    factoryOf(::NotificationServiceImpl) { bind<NotificationService>() }
    factoryOf(::StudioServiceImpl) { bind<StudioService>() }
    factoryOf(::MediaListEntryServiceImpl) { bind<MediaListEntryService>() }
    factoryOf(::ActivityUnionServiceImpl) { bind<ActivityUnionService>() }
    factoryOf(::BrowseServiceImpl) { bind<BrowseService>() }
    factoryOf(::ReviewServiceImpl) { bind<ReviewService>() }
    factoryOf(::RecommendationServiceImpl) { bind<RecommendationService>() }
    factory<SettingsService> {
        SettingsServiceImpl(
            get(),
            get(),
            get<Context>().mediaTagCollectionDataStore,
            get<Context>().genreCollectionDataStore
        )
    }
    factoryOf(::ToggleServiceImpl) { bind<ToggleService>() }
    factory { AiringScheduleWidgetService(get(), get(), androidContext().airingScheduleWidgetDataStore) }
    single{ MALExportService()}
}