package com.revolgenx.anilib.common.data.model

import com.revolgenx.anilib.entry.data.model.MediaEntryListModel
import com.revolgenx.anilib.media.data.model.MediaCoverImageModel
import com.revolgenx.anilib.media.data.model.MediaTitleModel
import com.revolgenx.anilib.studio.data.model.MediaStudioModel

open class CommonMediaModel() : BaseModel() {
    var mediaId: Int? = null
        set(value) {
            field = value
            id = value?:-1
        }
    var title: MediaTitleModel? = null
    var description: String? = null
    var startDate: FuzzyDateModel? = null
    var endDate: FuzzyDateModel? = null
    var genres: List<String>? = null
    var coverImage: MediaCoverImageModel? = null
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
    var source: Int? = null
    var synonyms: List<String>? = null
    var siteUrl:String? = null
}
