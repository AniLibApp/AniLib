package com.revolgenx.anilib.event

import com.revolgenx.anilib.model.search.filter.MediaSearchFilterModel

data class BrowseTrendingEvent(val trending: MediaSearchFilterModel) : CommonEvent()
