package com.revolgenx.anilib.model

class CommonMediaModel() : BaseMediaModel() {
    var title: TitleModel? = null
    var startDate: DateModel? = null
    var genres: List<String>? = null
    var description: String? = null
    var coverImage: CoverImageModel? = null
    var episodes: String? = null
    var bannerImage: String? = null
    var averageScore: Int? = null
    var duration: Int? = null
    var status: Int? = null
    var type: Int? = null
    var isAdult = false
}

//isAdult
//type
//genres
//status
//averageScore
//episodes
//duration
//startDate {
//    ... on FuzzyDate{
//        year
//        month
//        day
//    }
//}
//description(asHtml:true)
//coverImage {
//    ... on MediaCoverImage{
//        medium
//        large
//        extraLarge
//    }
//}
//bannerImage
