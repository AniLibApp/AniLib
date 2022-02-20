package com.revolgenx.anilib.media.event

import com.revolgenx.anilib.common.event.CommonEvent

data class BrowseRankEvent(val rankType: Int,val  mediaType: Int?): CommonEvent()
