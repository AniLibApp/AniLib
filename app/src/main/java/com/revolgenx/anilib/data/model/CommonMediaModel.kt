package com.revolgenx.anilib.data.model

import com.revolgenx.anilib.data.model.entry.MediaEntryListModel

open class CommonMediaModel() : BaseModel() {
    var mediaId: Int? = null
        set(value) {
            field = value
            baseId = value
        }
    var title: TitleModel? = null
    var description: String? = null
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
    var season: Int? = null
    var seasonYear: Int? = null
    var popularity:Int? = null
    var favourites:Int? = null
    var mediaEntryListModel: MediaEntryListModel? = null
    var studios: List<MediaStudioModel>? = null        /*empty in manga*/
    var staff:List<MediaStaffModel>? = null
    var source: Int? = null
    var synonyms: List<String>? = null
    var siteUrl:String? = null
}
