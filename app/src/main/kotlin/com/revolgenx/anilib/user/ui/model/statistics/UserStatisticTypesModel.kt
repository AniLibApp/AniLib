package com.revolgenx.anilib.user.ui.model.statistics

import com.patrykandpatrick.vico.core.entry.entryOf
import com.revolgenx.anilib.common.ext.getOrEmpty
import com.revolgenx.anilib.fragment.UserStatisticsOverview
import com.patrykandpatrick.vico.core.entry.entryModelOf


data class UserStatisticTypesModel(
    val anime: UserStatisticsModel? = null,
    val manga: UserStatisticsModel? = null
)

fun UserStatisticsOverview.toModel(): UserStatisticsModel {

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
    }
    val scoresTitleEntry = scores?.map { entryOf(it.score, it.count) }.getOrEmpty()
    val scoresHourEntry = scores?.map { entryOf(it.score, it.hoursWatched) }.getOrEmpty()

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
    })
    val lengthsTitleEntry =
        lengths?.mapIndexed { index, it -> entryOf(index, it.count) }.getOrEmpty()
    val lengthsHourEntry =
        lengths?.mapIndexed { index, it -> entryOf(index, it.hoursWatched) }.getOrEmpty()
    val lengthsMeanScoreEntry =
        lengths?.mapIndexed { index, it -> entryOf(index, it.meanScore) }.getOrEmpty()

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
    }?.sortedWith(compareBy { it.releaseYear })

    val releaseYearsTitleEntry =
        releaseYears?.map { entryOf(it.releaseYear, it.count) }.getOrEmpty()
    val releaseYearsHourEntry =
        releaseYears?.map { entryOf(it.releaseYear, it.hoursWatched) }.getOrEmpty()
    val releaseYearsMeanScoreEntry =
        releaseYears?.map { entryOf(it.releaseYear, it.meanScore) }.getOrEmpty()

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
    }?.sortedWith(compareBy { it.startYear })

    val startYearsTitleEntry =
        startYears?.map { entryOf(it.startYear, it.count) }.getOrEmpty()
    val startYearsHourEntry =
        startYears?.map { entryOf(it.startYear, it.hoursWatched) }.getOrEmpty()
    val startYearsMeanScoreEntry =
        startYears?.map { entryOf(it.startYear, it.meanScore) }.getOrEmpty()

    return UserStatisticsModel(
        count = count,
        meanScore = meanScore,
        standardDeviation = standardDeviation,
        minutesWatched = minutesWatched,
        episodesWatched = episodesWatched,
        chaptersRead = chaptersRead,
        volumesRead = volumesRead,
        scores = scores,
        scoresTitleEntry = entryModelOf(scoresTitleEntry),
        scoresHourEntry = entryModelOf(scoresHourEntry),

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
        },

        lengths = lengths,
        lengthsTitleEntry = entryModelOf(lengthsTitleEntry),
        lengthsHourEntry = entryModelOf(lengthsHourEntry),
        lengthsMeanScoreEntry = entryModelOf(lengthsMeanScoreEntry),

        releaseYears = releaseYears,
        releaseYearsTitleEntry = entryModelOf(releaseYearsTitleEntry),
        releaseYearsHourEntry = entryModelOf(releaseYearsHourEntry),
        releaseYearsMeanScoreEntry = entryModelOf(releaseYearsMeanScoreEntry),

        startYears = startYears,
        startYearsTitleEntry = entryModelOf(startYearsTitleEntry),
        startYearsHourEntry = entryModelOf(startYearsHourEntry),
        startYearsMeanScoreEntry = entryModelOf(startYearsMeanScoreEntry),

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
        },
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
        }
    )
}