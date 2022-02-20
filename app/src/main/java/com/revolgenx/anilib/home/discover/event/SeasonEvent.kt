package com.revolgenx.anilib.home.discover.event

import com.revolgenx.anilib.common.event.BaseEvent

sealed class SeasonEvent: BaseEvent() {
    object SeasonFilterEvent : SeasonEvent()
    object SeasonChangeEvent : SeasonEvent()
    object SeasonTagEvent : SeasonEvent()
    object SeasonGenreEvent : SeasonEvent()
    data class SeasonHeaderEvent(val showHeader:Boolean) : SeasonEvent()
}