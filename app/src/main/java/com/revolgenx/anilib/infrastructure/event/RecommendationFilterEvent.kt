package com.revolgenx.anilib.infrastructure.event

import com.revolgenx.anilib.data.field.recommendation.RecommendationFilterField

data class RecommendationFilterEvent(var field:RecommendationFilterField):CommonEvent()
