package com.revolgenx.anilib.event


data class DisplayModeChangedEvent(val whichDisplay: DisplayTypes) : BaseEvent()

enum class DisplayTypes {
    MEDIA_LIST
}