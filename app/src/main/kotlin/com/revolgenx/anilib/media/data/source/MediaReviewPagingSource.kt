package com.revolgenx.anilib.media.data.source

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.media.data.field.MediaReviewField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.review.ui.model.ReviewModel
import kotlinx.coroutines.flow.single

class MediaReviewPagingSource(
    field: MediaReviewField,
    private val service: MediaService
) : BasePagingSource<ReviewModel, MediaReviewField>(field) {

    override suspend fun loadPage(): PageModel<ReviewModel> {
        return service.getMediaReviewList(field).single()
    }

}