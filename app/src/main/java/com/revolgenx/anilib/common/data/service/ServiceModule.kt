package com.revolgenx.anilib.common.data.service

import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.data.service.MediaServiceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val serviceModules = module {
    factoryOf(::MediaServiceImpl) { bind<MediaService>() }
}