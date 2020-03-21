package com.revolgenx.anilib.model.stats

import com.github.mikephil.charting.data.Entry


class MediaStatsModel {
    var rankings: List<MediaStatsRankingModel>? = null
    var trends: List<MediaStatsTrendsModel>? = null
    var statusDistribution: List<MediaStatsStatusDistributionModel>? = null
    var scoreDistribution: List<MediaStatsScoreDistributionModel>? = null

    var trendsEntry: List<Entry>? = null
}
