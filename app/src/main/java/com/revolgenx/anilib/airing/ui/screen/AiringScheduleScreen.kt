package com.revolgenx.anilib.airing.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import com.revolgenx.anilib.R
import com.revolgenx.anilib.airing.ui.model.AiringScheduleHeaderModel
import com.revolgenx.anilib.airing.ui.model.AiringScheduleModel
import com.revolgenx.anilib.airing.ui.model.TimeUntilAiringModel
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleViewModel
import com.revolgenx.anilib.common.ext.isNull
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.action.ActionMenuItem
import com.revolgenx.anilib.common.ui.component.action.ActionsMenu
import com.revolgenx.anilib.common.ui.component.common.MediaTitleType
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.screen.collectAsLazyPagingItems
import com.revolgenx.anilib.media.ui.model.title
import com.revolgenx.anilib.media.ui.model.toStringRes
import com.revolgenx.anilib.media.ui.screen.MediaScreen
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.fresco.FrescoImage
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/*TODO HANDLE USER DATA*/
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
    val context = LocalContext.current

    val dayRangeTitle = remember {
        derivedStateOf {
            if (viewModel.field.isWeeklyTypeDate) {
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

            if (viewModel.field.isWeeklyTypeDate) {
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            LazyPagingList(
                items = pagingItems,
                onRefresh = {
                    viewModel.refresh()
                },
                span = { index ->
                    val item = pagingItems[index]
                    GridItemSpan(if (item is AiringScheduleHeaderModel) maxLineSpan else maxCurrentLineSpan)
                }
            ) { airingScheduleModel ->
                airingScheduleModel ?: return@LazyPagingList
                when (airingScheduleModel) {
                    is AiringScheduleHeaderModel -> {
                        AiringScheduleHeader(airingScheduleModel = airingScheduleModel)
                    }

                    is AiringScheduleModel -> {
                        AiringScheduleItem(airingScheduleModel = airingScheduleModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun AiringScheduleItem(airingScheduleModel: AiringScheduleModel) {
    val media = airingScheduleModel.media ?: return
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.clickable {
//            navigator.push(MediaScreen(media.id, media.type))
        }) {
            FrescoImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(104.dp),
                imageUrl = media.coverImage?.image,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PaddingValues(horizontal = 8.dp, vertical = 4.dp)),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    MediaTitleType { type ->
                        Text(
                            media.title?.title(type).naText(),
                            modifier = Modifier
                                .padding(horizontal = 2.dp, vertical = 3.dp),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Row(
                        modifier = Modifier.padding(PaddingValues(vertical = 2.dp)),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        media.genres?.take(4)?.map { genre ->
                            Text(
                                genre,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.primary,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Text(
                        stringResource(id = media.format.toStringRes()),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                val epAiringIn = if (media.episodes.isNull()) {
                    stringResource(id = R.string.ep_s_airing_in).format(airingScheduleModel.episode)
                } else {
                    stringResource(id = R.string.ep_s_of_s_airing_in).format(
                        airingScheduleModel.episode,
                        media.episodes
                    )
                }
                Column {
                    Text(epAiringIn, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    AiringScheduleTimer(airingScheduleModel)
                }
            }
        }
    }
}

@Composable
private fun AiringScheduleHeader(airingScheduleModel: AiringScheduleHeaderModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(text = airingScheduleModel.title, style = MaterialTheme.typography.titleMedium)
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
    Text(
        text = airingScheduleTimer.timeLeft.value.formatString(localContext()),
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 20.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}


@Composable
fun AiringScheduleAction(viewModel: AiringScheduleViewModel) {
    var isMenuOpen by remember {
        mutableStateOf(false)
    }

    val field = viewModel.field
    val isWeeklyTypeDate = field.isWeeklyTypeDate

    ActionsMenu(
        isOpen = isMenuOpen,
        onToggleOverflow = { isMenuOpen = !isMenuOpen },
        items = listOf(
            ActionMenuItem.AlwaysShown(
                titleRes = R.string.previous,
                onClick = { viewModel.previous() },
                iconRes = R.drawable.ic_chevron_left,
                contentDescriptionRes = R.string.previous,
            ),
            ActionMenuItem.AlwaysShown(
                titleRes = R.string.next,
                onClick = { viewModel.next() },
                iconRes = R.drawable.ic_chevron_right,
                contentDescriptionRes = R.string.next,
            ),
            ActionMenuItem.NeverShown(
                titleRes = R.string.filter,
                onClick = {

                },
                iconRes = R.drawable.ic_filter,
                contentDescriptionRes = R.string.filter,
            ),
            ActionMenuItem.NeverShown(
                titleRes = R.string.select_date,
                onClick = {

                },
                iconRes = R.drawable.ic_calendar,
                contentDescriptionRes = R.string.select_date,
            ),
            ActionMenuItem.NeverShown(
                titleRes = R.string.weekly,
                onClick = {
                    viewModel.updateDateRange(!isWeeklyTypeDate)
                },
                iconRes = R.drawable.ic_time,
                contentDescriptionRes = R.string.weekly,
                isChecked = isWeeklyTypeDate,
                onCheckedChange = { isChecked ->
                    viewModel.updateDateRange(isChecked)
                }
            )
        ))
}