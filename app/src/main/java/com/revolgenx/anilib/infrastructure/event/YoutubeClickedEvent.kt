package com.revolgenx.anilib.infrastructure.event

import com.revolgenx.anilib.data.meta.YoutubeMeta

data class YoutubeClickedEvent(var meta: YoutubeMeta) :CommonEvent()
