package com.revolgenx.anilib.common.data.service

import com.revolgenx.anilib.airing.data.service.AiringScheduleService
import com.revolgenx.anilib.airing.data.service.AiringScheduleServiceImpl
import com.revolgenx.anilib.character.data.service.CharacterService
import com.revolgenx.anilib.character.data.service.CharacterServiceImpl
import com.revolgenx.anilib.entry.data.service.MediaListEntryService
import com.revolgenx.anilib.entry.data.service.MediaListEntryServiceImpl
import com.revolgenx.anilib.list.data.service.MediaListService
import com.revolgenx.anilib.list.data.service.MediaListServiceImpl
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.data.service.MediaServiceImpl
import com.revolgenx.anilib.notification.data.service.NotificationService
import com.revolgenx.anilib.notification.data.service.NotificationServiceImpl
import com.revolgenx.anilib.staff.data.service.StaffService
import com.revolgenx.anilib.staff.data.service.StaffServiceImpl
import com.revolgenx.anilib.studio.data.service.StudioService
import com.revolgenx.anilib.studio.data.service.StudioServiceImpl
import com.revolgenx.anilib.user.data.service.UserService
import com.revolgenx.anilib.user.data.service.UserServiceImpl
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
}