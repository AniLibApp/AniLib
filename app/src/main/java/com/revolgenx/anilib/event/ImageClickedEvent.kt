package com.revolgenx.anilib.event

import com.revolgenx.anilib.meta.ImageMeta

data class ImageClickedEvent(var meta: ImageMeta) : CommonEvent()