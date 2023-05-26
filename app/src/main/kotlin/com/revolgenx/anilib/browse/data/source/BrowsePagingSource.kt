package com.revolgenx.anilib.browse.data.source

import com.revolgenx.anilib.browse.data.field.BrowseField
import com.revolgenx.anilib.browse.data.service.BrowseService
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.common.ui.model.BaseModel
import kotlinx.coroutines.flow.single

class BrowsePagingSource(
    field: BrowseField,
    private val service: BrowseService
) : BasePagingSource<BaseModel, BrowseField>(field) {

    override suspend fun loadPage(): PageModel<BaseModel> {
        return service.browse(field).single()
    }
}