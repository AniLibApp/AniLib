package com.revolgenx.anilib.media.data.model

import com.revolgenx.anilib.common.data.model.stats.ScoreDistributionModel
import com.revolgenx.anilib.common.data.model.stats.StatusDistributionModel

class MediaStatsModel {
    var statusDistribution: List<StatusDistributionModel>? = null
    var scoreDistribution: List<ScoreDistributionModel>? = null
}