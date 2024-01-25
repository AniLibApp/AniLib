package com.revolgenx.anilib.home.recommendation.ui.model

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.RecommendationQuery
import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.fragment.RecommendationFragment
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.type.RecommendationRating

data class RecommendationModel(
    val id: Int,
    val rating: MutableIntState,
    /**
     * RecommendationRating
     * */
    val userRating: MutableState<RecommendationRating?>,
    // The media the recommendation is from
    val media: MediaModel?,
    // The recommended media
    val mediaRecommendation: MediaModel?
) : BaseModel


fun RecommendationQuery.Recommendation.toModel(): RecommendationModel {
    return recommendationFragment.toModel().copy(media = media?.media?.toModel())
}

fun RecommendationFragment.toModel(): RecommendationModel {
    return RecommendationModel(
        id = id,
        rating = mutableIntStateOf(rating.orZero()),
        userRating = mutableStateOf(userRating),
        media = null,
        mediaRecommendation = mediaRecommendation?.media?.toModel()
    )
}