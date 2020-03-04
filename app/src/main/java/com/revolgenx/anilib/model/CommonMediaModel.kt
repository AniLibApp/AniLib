package com.revolgenx.anilib.model

class CommonMediaModel() : BaseMediaModel() {
    var title: TitleModel? = null
    var startDate: DateModel? = null
    var endDate: DateModel? = null
    var genres: List<String>? = null
    var description: String? = null
    var coverImage: CoverImageModel? = null
    var episodes: String? = null
    var bannerImage: String? = null
    var averageScore: Int? = null
    var duration: String? = null
    var status: Int? = null
    var format:Int? = null
    var isAdult = false
}
