package com.revolgenx.anilib.event

import com.revolgenx.anilib.model.search.filter.MediaSearchFilterModel

data class BrowseGenreEvent(val genre: MediaSearchFilterModel) : CommonEvent()