package com.revolgenx.anilib.user.data.model.stats

import com.revolgenx.anilib.user.data.model.UserModel

class StatsOverviewModel : UserModel() {
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

    var scoresDistribution: List<UserScoreStatisticModel>? = null
    var formatDistribution: List<UserFormatStatisticModel>? = null
    var statusDistribution: List<UserStatusStatisticModel>? = null
    var countryDistribution: List<UserCountryStatisticModel>? = null
    var releaseYear: List<UserReleaseYearStatisticModel>? = null
    var startYears: List<UserReleaseYearStatisticModel>? = null
}
