package com.revolgenx.anilib.infrastructure.event

import com.revolgenx.anilib.data.model.search.filter.MediaSearchFilterModel

data class BrowseGenreEvent(val genre: MediaSearchFilterModel) : CommonEvent()