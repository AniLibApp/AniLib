package com.revolgenx.anilib.event

import com.revolgenx.anilib.model.search.filter.MediaBrowseFilterModel

data class BrowseNewlyAddedEvent(val newlyAdded:MediaBrowseFilterModel):CommonEvent()