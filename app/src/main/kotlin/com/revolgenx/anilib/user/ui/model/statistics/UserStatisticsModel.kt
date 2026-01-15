package com.revolgenx.anilib.user.ui.model.statistics

import com.revolgenx.anilib.common.ext.nullIfEmpty
import com.revolgenx.anilib.common.ui.component.chart.ChartEntryModel
import com.revolgenx.anilib.fragment.UserMediaStatistics
import com.revolgenx.anilib.type.MediaListStatus

data class UserStatisticsModel(
    val count: Int = 0,
    val meanScore: Double = 0.0,
    val standardDeviation: Double = 0.0,
    val minutesWatched: Int = 0,

    val episodesWatched: Int = 0,
    val chaptersRead: Int = 0,
    val volumesRead: Int = 0,

    val scores: List<UserScoreStatisticModel>? = null,
    val scoresTitleEntry: List<Pair<Number, Number>>? = null,
    val scoresHourEntry: ChartEntryModel? = null,

    val lengths: List<UserLengthStatisticModel>? = null,
    val lengthsTitleEntry: ChartEntryModel? = null,
    val lengthsHourEntry: ChartEntryModel? = null,
    val lengthsMeanScoreEntry: ChartEntryModel? = null,

    val releaseYears: List<UserReleaseYearStatisticModel>? = null,
    val releaseYearsTitleEntry: ChartEntryModel? = null,
    val releaseYearsHourEntry: ChartEntryModel? = null,
    val releaseYearsMeanScoreEntry: ChartEntryModel? = null,

    val startYears: List<UserStartYearStatisticModel>? = null,
    val startYearsTitleEntry: ChartEntryModel? = null,
    val startYearsHourEntry: ChartEntryModel? = null,
    val startYearsMeanScoreEntry: ChartEntryModel? = null,

    val formats: List<UserFormatStatisticModel>? = null,
    val statuses: List<UserStatusStatisticModel>? = null,

    val genres: List<UserGenreStatisticModel>? = null,
    val tags: List<UserTagStatisticModel>? = null,
    val countries: List<UserCountryStatisticModel>? = null,
    val voiceActors: List<UserVoiceActorStatisticModel>? = null,
    val staff: List<UserStaffStatisticModel>? = null,
    val studios: List<UserStudioStatisticModel>? = null,

    val daysWatched: Double = if (minutesWatched != 0) minutesWatched.div(60.0).div(24) else 0.0,
    val daysPlanned: Double = statuses?.firstOrNull { it.status == MediaListStatus.PLANNING }?.hoursWatched?.div(
        24.0
    ) ?: 0.0,
    val chaptersPlanned: Int = statuses?.firstOrNull { it.status == MediaListStatus.PLANNING }?.chaptersRead ?: 0
) {
}


fun UserMediaStatistics.toModel(): UserStatisticsModel {
    return UserStatisticsModel(
        count = count,
        minutesWatched = minutesWatched,
        episodesWatched = episodesWatched,
        chaptersRead = chaptersRead,
        volumesRead = volumesRead,
        meanScore = meanScore,
        genres = genres?.mapNotNull { genre ->
            genre?.let { g ->
                UserGenreStatisticModel(
                    genre = g.genre,
                    count = g.count
                )
            }
        }?.nullIfEmpty()
    )
}
