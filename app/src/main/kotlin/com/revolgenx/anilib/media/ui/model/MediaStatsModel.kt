package com.revolgenx.anilib.media.ui.model

import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import com.patrykandpatrick.vico.core.marker.Marker
import com.revolgenx.anilib.MediaStatsQuery
import com.revolgenx.anilib.common.ui.model.stats.ScoreDistributionModel
import com.revolgenx.anilib.common.ui.model.stats.StatusDistributionModel
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaRankType
import com.revolgenx.anilib.type.MediaSeason
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


data class MediaStatsModel(
    var rankings: List<MediaRankModel>?,
    var trends: List<MediaTrendModel>?,
    var statusDistribution: List<StatusDistributionModel>?,
    var scoreDistribution: List<ScoreDistributionModel>?,
    var scoreDistributionEntry: ChartEntryModel,
    var trendsEntries: ChartEntryModel,
    var airingTrends: List<AiringTrendsModel>?,
    var airingWatchersProgressionEntries: ChartEntryModel,
    var airingScoreProgressionEntries: ChartEntryModel
)

data class MediaRankModel(
    val id: Int?,
    val rank: Int?,
    val rankType: MediaRankType?,
    val year: Int?,
    val allTime: Boolean,
    val context: String?,
    val season: MediaSeason?,
)

data class MediaTrendModel(
    var date: Int,
    var trending: Int,
    var averageScore: Int? = null,
    var episode: Int? = null,
    var inProgress: Int? = null,
    var media: MediaModel? = null,
    var mediaId: Int = -1,
    var popularity: Int? = null,
    var releasing: Boolean = false
)

data class AiringTrendsModel(
    val episode: Float,
    val averageScore: Float,
    val inProgress: Float
)


fun MediaStatsQuery.Media.toModel(): MediaStatsModel {
    val rankings = rankings?.mapNotNull { rankData ->
        rankData?.let { rank ->
            MediaRankModel(
                id = rank.id,
                context = rank.context,
                allTime = rank.allTime ?: false,
                rank = rank.rank,
                season = rank.season,
                year = rank.year,
                rankType = rank.type,
            )
        }
    }

    val trends = trends?.nodes?.mapNotNull { nodeData ->
        nodeData?.let { node ->
            MediaTrendModel(
                date = node.date,
                trending = node.trending
            )
        }
    }

    val trendsEntries = trends?.map {
        val date = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(it.date.toLong()),
            ZoneId.systemDefault()
        ).dayOfMonth
        entryOf(date, it.trending)
    }

    val statusDistribution = stats?.statusDistribution?.mapNotNull { statusData ->
        statusData?.let { status ->
            StatusDistributionModel(
                amount = status.amount ?: 0,
                status = status.status ?: MediaListStatus.UNKNOWN__
            )
        }
    }

    val scoreDistribution = stats?.scoreDistribution?.mapNotNull { scoreData ->
        scoreData?.let { score ->
            ScoreDistributionModel(
                amount = score.amount ?: 0,
                score = score.score ?: 0
            )
        }
    }?.sortedBy { it.score }

    val scoreDistributionEntry = scoreDistribution?.map {
        entryOf(it.score, it.amount)
    }

    val airingTrends = airingTrends?.nodes?.mapNotNull {
        it?.takeIf { it.episode != null }?.let { trends ->
            AiringTrendsModel(
                episode = trends.episode!!.toFloat(),
                averageScore = trends.averageScore?.toFloat() ?: 0f,
                inProgress = trends.inProgress?.toFloat() ?: 0f
            )
        }
    }?.sortedBy { it.episode }


    val airingWatchersProgressionEntries = mutableListOf<FloatEntry>()
    val airingScoreProgressionEntries = mutableListOf<FloatEntry>()

    airingTrends?.forEach {
        airingWatchersProgressionEntries += entryOf(it.episode, it.inProgress)
        airingScoreProgressionEntries += entryOf(it.episode, it.averageScore)
    }

    return MediaStatsModel(
        rankings = rankings,
        trends = trends,
        statusDistribution = statusDistribution,
        scoreDistribution = scoreDistribution,
        scoreDistributionEntry = entryModelOf(scoreDistributionEntry ?: emptyList()),
        trendsEntries = entryModelOf(trendsEntries ?: emptyList()),
        airingTrends = airingTrends,
        airingWatchersProgressionEntries = entryModelOf(airingWatchersProgressionEntries),
        airingScoreProgressionEntries = entryModelOf(airingScoreProgressionEntries)
    )
}