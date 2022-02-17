package com.revolgenx.anilib.user.data.model.stats

class UserStatisticsModel {
    var count: Int = 0
    var meanScore: Double = 0.0
    var standardDeviation: Double = 0.0
    var minutesWatched: Int = 0
        set(value) {
            field = value
            daysWatched = value.div(60.0).div(24)
        }

    var episodesWatched: Int = 0
    var chaptersRead: Int = 0
    var volumesRead: Int = 0

    var formats: List<UserFormatStatisticModel>? = null
    var statuses: List<UserStatusStatisticModel>? = null
    var scores: List<UserScoreStatisticModel>? = null
    var releaseYears: List<UserReleaseYearStatisticModel>? = null
    var lengths: List<UserLengthStatistic>? = null
    var startYears: List<UserStartYearStatisticModel>? = null
    var genres: List<UserGenreStatisticModel>? = null
    var tags: List<UserTagStatisticModel>? = null
    var countries: List<UserCountryStatisticModel>? = null
    var voiceActors: List<UserVoiceActorStatisticModel>? = null
    var staff: List<UserStaffStatisticModel>? = null
    var studios: List<UserStudioStatisticModel>? = null

    var daysWatched: Double = 0.0
}