package com.revolgenx.anilib.event

import com.revolgenx.anilib.model.search.filter.MediaBrowseFilterModel

data class BrowsePopularEvent(val popular: MediaBrowseFilterModel):BaseEvent()
