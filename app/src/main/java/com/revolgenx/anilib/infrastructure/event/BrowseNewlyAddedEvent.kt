package com.revolgenx.anilib.infrastructure.event

import com.revolgenx.anilib.data.model.search.filter.MediaSearchFilterModel

data class BrowseNewlyAddedEvent(val newlyAdded:MediaSearchFilterModel):CommonEvent()