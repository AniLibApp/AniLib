package com.revolgenx.anilib.media.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.common.ProvideVicoTheme
import com.patrykandpatrick.vico.compose.m3.common.rememberM3VicoTheme
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ext.prettyNumberFormat
import com.revolgenx.anilib.common.ui.component.card.Card
import com.revolgenx.anilib.common.ui.component.chart.ColumnChart
import com.revolgenx.anilib.common.ui.component.chart.LineChart
import com.revolgenx.anilib.common.ui.component.chart.Marker
import com.revolgenx.anilib.common.ui.component.chart.rememberMarker
import com.revolgenx.anilib.common.ui.component.common.Grid
import com.revolgenx.anilib.common.ui.component.common.HeaderBox
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcHeart
import com.revolgenx.anilib.common.ui.icons.appicon.IcStar
import com.revolgenx.anilib.common.ui.model.stats.StatusDistributionModel
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.common.ui.theme.rank_type_popular
import com.revolgenx.anilib.common.ui.theme.rank_type_rated
import com.revolgenx.anilib.list.ui.model.toColor
import com.revolgenx.anilib.list.ui.model.toStringRes
import com.revolgenx.anilib.media.ui.model.MediaStatsModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaStatsViewModel
import com.revolgenx.anilib.type.MediaRankType
import com.revolgenx.anilib.type.MediaType
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import anilib.i18n.R as I18nR

@Composable
fun MediaStatsScreen(viewModel: MediaStatsViewModel, mediaType: MediaType) {
    LaunchedEffect(viewModel) {
        viewModel.getResource()
    }

    val snackbar = localSnackbarHostState()
    val scope = rememberCoroutineScope()

    ResourceScreen(
        viewModel = viewModel
    ) { statsModel ->
        ProvideVicoTheme(rememberM3VicoTheme()) {
            val marker = rememberMarker()
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
            ) {
                StatsRankingSection(statsModel)
                StatsRecentActivityPerDaySection(statsModel, marker)
                StatsAiringScoreProgressionSection(statsModel, marker)
                StatsAiringWatcherProgressionSection(statsModel, marker)
                StatsStatusDistributionSection(statsModel, mediaType, onAmountClick = {
                    scope.launch {
                        snackbar.showSnackbar(it.toString(), withDismissAction = true)
                    }
                })
                StatsScoreDistributionSection(statsModel, marker)
            }
        }
    }
}

@Composable
private fun StatsRankingSection(statsModel: MediaStatsModel) {
    val rankings = statsModel.rankings ?: return
    HeaderBox(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .padding(bottom = 12.dp, top = 8.dp),
        text = stringResource(id = I18nR.string.rankings)
    )
    Grid(items = rankings) { rank ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(3.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp, vertical = 7.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val rankImage =
                    if (rank.rankType == MediaRankType.POPULAR) AppIcons.IcHeart else AppIcons.IcStar
                val rankColor =
                    if (rank.rankType == MediaRankType.RATED) rank_type_popular else rank_type_rated
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = rankImage,
                    contentDescription = null,
                    tint = rankColor
                )
                val seasons = stringArrayResource(id = R.array.media_season)
                val rankText = remember {
                    (rank.rank?.let { "#$it " } ?: "") +
                            (rank.context?.trim()?.split(" ")
                                ?.joinToString(separator = " ") { it.replaceFirstChar { it.uppercase() } }
                                    + " ") +
                            (rank.season?.let { seasons[it.ordinal] + " " } ?: "") +
                            (rank.year ?: "")
                }

                Text(
                    text = rankText,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun StatsScoreDistributionSection(
    statsModel: MediaStatsModel,
    marker: Marker
) {
    val scoreDistributionEntry = statsModel.scoreDistributionEntry ?: return
    MediaStatsHeaderText(text = stringResource(id = I18nR.string.score_distribution))
    OutlinedCard {
        ColumnChart(marker = marker, series = scoreDistributionEntry)
    }
}

@Composable
private fun StatsStatusDistributionSection(
    statsModel: MediaStatsModel,
    mediaType: MediaType,
    onAmountClick: (amount: Int) -> Unit
) {
    val statusDistribution = statsModel.statusDistribution ?: return
    MediaStatsHeaderText(text = stringResource(id = I18nR.string.status_distribution))
    Grid(
        items = statusDistribution,
        columnSpacing = 3.dp,
        rowSpacing = 3.dp
    ) { item ->
        val statusColor =
            item.status.toColor()
        val listStatus = item.status.toStringRes(mediaType)
            .let { str -> stringResource(id = str) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = statusColor)
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .clickable {
                        onAmountClick(item.amount)
                    },
                text = stringResource(id = I18nR.string.status_distribution_string).format(
                    item.amount.prettyNumberFormat(),
                    listStatus
                ),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
    ) {
        val statusTotalAmount = remember {
            statusDistribution.sumOf(StatusDistributionModel::amount).toFloat()
        }
        statusDistribution.forEach { item ->
            val weight = item.amount / statusTotalAmount * 100f
            if (weight > 0) {
                Box(
                    modifier = Modifier
                        .weight(weight)
                        .fillMaxHeight()
                        .background(
                            color = item.status
                                .toColor()
                        )
                )
            }

        }
    }
}

@Composable
private fun StatsAiringWatcherProgressionSection(
    statsModel: MediaStatsModel,
    marker: Marker
) {
    val airingWatchersProgressionEntries = statsModel.airingWatchersProgressionEntries ?: return
    MediaStatsHeaderText(text = stringResource(id = I18nR.string.airing_watchers_progression))
    OutlinedCard {
        LineChart(
            marker = marker,
            series = airingWatchersProgressionEntries,
            initialScroll = Scroll.Absolute.End
        )
    }
}

@Composable
private fun StatsAiringScoreProgressionSection(
    statsModel: MediaStatsModel,
    marker: Marker
) {
    val airingScoreProgressionEntries = statsModel.airingScoreProgressionEntries ?: return
    MediaStatsHeaderText(text = stringResource(id = I18nR.string.airing_score_progression))

    OutlinedCard {
        LineChart(
            marker = marker,
            series = airingScoreProgressionEntries,
            initialScroll = Scroll.Absolute.End
        )
    }
}

@Composable
private fun StatsRecentActivityPerDaySection(
    statsModel: MediaStatsModel,
    marker: Marker
) {
    val trendEntries = statsModel.trendsEntries ?: return
    MediaStatsHeaderText(text = stringResource(id = I18nR.string.recent_activity_per_day))

    OutlinedCard {
        LineChart(
            marker = marker,
            series = trendEntries,
            bottomAxisValueFormatter = { value ->
                LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(value.toLong()),
                    ZoneId.systemDefault()
                ).dayOfMonth.toString()
            }
        )
    }
}

@Composable
private fun MediaStatsHeaderText(text: String) {
    HeaderBox(
        text = text
    )
}


