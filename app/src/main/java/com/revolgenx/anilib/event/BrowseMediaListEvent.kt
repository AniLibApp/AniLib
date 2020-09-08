package com.revolgenx.anilib.event

import com.revolgenx.anilib.meta.MediaListMeta

data class BrowseMediaListEvent(val mediaListMeta: MediaListMeta) : CommonEvent()
