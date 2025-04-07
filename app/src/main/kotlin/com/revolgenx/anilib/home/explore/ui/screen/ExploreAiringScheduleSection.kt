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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.revolgenx.anilib.common.ext.browseGenreScreen
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClickWithValue
import com.revolgenx.anilib.home.explore.component.ExploreMediaCard
import com.revolgenx.anilib.home.explore.component.ExploreMediaCardHeight
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreAiringScheduleFilterViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreAiringScheduleViewModel
import com.revolgenx.anilib.media.ui.component.MediaComponentState
import com.revolgenx.anilib.media.ui.component.rememberMediaComponentState
import com.revolgenx.anilib.media.ui.model.toStringRes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

@Composable
fun ExploreAiringScheduleSection(viewModel: ExploreAiringScheduleViewModel) {
    val filterViewModel: ExploreAiringScheduleFilterViewModel = koinViewModel()
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
                    filterViewModel.field = viewModel.field.copy()
                    filterBottomSheetState.expand()
                }
            },
            onMore = {
                navigator.airingScheduleScreen()
            }
        )
        ExploreAiringHeader(viewModel) { startDate ->
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
            onPullRefresh = false
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
    ExploreMediaCard(
        media = media,
        mediaComponentState = mediaComponentState,
        footerContent = {
            val progressBehind = media.mediaListEntry?.progress?.let { progress ->
                airingScheduleModel.currentEpisode?.minus(progress)
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
                            LightText(
                                modifier = Modifier.clickable {
                                    mediaComponentState.navigator.browseGenreScreen(it)
                                },
                                text = it,
                                color = MaterialTheme.colorScheme.primary,
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
    val timeUntilAiringModel = airingScheduleModel.timeUntilAiringModel
    val airingAtModel = airingScheduleModel.airingAtModel
    val episode = airingScheduleModel.episode

    val timeUntilAired = remember {
        mutableStateOf(timeUntilAiringModel.formatString(context))
    }

    LaunchedEffect(Unit) {
        timeUntilAiringModel.renew()
        while (true) {
            if (timeUntilAiringModel.alreadyAired) break
            delay(1000)
            timeUntilAired.value = timeUntilAiringModel.formatString(context)
            timeUntilAiringModel.tick()
        }
    }

    val scheduleTimeText = if (timeUntilAiringModel.alreadyAired) {
        airingAtModel.airedAt
    } else {
        timeUntilAired.value
    }
    MediumText(
        text = stringResource(id = I18nR.string.ep_s_s).format(episode, scheduleTimeText),
        color = MaterialTheme.colorScheme.primary,
        fontSize = 12.sp
    )
}


@Composable
private fun ExploreAiringHeader(
    exploreAiringViewModel: ExploreAiringScheduleViewModel,
    onDaySelected: OnClickWithValue<Long>
) {
    val weekDays = exploreAiringViewModel.weekDaysFromToday.value
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp)
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