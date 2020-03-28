package com.revolgenx.anilib.model

open class CommonMediaModel() : BaseModel() {
    var mediaId: Int?= null
    var title: TitleModel? = null
    var startDate: DateModel? = null
    var endDate: DateModel? = null
    var genres: List<String>? = null
    var coverImage: CoverImageModel? = null
    var episodes: String? = null
    var chapters: String? = null
    var volumes: String? = null     /*can be null*/
    var bannerImage: String? = null
    var averageScore: Int? = null /*can be null*/
    var duration: String? = null
    var status: Int? = null
    var format: Int? = null
    var type: Int? = null
    var isAdult = false
}
