package com.revolgenx.anilib.infrastructure.event

data class ChangeViewPagerPageEvent(val data: ToPage) : BaseEvent()


interface ToPage {}
enum class MainActivityPage : ToPage {
    HOME, LIST, RADIO
}

enum class ListContainerFragmentPage : ToPage {
    ANIME, MANGA
}
