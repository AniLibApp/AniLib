package com.revolgenx.anilib.home.event

import com.revolgenx.anilib.common.event.BaseEvent

data class ChangeViewPagerPageEvent(val data: ToPage) : BaseEvent()


interface ToPage {}
enum class MainActivityPage : ToPage {
    HOME, LIST, RADIO
}

enum class ListContainerFragmentPage : ToPage {
    ANIME, MANGA
}
