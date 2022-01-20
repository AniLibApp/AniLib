package com.revolgenx.anilib.user.data.model.stats

import com.revolgenx.anilib.media.data.model.MediaModel

class MediaTrendModel {
    var date: Int = 0
    var trending: Int = 0
    var averageScore: Int? = null
    var episode: Int? = null
    var inProgress: Int? = null
    var media:MediaModel? = null
    var mediaId:Int = -1
    var popularity: Int? = null
    var releasing: Boolean = false
}
