package com.revolgenx.anilib.event

import com.revolgenx.anilib.model.search.filter.SearchFilterModel

data class BrowseFilterAppliedEvent(var filterModel: SearchFilterModel) : CommonEvent()