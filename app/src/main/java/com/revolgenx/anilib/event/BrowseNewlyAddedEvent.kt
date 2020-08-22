package com.revolgenx.anilib.event

import com.revolgenx.anilib.model.search.filter.MediaSearchFilterModel

data class BrowseNewlyAddedEvent(val newlyAdded:MediaSearchFilterModel):CommonEvent()