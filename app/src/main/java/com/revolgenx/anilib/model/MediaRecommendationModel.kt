package com.revolgenx.anilib.model


open class MediaRecommendationModel : BaseMediaModel() {
    var recommendationId: Int? = null
    var mediaRecommendationId: Int? = null
    var rating: Int? = null
    var userRating: Int? = null
    var title: TitleModel? = null
    var averageScore: Int? = null
    var format: Int? = null
    var type:Int? = null
    var seasonYear: Int? = null
    var episodes: String? = null
    var chapters:String? = null
    var status: Int? = null
    var bannerImage: String? = null
    var coverImage: CoverImageModel? = null
}
