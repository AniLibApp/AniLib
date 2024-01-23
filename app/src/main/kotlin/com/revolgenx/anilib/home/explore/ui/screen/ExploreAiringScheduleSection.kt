package com.revolgenx.anilib.home.explore.ui.screen

import IcAiring
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.airing.ui.model.AiringScheduleModel
import com.revolgenx.anilib.airing.ui.screen.AiringScheduleFilterBottomSheet
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleViewModel
import com.revolgenx.anilib.common.ext.airingScheduleScreen
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClickWithValue
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreAiringScheduleFilterViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreAiringScheduleViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreAiringViewModel
import com.revolgenx.anilib.media.ui.component.CoverMediaCard
import com.revolgenx.anilib.media.ui.component.MediaComponentState
import com.revolgenx.anilib.media.ui.component.rememberMediaComponentState
import com.revolgenx.anilib.media.ui.model.toStringRes
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

@Composable
fun ExploreAiringScheduleSection() {
    val viewModel: ExploreAiringScheduleViewModel = koinViewModel()
    val filterViewModel: ExploreAiringScheduleFilterViewModel = koinViewModel()
    val exploreAiringViewModel: ExploreAiringViewModel = koinViewModel()

    val filterBottomSheetState = rememberBottomSheetState()

    val scope = rememberCoroutineScope()
    val navigator = localNavigator()
    val context = localContext()

    Column {
        ExploreScreenHeader(
            text = stringResource(id = I18nR.string.airing),
            icon = AppIcons.IcAiring,
            onFilter = {
                scope.launch {
                    filterBottomSheetState.expand()
                }
            },
            onMore = {
                navigator.airingScheduleScreen()
            }
        )
        ExploreAiringHeader(exploreAiringViewModel) { startDate ->
            viewModel.updateStartDate(startDate)
        }
        ExploreAiringScheduleContent(
            context = context,
            navigator = navigator,
            viewModel = viewModel
        )
    }

    AiringScheduleFilterBottomSheet(
        bottomSheetState = filterBottomSheetState,
        viewModel = filterViewModel
    )
}

@Composable
private fun ExploreAiringScheduleContent(
    context: Context,
    navigator: Navigator,
    viewModel: AiringScheduleViewModel
) {
    val pagingItems = viewModel.collectAsLazyPagingItems()
    val mediaComponentState = rememberMediaComponentState(navigator = navigator, context = context)

    Box(
        modifier = Modifier.height(ExploreMediaCardHeight)
    ) {
        LazyPagingList(
            pagingItems = pagingItems,
            type = ListPagingListType.ROW,
            onRefresh = {
                viewModel.refresh()
            }
        ) { model ->
            if (model == null || model !is AiringScheduleModel) return@LazyPagingList
            AiringScheduleItem(
                airingScheduleModel = model,
                mediaComponentState = mediaComponentState,
                context = context
            )
        }
    }
}


@Composable
private fun AiringScheduleItem(
    airingScheduleModel: AiringScheduleModel,
    mediaComponentState: MediaComponentState,
    context: Context
) {
    val media = airingScheduleModel.media ?: return
    CoverMediaCard(
        media = media,
        mediaComponentState = mediaComponentState,
        width = ExploreMediaCardWidth,
        height = ExploreMediaCardHeight,
        footerContent = {
            val progressBehind = media.mediaListEntry?.progress?.let { progress ->
                airingScheduleModel.lastEpisode?.let { lastEpisode ->
                    lastEpisode - progress
                }
            }?.takeIf { it > 0 }

            if (progressBehind != null) {
                MediumText(
                    text = pluralStringResource(
                        id = I18nR.plurals.s_episodes_behind,
                        progressBehind
                    ).format(progressBehind),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            } else {
                media.genres?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        it.take(3).forEach {
                            MediumText(
                                text = it,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            val format = stringResource(id = media.format.toStringRes())
            LightText(
                text = stringResource(id = I18nR.string.s_dot_s).format(
                    format,
                    media.episodes.naText()
                ),
                lineHeight = 11.sp
            )
            Spacer(Modifier.size(3.dp))
            AiringScheduleTimer(airingScheduleModel = airingScheduleModel, context = context)
        }
    )
}

@Composable
private fun AiringScheduleTimer(
    airingScheduleModel: AiringScheduleModel,
    context: Context,
) {
    val airingScheduleTimer = airingScheduleModel.airingScheduleTimer!!
    val airingAtModel = airingScheduleModel.airingAtModel
    val episode = airingScheduleModel.episode

    DisposableEffect(airingScheduleTimer) {
        airingScheduleTimer.start()
        onDispose {
            airingScheduleTimer.stop()
        }
    }
    val scheduleTimeText = if (airingScheduleTimer.timeUntilAiringModel.alreadyAired) {
        airingAtModel.airedAt
    } else {
        airingScheduleTimer.timeLeft.value.formatString(context)
    }
    MediumText(
        text = stringResource(id = I18nR.string.ep_s_s).format(episode, scheduleTimeText),
        color = MaterialTheme.colorScheme.primary,
        fontSize = 12.sp
    )
}


@Composable
private fun ExploreAiringHeader(
    exploreAiringViewModel: ExploreAiringViewModel,
    onDaySelected: OnClickWithValue<Long>
) {
    val weekDays = exploreAiringViewModel.weekDaysFromToday.value
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        weekDays.forEach { (day, startDay) ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        exploreAiringViewModel.selectedDay.value = day
                        onDaySelected(startDay)
                    }
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = day,
                    color = if (day == exploreAiringViewModel.selectedDay.value) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }
    }
}