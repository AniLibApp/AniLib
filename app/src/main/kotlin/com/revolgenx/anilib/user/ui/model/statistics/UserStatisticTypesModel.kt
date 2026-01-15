package com.revolgenx.anilib.user.ui.model.statistics

import com.revolgenx.anilib.common.ext.nullIfEmpty
import com.revolgenx.anilib.fragment.UserStatisticsOverview

data class UserStatisticTypesModel(
    val anime: UserStatisticsModel? = null,
    val manga: UserStatisticsModel? = null,
    val media: UserStatisticsModel? = null
)

fun UserStatisticsOverview.toModel(isAnime: Boolean): UserStatisticsModel {

    val scores = scores?.mapNotNull { scrData ->
        scrData?.let { scr ->
            UserScoreStatisticModel(
                count = scr.count,
                meanScore = scr.meanScore,
                minutesWatched = scr.minutesWatched,
                chaptersRead = scr.chaptersRead,
                score = scr.score ?: 0
            )
        }
    }?.nullIfEmpty()

    val scoresTitleEntry = scores?.map { Pair(it.score, it.count) }
    val scoresHourEntry = scores?.map { Pair(it.score,  if(isAnime) it.hoursWatched else it.chaptersRead) }

    val lengths = lengths?.mapNotNull { length ->
        length?.let { len ->
            UserLengthStatisticModel(
                count = len.count,
                meanScore = len.meanScore,
                minutesWatched = len.minutesWatched,
                chaptersRead = len.chaptersRead,
                length = len.length
            )
        }
    }?.sortedWith(compareBy(nullsLast()) {
        it.length?.let {
            "^(\\d+)".toRegex().find(it)?.value?.toInt()
        }
    })?.nullIfEmpty()
    val lengthsTitleEntry =
        lengths?.mapIndexed { index, it -> Pair(index, it.count) }
    val lengthsHourEntry =
        lengths?.mapIndexed { index, it -> Pair(index, if(isAnime) it.hoursWatched else it.chaptersRead) }
    val lengthsMeanScoreEntry =
        lengths?.mapIndexed { index, it -> Pair(index, it.meanScore) }

    val releaseYears = releaseYears?.mapNotNull { yrData ->
        yrData?.let { yr ->
            UserReleaseYearStatisticModel(
                count = yr.count,
                meanScore = yr.meanScore,
                minutesWatched = yr.minutesWatched,
                chaptersRead = yr.chaptersRead,
                releaseYear = yr.releaseYear ?: 0
            )
        }
    }?.sortedWith(compareBy { it.releaseYear })?.nullIfEmpty()

    val releaseYearsTitleEntry =
        releaseYears?.map { Pair(it.releaseYear, it.count) }
    val releaseYearsHourEntry =
        releaseYears?.map { Pair(it.releaseYear, if(isAnime) it.hoursWatched else it.chaptersRead) }
    val releaseYearsMeanScoreEntry =
        releaseYears?.map { Pair(it.releaseYear, it.meanScore) }

    val startYears = startYears?.mapNotNull { yrData ->
        yrData?.let { yr ->
            UserStartYearStatisticModel(
                count = yr.count,
                meanScore = yr.meanScore,
                minutesWatched = yr.minutesWatched,
                chaptersRead = yr.chaptersRead,
                startYear = yr.startYear ?: 0
            )
        }
    }?.sortedWith(compareBy { it.startYear })?.nullIfEmpty()

    val startYearsTitleEntry =
        startYears?.map { Pair(it.startYear, it.count) }
    val startYearsHourEntry =
        startYears?.map { Pair(it.startYear, if(isAnime) it.hoursWatched else it.chaptersRead) }
    val startYearsMeanScoreEntry =
        startYears?.map { Pair(it.startYear, it.meanScore) }

    return UserStatisticsModel(
        count = count,
        meanScore = meanScore,
        standardDeviation = standardDeviation,
        minutesWatched = minutesWatched,
        episodesWatched = episodesWatched,
        chaptersRead = chaptersRead,
        volumesRead = volumesRead,
        scores = scores,
        scoresTitleEntry = scoresTitleEntry,
        scoresHourEntry = scoresHourEntry,

        statuses = statuses?.mapNotNull { statData ->
            statData?.let { stat ->
                UserStatusStatisticModel(
                    count = stat.count,
                    meanScore = stat.meanScore,
                    minutesWatched = stat.minutesWatched,
                    chaptersRead = stat.chaptersRead,
                    status = stat.status
                )
            }
        }?.nullIfEmpty(),

        lengths = lengths,
        lengthsTitleEntry = lengthsTitleEntry,
        lengthsHourEntry = lengthsHourEntry,
        lengthsMeanScoreEntry = lengthsMeanScoreEntry,

        releaseYears = releaseYears,
        releaseYearsTitleEntry = releaseYearsTitleEntry,
        releaseYearsHourEntry = releaseYearsHourEntry,
        releaseYearsMeanScoreEntry = releaseYearsMeanScoreEntry,

        startYears = startYears,
        startYearsTitleEntry = startYearsTitleEntry,
        startYearsHourEntry = startYearsHourEntry,
        startYearsMeanScoreEntry = startYearsMeanScoreEntry,

        formats = formats?.mapNotNull { fmtData ->
            fmtData?.let { fmt ->
                UserFormatStatisticModel(
                    count = fmt.count,
                    meanScore = fmt.meanScore,
                    minutesWatched = fmt.minutesWatched,
                    chaptersRead = fmt.chaptersRead,
                    format = fmt.format
                )
            }
        }?.nullIfEmpty(),
        countries = countries?.mapNotNull { cntData ->
            cntData?.let { cnt ->
                UserCountryStatisticModel(
                    count = cnt.count,
                    meanScore = cnt.meanScore,
                    minutesWatched = cnt.minutesWatched,
                    chaptersRead = cnt.chaptersRead,
                    country = cnt.country?.toString()
                )
            }
        }?.nullIfEmpty()
    )
}