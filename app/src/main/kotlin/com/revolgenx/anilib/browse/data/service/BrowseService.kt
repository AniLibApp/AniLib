package com.revolgenx.anilib.browse.data.service

import com.revolgenx.anilib.browse.data.field.BrowseField
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.ui.model.BaseModel
import kotlinx.coroutines.flow.Flow

interface BrowseService {
    fun browse(browseField: BrowseField): Flow<PageModel<BaseModel>>
}