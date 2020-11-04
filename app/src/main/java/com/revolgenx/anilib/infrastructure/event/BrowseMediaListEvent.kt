package com.revolgenx.anilib.infrastructure.event

import com.revolgenx.anilib.data.meta.MediaListMeta

data class BrowseMediaListEvent(val mediaListMeta: MediaListMeta) : CommonEvent()
