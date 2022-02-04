package com.revolgenx.anilib.user.data.model.stats

import com.github.mikephil.charting.data.Entry
import com.revolgenx.anilib.common.data.model.stats.ScoreDistributionModel
import com.revolgenx.anilib.common.data.model.stats.StatusDistributionModel


class MediaStatsModel {
    var rankings: List<MediaRankModel>? = null
    var trends: List<MediaTrendModel>? = null
    var statusDistribution: List<StatusDistributionModel>? = null
    var scoreDistribution: List<ScoreDistributionModel>? = null
    var trendsEntries: List<Entry>? = null
    var airingTrends: List<AiringTrendsModel>? = null
    var airingWatchersProgressionEntries: List<Entry>? = null
    var airingScoreProgressionEntries: List<Entry>? = null
}



