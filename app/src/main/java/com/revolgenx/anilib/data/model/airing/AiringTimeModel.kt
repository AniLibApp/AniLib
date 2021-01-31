package com.revolgenx.anilib.data.model.airing

import com.revolgenx.anilib.util.CommonTimer

class AiringTimeModel {
    var timeUntilAiring: TimeUntilAiringModel? = null
    var commonTimer: CommonTimer? = null
    var episode:Int? = null
    var airingAt: AiringAtModel?= null
}
