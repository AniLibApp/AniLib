package com.revolgenx.anilib.common.data.service

import com.revolgenx.anilib.common.data.field.ToggleFavoriteField
import com.revolgenx.anilib.social.data.field.ToggleLikeV2Field
import com.revolgenx.anilib.social.ui.model.LikeableUnionModel
import kotlinx.coroutines.flow.Flow

interface ToggleService {
    fun toggleLikeV2(field: ToggleLikeV2Field): Flow<LikeableUnionModel?>
    fun toggleFavourite(
        field: ToggleFavoriteField,
    ): Flow<Boolean>
}