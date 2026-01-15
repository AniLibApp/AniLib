package com.revolgenx.anilib.media.ui.model

import com.revolgenx.anilib.MediaStatsQuery
import com.revolgenx.anilib.common.ext.nullIfEmpty
import com.revolgenx.anilib.common.ui.component.chart.ChartEntryModel
import com.revolgenx.anilib.common.ui.model.stats.ScoreDistributionModel
import com.revolgenx.anilib.common.ui.model.stats.StatusDistributionModel
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaRankType
import com.revolgenx.anilib.type.MediaSeason


class MediaStatsModel(
    val rankings: List<MediaRankModel>?,
    val trends: List<MediaTrendModel>?,
    val statusDistribution: List<StatusDistributionModel>?,
    val scoreDistribution: List<ScoreDistributionModel>?,
    val scoreDistributionEntry: ChartEntryModel?,
    val trendsEntries: ChartEntryModel?,
    val airingTrends: List<AiringTrendsModel>?,
    val airingWatchersProgressionEntries: ChartEntryModel?,
    val airingScoreProgressionEntries: ChartEntryModel?
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
    }?.nullIfEmpty()


    val trends = trends?.nodes?.mapNotNull { nodeData ->
        nodeData?.let { node ->
            MediaTrendModel(
                date = node.date,
                trending = node.trending
            )
        }
    }?.nullIfEmpty()

    val trendsEntries = trends?.map {
        Pair(it.date, it.trending)
    }

    val statusDistribution = stats?.statusDistribution?.mapNotNull { statusData ->
        statusData?.let { status ->
            StatusDistributionModel(
                amount = status.amount ?: 0,
                status = status.status ?: MediaListStatus.UNKNOWN__
            )
        }
    }?.nullIfEmpty()

    val scoreDistribution = stats?.scoreDistribution?.mapNotNull { scoreData ->
        scoreData?.let { score ->
            ScoreDistributionModel(
                amount = score.amount ?: 0,
                score = score.score ?: 0
            )
        }
    }?.sortedBy { it.score }?.nullIfEmpty()

    val scoreDistributionEntry = scoreDistribution?.map {
        Pair(it.score, it.amount)
    }

    var airingWatchersProgressionEntries: List<Pair<Float, Float>>? = null
    var airingScoreProgressionEntries: List<Pair<Float, Float>>? = null

    val airingTrends = airingTrends?.nodes?.mapNotNull {
        it?.takeIf { it.episode != null }?.let { trends ->
            AiringTrendsModel(
                episode = trends.episode!!.toFloat(),
                averageScore = trends.averageScore?.toFloat() ?: 0f,
                inProgress = trends.inProgress?.toFloat() ?: 0f
            )
        }
    }?.sortedBy { it.episode }?.nullIfEmpty()

    airingTrends?.let { airingTrendsModels ->
        airingWatchersProgressionEntries = mutableListOf()
        airingScoreProgressionEntries = mutableListOf()
        airingTrendsModels.forEach {
            airingWatchersProgressionEntries.add(Pair(it.episode, it.inProgress))
            airingScoreProgressionEntries.add(Pair(it.episode, it.averageScore))
        }
    }

    return MediaStatsModel(
        rankings = rankings,
        trends = trends,
        statusDistribution = statusDistribution,
        scoreDistribution = scoreDistribution,
        scoreDistributionEntry = scoreDistributionEntry,
        trendsEntries = trendsEntries,
        airingTrends = airingTrends,
        airingWatchersProgressionEntries = airingWatchersProgressionEntries,
        airingScoreProgressionEntries = airingScoreProgressionEntries
    )
}