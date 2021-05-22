package com.revolgenx.anilib.infrastructure.event

sealed class SeasonEvent:BaseEvent() {
    object SeasonFilterEvent : SeasonEvent()
    object SeasonChangeEvent : SeasonEvent()
    object SeasonTagEvent : SeasonEvent()
    object SeasonGenreEvent : SeasonEvent()
    data class SeasonHeaderEvent(val showHeader:Boolean) : SeasonEvent()
}

sealed class RecommendationEvent:BaseEvent(){
    data class RecommendationFilterEvent(val onList:Boolean?, val sort:Int):RecommendationEvent()
}