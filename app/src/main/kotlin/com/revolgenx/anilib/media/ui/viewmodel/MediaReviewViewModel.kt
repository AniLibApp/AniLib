package com.revolgenx.anilib.media.ui.viewmodel

import com.revolgenx.anilib.common.ui.screen.PagingViewModel
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.media.data.field.MediaReviewField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.data.source.MediaReviewPagingSource
import com.revolgenx.anilib.review.ui.model.ReviewModel
import kotlinx.coroutines.flow.Flow

class MediaReviewViewModel(private val mediaService: MediaService) :
    PagingViewModel<ReviewModel, MediaReviewField, MediaReviewPagingSource>() {
    override val field = MediaReviewField()

    override val pagingSource: MediaReviewPagingSource
        get() = MediaReviewPagingSource(this.field, mediaService)

}