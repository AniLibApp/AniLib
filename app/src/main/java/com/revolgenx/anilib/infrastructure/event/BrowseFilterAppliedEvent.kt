package com.revolgenx.anilib.infrastructure.event

import com.revolgenx.anilib.data.model.search.filter.SearchFilterModel

data class BrowseFilterAppliedEvent(var filterModel: SearchFilterModel) : CommonEvent()