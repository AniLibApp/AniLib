package com.revolgenx.anilib.common.data.service

import com.revolgenx.anilib.common.data.field.ToggleFavoriteField
import kotlinx.coroutines.flow.Flow

interface ToggleService {
    fun toggleFavourite(
        field: ToggleFavoriteField,
    ): Flow<Boolean>
}