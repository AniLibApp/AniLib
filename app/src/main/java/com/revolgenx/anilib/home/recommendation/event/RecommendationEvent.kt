package com.revolgenx.anilib.home.recommendation.event

import com.revolgenx.anilib.common.event.BaseEvent

sealed class RecommendationEvent: BaseEvent(){
    data class RecommendationFilterEvent(val onList:Boolean?, val sort:Int): RecommendationEvent()
}