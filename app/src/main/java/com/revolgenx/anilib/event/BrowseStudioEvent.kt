package com.revolgenx.anilib.event

import com.revolgenx.anilib.meta.StudioMeta

data class BrowseStudioEvent(var meta: StudioMeta) : BaseEvent()
