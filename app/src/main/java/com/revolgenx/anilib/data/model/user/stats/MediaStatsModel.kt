package com.revolgenx.anilib.data.model.user.stats

import com.github.mikephil.charting.data.Entry


class MediaStatsModel {
    var rankings: List<MediaStatsRankingModel>? = null
    var trends: List<MediaStatsTrendsModel>? = null
    var statusDistribution: List<MediaStatsStatusDistributionModel>? = null
    var scoreDistribution: List<MediaStatsScoreDistributionModel>? = null
    var trendsEntries: List<Entry>? = null
    var airingTrends: List<AiringTrendsModel>? = null
    var airingWatchersProgressionEntries: List<Entry>? = null
    var airingScoreProgressionEntries: List<Entry>? = null
}



