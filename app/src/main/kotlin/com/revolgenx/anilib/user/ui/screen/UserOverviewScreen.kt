package com.revolgenx.anilib.user.ui.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import com.revolgenx.anilib.common.ui.component.card.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ui.component.common.Grid
import com.revolgenx.anilib.common.ui.component.common.HeaderBox
import com.revolgenx.anilib.common.ui.component.scroll.ScrollBarConfig
import com.revolgenx.anilib.common.ui.component.scroll.verticalScrollWithScrollbar
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.component.text.MarkdownText
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.user.ui.model.UserModel
import com.revolgenx.anilib.user.ui.model.stats.UserActivityHistoryModel
import com.revolgenx.anilib.user.ui.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import anilib.i18n.R as I18nR

@Composable
fun UserOverviewScreen(viewModel: UserViewModel) {
    val scope = rememberCoroutineScope()
    val context = localContext()

    ResourceScreen(viewModel = viewModel) { userModel ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
        ) {
            UserDescriptionSection(userModel)
            UserActivityHistorySection(userModel, scope)
            UserStatisticSection(userModel, context)
            UserGenreOverviewSection(userModel)
        }
    }
}

@Composable
private fun UserGenreOverviewSection(userModel: UserModel) {
    userModel.statistics?.media?.genres?.let {
        UserOverviewHeader(stringResource(id = I18nR.string.genre_overview))

        Card {
            LazyRow {
                items(it) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        MediumText(
                            text = it.genre.orEmpty(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                        )
                        MediumText(
                            text = it.count.toString(),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserStatisticSection(
    userModel: UserModel,
    context: Context
) {
    userModel.statistics?.anime?.let { stats ->
        Spacer(modifier = Modifier.size(18.dp))
        OutlinedCard {
            val statsItems = remember {
                listOf(
                    UserStatisticsModel(
                        stats.count.toString(),
                        context.getString(I18nR.string.total_anime)
                    ),
                    UserStatisticsModel(
                        stats.episodesWatched.toString(),
                        context.getString(I18nR.string.episodes_watched)
                    ),
                    UserStatisticsModel(
                        "%.1f".format(stats.daysWatched),
                        context.getString(I18nR.string.days_watched)
                    ),
                    UserStatisticsModel(
                        stats.meanScore.toString(),
                        context.getString(I18nR.string.mean_score)
                    ),
                )
            }

            UserStatisticGridItem(statsItems)
        }
    }

    userModel.statistics?.manga?.let { stats ->
        Spacer(modifier = Modifier.size(18.dp))
        OutlinedCard {
            val statsItems = remember {
                listOf(
                    UserStatisticsModel(
                        stats.count.toString(),
                        context.getString(I18nR.string.total_manga)
                    ),
                    UserStatisticsModel(
                        stats.volumesRead.toString(),
                        context.getString(I18nR.string.volume_read)
                    ),
                    UserStatisticsModel(
                        stats.chaptersRead.toString(),
                        context.getString(I18nR.string.chapters_read)
                    ),
                    UserStatisticsModel(
                        stats.meanScore.toString(),
                        context.getString(I18nR.string.mean_score)
                    ),
                )
            }

            UserStatisticGridItem(statsItems)
        }
    }
}

@Composable
private fun UserStatisticGridItem(statsItems: List<UserStatisticsModel>) {
    Grid(
        modifier = Modifier.padding(8.dp),
        items = statsItems,
        columnSpacing = 8.dp,
        rowSpacing = 8.dp
    ) {
        UserStatisticsTextItem(
            title = it.title,
            subtitle = it.subtitle
        )
    }
}

data class UserStatisticsModel(
    val title: String,
    val subtitle: String
)

@Composable
private fun UserStatisticsTextItem(
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        MediumText(
            text = title,
            maxLines = 1,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            lineHeight = 20.sp,
        )
        MediumText(
            text = subtitle,
            fontSize = 15.sp,
            lineHeight = 19.sp,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun UserActivityHistorySection(userModel: UserModel, scope: CoroutineScope) {
    userModel.stats?.activityHistory?.let { activityHistory ->
        UserOverviewHeader(stringResource(id = I18nR.string.activity_history))

        OutlinedCard {
            LazyHorizontalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(146.dp),
                rows = GridCells.Fixed(7),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(items = activityHistory) { activityHistory ->
                    UserActivityHistoryToolTip(activityHistory, scope) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(
                                    color = if (activityHistory.alpha != 0f) {
                                        MaterialTheme.colorScheme.primary.copy(activityHistory.alpha)
                                    } else {
                                        MaterialTheme.colorScheme.onSurface.copy(0.1f)
                                    },
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
private fun UserActivityHistoryToolTip(
    activityHistory: UserActivityHistoryModel,
    scope: CoroutineScope,
    content: @Composable () -> Unit
) {
    if (activityHistory.level == 0) {
        content()
    } else {
        val tooltipState = rememberTooltipState(isPersistent = true)
        TooltipBox(
            positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
            tooltip = {
                RichTooltip(
                    title = {
                        MediumText(
                            text = activityHistory.date.formattedDate,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 15.sp
                        )
                    },
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(
                                        activityHistory.alpha
                                    ),
                                    shape = CircleShape
                                )
                        )
                        MediumText(
                            text = stringResource(id = I18nR.string.amount).format(
                                activityHistory.amount
                            ),
                            fontSize = 15.sp
                        )
                    }
                }
            },
            state = tooltipState
        ) {
            Box(
                modifier = Modifier
                    .clickable {
                    scope.launch {
                        tooltipState.show()
                    }
                }
            ) {
                content()
            }
        }
    }
}


@Composable
private fun UserOverviewHeader(text: String) {
    HeaderBox(text = text)
}


/*todo: add scroll handler*/
@Composable
private fun UserDescriptionSection(userModel: UserModel) {
    val showUserDescription = remember { mutableStateOf(false) }
    val hasDescription = userModel.about.isNullOrBlank().not()
    if (hasDescription && showUserDescription.value.not()) {
        Button(onClick = { showUserDescription.value = true }) {
            Text(
                text = stringResource(id = I18nR.string.show_description)
            )
        }
    } else {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.onSurface)
        ) {
            Column(
                modifier = Modifier
                    .heightIn(max = 600.dp)
                    .verticalScrollWithScrollbar(
                        state = rememberScrollState(),
                        scrollbarConfig = remember {
                            ScrollBarConfig()
                        }
                    )
                    .padding(8.dp)
            ) {
                val noDescriptionText =
                    stringResource(id = I18nR.string.no_description).takeIf { !hasDescription }

                MarkdownText(
                    text = noDescriptionText,
                    spanned = userModel.aboutSpanned
                )
            }
        }
    }
}