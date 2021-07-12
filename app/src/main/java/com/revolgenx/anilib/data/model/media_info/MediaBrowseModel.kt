package com.revolgenx.anilib.data.model.media_info

import com.revolgenx.anilib.data.model.CommonMediaModel
import com.revolgenx.anilib.data.model.airing.AiringTimeModel

class MediaBrowseModel: CommonMediaModel() {
    var mediaListStatus:Int? = null
    var airingTimeModel:AiringTimeModel? = null
}
