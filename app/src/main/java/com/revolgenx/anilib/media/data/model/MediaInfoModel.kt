package com.revolgenx.anilib.media.data.model

import com.revolgenx.anilib.common.data.model.CommonMediaModel
import com.revolgenx.anilib.airing.data.model.AiringTimeModel

class MediaInfoModel: CommonMediaModel() {
    var mediaListStatus:Int? = null
    var airingTimeModel: AiringTimeModel? = null
}

