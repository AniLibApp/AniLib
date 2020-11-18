package com.revolgenx.anilib.infrastructure.event

import com.revolgenx.anilib.data.meta.StudioMeta

data class BrowseStudioEvent(var meta: StudioMeta) : CommonEvent()
