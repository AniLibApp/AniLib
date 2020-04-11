package com.revolgenx.anilib.event

import com.revolgenx.anilib.meta.MediaListFilterMeta

data class MediaListFilterEvent(var meta: MediaListFilterMeta) : BaseEvent()