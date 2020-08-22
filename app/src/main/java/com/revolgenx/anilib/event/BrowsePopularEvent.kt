package com.revolgenx.anilib.event

import com.revolgenx.anilib.model.search.filter.MediaSearchFilterModel

data class BrowsePopularEvent(val popular: MediaSearchFilterModel):CommonEvent()
