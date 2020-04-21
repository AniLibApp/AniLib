package com.revolgenx.anilib.event

import android.view.View
import com.revolgenx.anilib.meta.ImageMeta

data class ImageClickedEvent(var meta: ImageMeta) : BaseEvent()