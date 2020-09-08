package com.revolgenx.anilib.event

import com.revolgenx.anilib.meta.MediaListFilterMeta

data class MediaListCollectionFilterEvent(var meta: MediaListFilterMeta) : CommonEvent()