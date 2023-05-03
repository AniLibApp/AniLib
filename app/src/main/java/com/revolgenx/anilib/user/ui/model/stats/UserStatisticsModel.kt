package com.revolgenx.anilib.user.ui.model.stats

import com.revolgenx.anilib.fragment.UserMediaStatistics

data class UserStatisticsModel(
    val count: Int = 0,
    val meanScore: Double = 0.0,
    val standardDeviation: Double = 0.0,
    val minutesWatched: Int = 0,

    val episodesWatched: Int = 0,
    val chaptersRead: Int = 0,
    val volumesRead: Int = 0,

    val formats: List<UserFormatStatisticModel>? = null,
    val statuses: List<UserStatusStatisticModel>? = null,
    val scores: List<UserScoreStatisticModel>? = null,
    val releaseYears: List<UserReleaseYearStatisticModel>? = null,
    val lengths: List<UserLengthStatistic>? = null,
    val startYears: List<UserStartYearStatisticModel>? = null,
    val genres: List<UserGenreStatisticModel>? = null,
    val tags: List<UserTagStatisticModel>? = null,
    val countries: List<UserCountryStatisticModel>? = null,
    val voiceActors: List<UserVoiceActorStatisticModel>? = null,
    val staff: List<UserStaffStatisticModel>? = null,
    val studios: List<UserStudioStatisticModel>? = null,

    val daysWatched: Double = if (minutesWatched != 0) minutesWatched.div(60.0).div(24) else 0.0
) {
}


fun UserMediaStatistics.toModel(): UserStatisticsModel {
    return UserStatisticsModel(
        count = count,
        minutesWatched = minutesWatched,
        episodesWatched = episodesWatched,
        meanScore = meanScore,
        genres = genres?.mapNotNull { genre ->
            genre?.let { g ->
                UserGenreStatisticModel(
                    genre = g.genre,
                    count = g.count
                )
            }
        }
    )
}
