package com.revolgenx.anilib.media.data.service

import com.revolgenx.anilib.character.ui.model.CharacterEdgeModel
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.media.data.field.MediaCharacterField
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.media.data.field.MediaOverviewField
import com.revolgenx.anilib.media.data.field.MediaReviewField
import com.revolgenx.anilib.media.data.field.MediaStaffField
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.review.ui.model.ReviewModel
import com.revolgenx.anilib.staff.ui.model.StaffEdgeModel
import kotlinx.coroutines.flow.Flow

interface MediaService {

    fun getMediaList(
        field: MediaField
    ): Flow<PageModel<MediaModel>>

    fun getMediaOverview(
        field: MediaOverviewField
    ): Flow<MediaModel?>

    fun getMediaCharacterList(
        field: MediaCharacterField
    ): Flow<PageModel<CharacterEdgeModel>>

    fun getMediaStaffList(
        field: MediaStaffField
    ): Flow<PageModel<StaffEdgeModel>>

    fun getMediaReviewList(
        field: MediaReviewField
    ): Flow<PageModel<ReviewModel>>

}