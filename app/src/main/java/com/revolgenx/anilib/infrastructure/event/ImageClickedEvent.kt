package com.revolgenx.anilib.infrastructure.event

import com.revolgenx.anilib.data.meta.ImageMeta

data class ImageClickedEvent(var meta: ImageMeta) : CommonEvent()