package com.revolgenx.anilib.infrastructure.event


data class DisplayModeChangedEvent(val whichDisplay: DisplayTypes) : BaseEvent()

enum class DisplayTypes {
    MEDIA_LIST
}
