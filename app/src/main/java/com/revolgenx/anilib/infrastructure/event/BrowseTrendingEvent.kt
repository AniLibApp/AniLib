package com.revolgenx.anilib.infrastructure.event

import com.revolgenx.anilib.data.model.search.filter.MediaSearchFilterModel

data class BrowseTrendingEvent(val trending: MediaSearchFilterModel) : CommonEvent()
