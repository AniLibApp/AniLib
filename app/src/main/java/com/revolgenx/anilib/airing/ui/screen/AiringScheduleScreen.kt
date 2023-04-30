package com.revolgenx.anilib.airing.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.androidx.AndroidScreen
import com.revolgenx.anilib.R
import com.revolgenx.anilib.airing.ui.model.AiringScheduleModel
import com.revolgenx.anilib.airing.ui.model.TimeUntilAiringModel
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleViewModel
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.screen.collectAsLazyPagingItems
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class AiringScheduleScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        AiringScreenContent()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AiringScreenContent(
    viewModel: AiringScheduleViewModel = koinViewModel()
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val field = viewModel.field
    val context = LocalContext.current

    val dayRangeTitle = remember {
        derivedStateOf {
            if (field.isWeeklyTypeDate) {
                context.getString(R.string.day_range_string).format(
                    viewModel.startDateTime.dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.getDefault()
                    ),

                    viewModel.endDateTime.dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.getDefault()
                    )
                )
            } else {
                viewModel.startDateTime.dayOfWeek.getDisplayName(
                    TextStyle.FULL,
                    Locale.getDefault()
                )
            }
        }
    }

    val dateRangeSubTitle = remember {
        derivedStateOf {
            val dateFormatPattern = context.getString(R.string.date_format_pattern)

            if (field.isWeeklyTypeDate) {
                context.getString(R.string.day_range_string).format(
                    viewModel.startDateTime.format(
                        DateTimeFormatter.ofPattern(
                            dateFormatPattern
                        )
                    ), viewModel.endDateTime.format(DateTimeFormatter.ofPattern(dateFormatPattern))
                )
            } else {
                viewModel.startDateTime.format(DateTimeFormatter.ofPattern(dateFormatPattern))
            }
        }
    }

    ScreenScaffold(
        title = dayRangeTitle.value,
        subTitle = dateRangeSubTitle.value,
        actions = {
            AiringScheduleAction(viewModel)
        },
        scrollBehavior = scrollBehavior
    ) {
        val pagingItems = viewModel.collectAsLazyPagingItems()
        LazyPagingList(
            items = pagingItems,
            onRefresh = {
                viewModel.refresh()
            }
        ) { airingScheduleModel ->
            airingScheduleModel ?: return@LazyPagingList
            AiringScheduleItem(airingScheduleModel)
        }
    }
}

@Composable
private fun AiringScheduleItem(airingScheduleModel: AiringScheduleModel) {
    Card {
        Row {
            Column {
                AiringScheduleTimer(airingScheduleModel)
            }
        }
    }
}

@Composable
private fun AiringScheduleTimer(airingScheduleModel: AiringScheduleModel) {
    val airingScheduleTimer = airingScheduleModel.airingScheduleTimer ?: return
    DisposableEffect(airingScheduleModel.id) {
        airingScheduleTimer.start()
        onDispose {
            airingScheduleTimer.stop()
        }
    }
    TimeUntilAiring(
        timeUntilAiringModel = airingScheduleTimer.timeLeft.value,
    )
}

@Composable
private fun TimeUntilAiring(timeUntilAiringModel: TimeUntilAiringModel?) {
    timeUntilAiringModel ?: return
    Text(text = timeUntilAiringModel.getFormattedTime())
}


@Composable
fun AiringScheduleAction(viewModel: AiringScheduleViewModel) {
    IconButton(onClick = { viewModel.previous() }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_left),
            contentDescription = stringResource(
                id = R.string.previous
            )
        )
    }
    IconButton(onClick = { viewModel.next() }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = stringResource(
                id = R.string.next
            )
        )
    }
    IconButton(onClick = { viewModel.next() }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_more_vert),
            contentDescription = stringResource(
                id = R.string.more
            )
        )
    }
}