package com.revolgenx.anilib.user.ui.screen.userStats

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.scroll.InitialScroll
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.common.ui.component.button.SegmentedButton
import com.revolgenx.anilib.common.ui.component.chart.ColumnChart
import com.revolgenx.anilib.common.ui.component.chart.LineChart
import com.revolgenx.anilib.common.ui.component.chart.rememberMarker
import com.revolgenx.anilib.common.ui.component.common.Grid
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcBook
import com.revolgenx.anilib.common.ui.icons.appicon.IcBookmark
import com.revolgenx.anilib.common.ui.icons.appicon.IcCalculate
import com.revolgenx.anilib.common.ui.icons.appicon.IcCalendar
import com.revolgenx.anilib.common.ui.icons.appicon.IcHourglass
import com.revolgenx.anilib.common.ui.icons.appicon.IcLibraryBooks
import com.revolgenx.anilib.common.ui.icons.appicon.IcPercent
import com.revolgenx.anilib.common.ui.icons.appicon.IcPlay
import com.revolgenx.anilib.common.ui.icons.appicon.IcTv
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.common.ui.theme.onSurfaceVariant
import com.revolgenx.anilib.common.ui.theme.primary
import com.revolgenx.anilib.list.ui.model.toStringRes
import com.revolgenx.anilib.media.ui.model.isAnime
import com.revolgenx.anilib.media.ui.model.toStringRes
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.user.ui.model.stats.BaseStatisticModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsOverviewViewModel

@Composable
fun UserStatsOverviewScreen(mediaType: MediaType, viewModel: UserStatsOverviewViewModel) {

    LaunchedEffect(viewModel) {
        viewModel.getResource()
    }

    ResourceScreen(
        resourceState = viewModel.resource.value,
        refresh = { viewModel.refresh() }) { user ->
        val stats = user.statistics?.anime ?: user.statistics?.manga ?: return@ResourceScreen

        ProvideChartStyle(m3ChartStyle()) {
            val marker = rememberMarker()

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val isAnime = mediaType.isAnime

                val totalLabel =
                    stringResource(id = if (isAnime) R.string.total_anime else R.string.total_manga)
                val countLabel =
                    stringResource(id = if (isAnime) R.string.episodes_watched else R.string.chapters_read)
                val currentLabel =
                    stringResource(id = if (isAnime) R.string.days_watched else R.string.volume_read)
                val plannedLabel =
                    stringResource(id = if (isAnime) R.string.days_planned else R.string.chapters_planned)
                val meanScoreLabel = stringResource(id = R.string.mean_score)
                val standardDeviationLabel = stringResource(id = R.string.standard_deviation)

                val items = remember {
                    listOf(
                        StatsNumberData(
                            icon = if (isAnime) AppIcons.IcTv else AppIcons.IcLibraryBooks,
                            label = totalLabel,
                            value = stats.count.toString()
                        ),
                        StatsNumberData(
                            icon = if (isAnime) AppIcons.IcPlay else AppIcons.IcBookmark,
                            label = countLabel,
                            value = if (isAnime) stats.episodesWatched.toString() else stats.chaptersRead.toString()
                        ),
                        StatsNumberData(
                            icon = if (isAnime) AppIcons.IcCalendar else AppIcons.IcBook,
                            label = currentLabel,
                            value = if (isAnime) String.format(
                                "%.2f",
                                stats.daysWatched
                            ) else stats.volumesRead.toString()
                        ),
                        StatsNumberData(
                            icon = AppIcons.IcHourglass,
                            label = plannedLabel,
                            value = if (isAnime) String.format(
                                "%.2f",
                                stats.daysPlanned
                            ) else stats.chaptersPlanned.toString()
                        ),
                        StatsNumberData(
                            icon = AppIcons.IcPercent,
                            label = meanScoreLabel,
                            value = stats.meanScore.toString()
                        ),
                        StatsNumberData(
                            icon = AppIcons.IcCalculate,
                            label = standardDeviationLabel,
                            value = stats.standardDeviation.toString()
                        )
                    )
                }
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Grid(items = items) {
                        StatsNumberText(
                            icon = it.icon,
                            label = it.label,
                            value = it.value
                        )
                    }
                }

                HeaderText(text = stringResource(id = R.string.score))
                SegmentedButton(
                    items = stringArrayResource(id = if (isAnime) R.array.anime_stats_score_menu else R.array.manga_stats_score_menu),
                    selectedPosition = viewModel.statsScoreType.intValue
                ) {
                    viewModel.statsScoreType.intValue = it
                }
                when (viewModel.statsScoreType.intValue) {
                    0 -> stats.scoresTitleEntry
                    else -> stats.scoresHourEntry
                }?.let { entryModel ->
                    ColumnChart(
                        marker = marker,
                        model = entryModel
                    )
                }

                HeaderText(text = stringResource(id = if (isAnime) R.string.episode_count else R.string.chapter_count))
                SegmentedButton(
                    items = stringArrayResource(id = if (isAnime) R.array.anime_stats_menu else R.array.manga_stats_score_menu),
                    selectedPosition = viewModel.statsLengthType.intValue
                ) {
                    viewModel.statsLengthType.intValue = it
                }

                when (viewModel.statsLengthType.intValue) {
                    0 -> stats.lengthsTitleEntry
                    1 -> stats.lengthsHourEntry
                    else -> stats.lengthsMeanScoreEntry
                }?.let { entryModel ->
                    val unknown = stringResource(id = R.string.unknown)
                    ColumnChart(
                        marker = marker,
                        model = entryModel,
                        bottomAxis = bottomAxis(
                            valueFormatter = { value, _ ->
                                stats.lengths?.get(value.toInt())?.length ?: unknown
                            }
                        )
                    )
                }

                stats.formats?.let { formatDistribution ->

                    HeaderText(text = stringResource(id = R.string.format_distribution))
                    StatisticModelItemCard(
                        modelDistribution = formatDistribution,
                        isAnime
                    ) {
                        StatisticModelItemHeader(stringResource(id = it.format.toStringRes()))
                    }
                }


                stats.statuses?.let { statusDistribution ->
                    HeaderText(text = stringResource(id = R.string.status_distribution))
                    StatisticModelItemCard(
                        modelDistribution = statusDistribution,
                        isAnime
                    ) {
                        StatisticModelItemHeader(stringResource(id = it.status.toStringRes(mediaType)))
                    }
                }


                stats.countries?.let { countryDistribution ->
                    HeaderText(text = stringResource(id = R.string.country_distribution))
                    StatisticModelItemCard(
                        modelDistribution = countryDistribution,
                        isAnime
                    ) {
                        StatisticModelItemHeader(it.country.naText())
                    }
                }



                HeaderText(text = stringResource(id = R.string.release_year))
                SegmentedButton(
                    items = stringArrayResource(id = if (isAnime) R.array.anime_stats_menu else R.array.manga_stats_score_menu),
                    selectedPosition = viewModel.statsReleaseYearType.intValue
                ) {
                    viewModel.statsReleaseYearType.intValue = it
                }


                when (viewModel.statsReleaseYearType.intValue) {
                    0 -> stats.releaseYearsTitleEntry
                    1 -> stats.releaseYearsHourEntry
                    else -> stats.releaseYearsMeanScoreEntry
                }?.let { entryModel ->
                    LineChart(
                        marker = marker,
                        model = entryModel,
                        spacing = 50.dp,
                        initialScroll = InitialScroll.End
                    )
                }


                HeaderText(text = stringResource(id = if (isAnime) R.string.watch_year else R.string.read_year))
                SegmentedButton(
                    items = stringArrayResource(id = if (isAnime) R.array.anime_stats_menu else R.array.manga_stats_score_menu),
                    selectedPosition = viewModel.statsStartYearType.intValue
                ) {
                    viewModel.statsStartYearType.intValue = it
                }


                when (viewModel.statsStartYearType.intValue) {
                    0 -> stats.startYearsTitleEntry
                    1 -> stats.startYearsHourEntry
                    else -> stats.startYearsMeanScoreEntry
                }?.let { entryModel ->
                    LineChart(
                        marker = marker,
                        model = entryModel,
                        spacing = 50.dp,
                        initialScroll = InitialScroll.End
                    )
                }

            }
        }
    }
}

private data class StatsNumberData(
    val icon: ImageVector,
    val label: String,
    val value: String
)

@Composable
private fun StatsNumberText(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(0.2f)
                .padding(end = 2.dp)
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                imageVector = icon,
                contentDescription = null,
                tint = onSurfaceVariant
            )
        }
        Column(
            modifier = Modifier.weight(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = label,
                color = onSurfaceVariant,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun HeaderText(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.1.sp,
        color = onSurfaceVariant
    )
}

@Composable
private fun <M : BaseStatisticModel> StatisticModelItemCard(
    modelDistribution: List<M>,
    isAnime: Boolean,
    header: @Composable (model: M) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            modelDistribution.forEach { model ->
                StatisticModelItem(
                    model,
                    isAnime
                ) {
                    header(model)
                }
            }
        }
    }
}

@Composable
private fun StatisticModelItem(
    model: BaseStatisticModel,
    isAnime: Boolean,
    header: @Composable () -> Unit
) {
    Column {
        header()
        Text(text = stringResource(id = R.string.count_d).format(model.count))
        Text(
            text = stringResource(id = if (isAnime) R.string.hours_watched_d else R.string.chapters_read_d).format(
                (if (isAnime) model.hoursWatched else model.chaptersRead).orZero()
            )
        )
        Text(text = stringResource(id = R.string.mean_score_d).format(model.meanScore))
    }
}

@Composable
private fun StatisticModelItemHeader(header: String) {
    Text(
        text = header,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.1.sp,
        color = primary
    )
}