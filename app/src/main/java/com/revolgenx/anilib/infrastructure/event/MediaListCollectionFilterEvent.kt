package com.revolgenx.anilib.infrastructure.event

import com.revolgenx.anilib.data.meta.MediaListFilterMeta

data class MediaListCollectionFilterEvent(var meta: MediaListFilterMeta) : CommonEvent()