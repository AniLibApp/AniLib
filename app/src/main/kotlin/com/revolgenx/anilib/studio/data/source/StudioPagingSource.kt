package com.revolgenx.anilib.studio.data.source

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.model.HeaderModel
import com.revolgenx.anilib.studio.data.field.StudioField
import com.revolgenx.anilib.studio.data.field.StudioMediaSort
import com.revolgenx.anilib.studio.data.service.StudioService
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

class StudioPagingSource(field: StudioField, private val studioService: StudioService) :
    BasePagingSource<BaseModel, StudioField>(field) {
    private var lastHeader: String = ""

    override suspend fun loadPage(): PageModel<BaseModel> {
        return studioService.getStudioMedia(field).let {
            if (field.sort == StudioMediaSort.START_DATE_DESC) {
                it.map {
                    val results = arrayListOf<BaseModel>()
                    for (mediaModel in it.data.orEmpty()) {
                        val header =
                            mediaModel.startDate?.year?.toString() ?: "TBA"

                        if (header != lastHeader) {
                            results.add(HeaderModel(header))
                            lastHeader = header
                        }
                        results.add(mediaModel)
                    }
                    PageModel(it.pageInfo, results)
                }
            } else it
        }.single() as PageModel<BaseModel>
    }
}