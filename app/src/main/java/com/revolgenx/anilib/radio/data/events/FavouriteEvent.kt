package com.revolgenx.anilib.radio.data.events

import com.revolgenx.anilib.infrastructure.event.BaseEvent

data class FavouriteEvent(val id: Long, val isFavourite:Boolean):BaseEvent()
