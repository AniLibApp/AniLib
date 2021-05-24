package com.revolgenx.anilib.social.infrastructure.service

import org.koin.dsl.module

val activityServiceModules = module {
    factory<ActivityUnionService> { ActivityUnionServiceImpl(get())}
}