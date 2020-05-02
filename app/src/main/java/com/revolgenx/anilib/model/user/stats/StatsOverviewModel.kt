package com.revolgenx.anilib.model.user.stats

import com.revolgenx.anilib.model.BaseUserModel

class StatsOverviewModel : BaseUserModel() {
    var type: Int? = null

    var count: Int? = null
    var scoreFormat: Int? = null

    var meanScore: Double? = null
    var standardDeviation: Double? = null
    var daysPlanned: Double? = null

    var daysWatched: Float? = null
        set(value) {
            field = value?.div(60)?.div(24)
        }

    var episodesWatched: Int? = null

    var chaptersRead: Int? = null
    var volumesRead: Int? = null

    var scoresDistribution: List<StatsOverviewScoreModel>? = null
    var formatDistribution: List<StatsFormatDistributionModel>? = null
    var statusDistribution: List<StatsStatusDistributionModel>? = null
    var countryDistribution: List<StatsCountryDistributionModel>? = null
    var releaseYear: List<StatsYearModel>? = null
    var watchYear: List<StatsYearModel>? = null
}
