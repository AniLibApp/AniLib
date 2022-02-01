package com.revolgenx.anilib.airing.data.model

import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.util.CommonTimer

class AiringScheduleModel {
    var airingAt: Int = -1
    var airingAtModel: AiringAtModel? = null
    var episode: Int = -1
    var id: Int = -1
    var media: MediaModel? = null
    var mediaId: Int = -1
    var timeUntilAiring: Int = -1
    var timeUntilAiringModel: TimeUntilAiringModel? = null
    var commonTimer: CommonTimer? = null
}