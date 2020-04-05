package com.revolgenx.anilib.event

import com.revolgenx.anilib.model.search.filter.BrowseFilterModel

data class BrowseFilterAppliedEvent(var filterModel: BrowseFilterModel) : BaseEvent()