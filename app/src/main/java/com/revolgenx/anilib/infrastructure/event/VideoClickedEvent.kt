package com.revolgenx.anilib.infrastructure.event

import com.revolgenx.anilib.data.meta.VideoMeta

data class VideoClickedEvent(var videoMeta: VideoMeta):CommonEvent()
