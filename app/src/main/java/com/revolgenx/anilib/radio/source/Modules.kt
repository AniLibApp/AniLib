package com.revolgenx.anilib.radio.source

import org.koin.dsl.module
val radioSourceModule = module {
    single { RadioStationSource(get(), get()) }
}