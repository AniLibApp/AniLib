package com.revolgenx.anilib.home.event

import com.revolgenx.anilib.common.event.BaseEvent

data class ChangeViewPagerPageEvent(val data: MainActivityPage) : BaseEvent()
enum class MainActivityPage{
    HOME, LIST
}

