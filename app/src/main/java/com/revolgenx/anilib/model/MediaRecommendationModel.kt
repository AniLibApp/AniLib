package com.revolgenx.anilib.model

import com.revolgenx.anilib.type.RecommendationRating

class MediaRecommendationModel :BaseMediaModel(){
    var rating:Int = 0
    var userRating:Int = RecommendationRating.NO_RATING.ordinal
    var title:TitleModel = TitleModel()
    var averageScore:Double = 0.0
}
