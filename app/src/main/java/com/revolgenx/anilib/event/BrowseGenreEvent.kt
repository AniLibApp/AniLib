package com.revolgenx.anilib.event

import com.revolgenx.anilib.model.search.filter.MediaBrowseFilterModel

data class BrowseGenreEvent(val genre: MediaBrowseFilterModel) : CommonEvent()