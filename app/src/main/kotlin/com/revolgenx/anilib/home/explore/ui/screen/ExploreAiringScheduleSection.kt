package com.revolgenx.anilib.home.explore.ui.screen

import IcAiring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.revolgenx.anilib.airing.ui.model.AiringAtModel
import com.revolgenx.anilib.airing.ui.model.AiringScheduleModel
import com.revolgenx.anilib.airing.ui.model.AiringScheduleTimer
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleViewModel
import com.revolgenx.anilib.common.ext.airingScheduleScreen
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ui.component.text.SmallLightText
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClickWithValue
import com.revolgenx.anilib.common.util.OnMediaClick
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreAiringViewModel
import com.revolgenx.anilib.media.ui.component.MediaCard
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

@Composable
fun ExploreAiringScheduleSection() {
    val viewModel: AiringScheduleViewModel = koinViewModel()
    val exploreAiringViewModel: ExploreAiringViewModel = koinViewModel()
    val navigator = localNavigator()
    Column {
        ExploreScreenHeader(
            text = stringResource(id = I18nR.string.airing),
            icon = AppIcons.IcAiring,
            onFilter = {

            },
            onMore = {
                navigator.airingScheduleScreen()
            }
        )
//        ExploreAiringHeader(exploreAiringViewModel){ startDate ->
//            viewModel.updateStartDate(startDate)
//        }
        ExploreAiringScheduleContent(viewModel)
    }
}

@Composable
private fun ExploreAiringScheduleContent(viewModel: AiringScheduleViewModel) {
    val pagingItems = viewModel.collectAsLazyPagingItems()
    val navigator = localNavigator()

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
            AiringScheduleItem(airingScheduleModel = model) { id, type ->
                navigator.mediaScreen(id, type)
            }
        }
    }
}


@Composable
private fun AiringScheduleItem(airingScheduleModel: AiringScheduleModel, onClick: OnMediaClick) {
    val media = airingScheduleModel.media ?: return
    MediaCard(
        media = media,
        onMediaClick = onClick,
        width = ExploreMediaCardWidth,
        height = ExploreMediaCardHeight,
        footerContent = {
            AiringScheduleTimer(
                airingAtModel = airingScheduleModel.airingAtModel,
                airingScheduleTimer = airingScheduleModel.airingScheduleTimer!!
            )
        }
    )
}

@Composable
private fun AiringScheduleTimer(
    airingAtModel: AiringAtModel,
    airingScheduleTimer: AiringScheduleTimer
) {
    val context = localContext()
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
    SmallLightText(
        text = scheduleTimeText,
        color = MaterialTheme.colorScheme.primary,
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