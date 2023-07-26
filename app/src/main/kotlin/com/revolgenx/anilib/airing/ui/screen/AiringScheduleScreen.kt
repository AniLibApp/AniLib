package com.revolgenx.anilib.airing.ui.screen

import android.util.Range
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import com.revolgenx.anilib.R
import com.revolgenx.anilib.airing.data.field.AiringScheduleField
import com.revolgenx.anilib.airing.ui.model.AiringScheduleModel
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleFilterViewModel
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleViewModel
import com.revolgenx.anilib.common.ext.isNull
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.action.BottomSheetConfirmationAction
import com.revolgenx.anilib.common.ui.component.action.OverflowMenu
import com.revolgenx.anilib.common.ui.component.action.OverflowMenuItem
import com.revolgenx.anilib.common.ui.component.common.Header
import com.revolgenx.anilib.common.ui.component.common.MediaCoverImageType
import com.revolgenx.anilib.common.ui.component.common.MediaTitleType
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.ui.component.menu.AlSortMenuItem
import com.revolgenx.anilib.common.ui.component.menu.AlSortOrder
import com.revolgenx.anilib.common.ui.component.menu.SortSelectMenu
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.model.HeaderModel
import com.revolgenx.anilib.common.ui.theme.background
import com.revolgenx.anilib.common.ui.theme.onSurfaceVariant
import com.revolgenx.anilib.common.ui.theme.primary
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.media.ui.model.toStringRes
import com.revolgenx.anilib.type.AiringSort
import com.skydoves.landscapist.ImageOptions
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

    val context = LocalContext.current
    val field = viewModel.field
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val calendarState = rememberUseCaseState()
    val openBottomSheet = rememberSaveable { mutableStateOf(false) }
    val navigator = localNavigator()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val calendarRange = remember {
        derivedStateOf {
            Range(viewModel.startDateTime.toLocalDate(), viewModel.endDateTime.toLocalDate())
        }
    }

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
            AiringScheduleAction(
                isWeeklyTypeDate = field.isWeeklyTypeDate,
                onPrevious = {
                    viewModel.previous()
                },
                onNext = {
                    viewModel.next()
                },
                onFilter = {
                    openBottomSheet.value = true
                },
                onCalendar = {
                    calendarState.show()
                },
                onWeekly = {
                    viewModel.updateDateRange(!field.isWeeklyTypeDate)
                }
            )
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
                pagingItems = pagingItems,
                onRefresh = {
                    viewModel.refresh()
                },
                span = { index ->
                    val item = pagingItems[index]
                    GridItemSpan(if (item is HeaderModel) maxLineSpan else 1)
                }
            ) { model ->
                model ?: return@LazyPagingList
                when (model) {
                    is HeaderModel -> {
                        Header(header = model)
                    }

                    is AiringScheduleModel -> {
                        AiringScheduleItem(airingScheduleModel = model) {
                            navigator.mediaScreen(
                                model.mediaId,
                                model.media?.type
                            )
                        }
                    }
                }
            }
        }


        AiringScheduleFilterBottomSheet(
            openBottomSheet = openBottomSheet,
            bottomSheetState = bottomSheetState
        ) { airingScheduleField ->
            viewModel.updateField(airingScheduleField)
        }

    }


    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(style = CalendarStyle.MONTH),
        selection = if (field.isWeeklyTypeDate) {
            CalendarSelection.Period(
                selectedRange = calendarRange.value
            ) { startDate, endDate ->
                viewModel.updateDates(startDate, endDate)
            }
        } else {
            CalendarSelection.Date(selectedDate = viewModel.startDateTime.toLocalDate()) { startDate ->
                viewModel.updateStartDate(startDate)
            }
        })

}

@Composable
private fun AiringScheduleItem(airingScheduleModel: AiringScheduleModel, onClick: OnClick) {
    val media = airingScheduleModel.media ?: return
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.clickable {
            onClick()
        }) {
            MediaCoverImageType {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(104.dp),
                    imageUrl = media.coverImage?.image(it),
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    previewPlaceholder = R.drawable.bleach
                )
            }
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
                                color = primary,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Text(
                        stringResource(id = media.format.toStringRes()),
                        fontSize = 12.sp,
                        color = primary,
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
                    Text(epAiringIn, color = onSurfaceVariant)
                    AiringScheduleTimer(airingScheduleModel)
                }
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
    Text(
        text = airingScheduleTimer.timeLeft.value.formatString(localContext()),
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 20.sp,
        color = onSurfaceVariant,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AiringScheduleFilterBottomSheet(
    openBottomSheet: MutableState<Boolean> = mutableStateOf(false),
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    airingScheduleViewModel: AiringScheduleViewModel = koinViewModel(),
    viewModel: AiringScheduleFilterViewModel = koinViewModel(),
    onDone: (field: AiringScheduleField) -> Unit
) {
    if (openBottomSheet.value) {
        viewModel.field.value = airingScheduleViewModel.field.copy()

        ModalBottomSheet(
            onDismissRequest = { openBottomSheet.value = false },
            sheetState = bottomSheetState,
            containerColor = background
        ) {
            AiringScheduleFilterBottomSheetContent(
                field = viewModel.field.value,
                onPositiveClicked = {
                    onDone.invoke(viewModel.field.value)
                }
            ) {
                openBottomSheet.value = false
            }
        }
    }
}

@Composable
private fun AiringScheduleFilterBottomSheetContent(
    modifier: Modifier = Modifier,
    field: AiringScheduleField,
    onNegativeClicked: (() -> Unit)? = null,
    onPositiveClicked: (() -> Unit)? = null,
    dismiss: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 4.dp)
    ) {
        BottomSheetConfirmationAction(
            onPositiveClicked = {
                onPositiveClicked?.invoke()
                dismiss?.invoke()
            },
            onNegativeClicked = {
                onNegativeClicked?.invoke()
                dismiss?.invoke()
            }
        )
        LazyColumn() {
            item {
                val sort = field.sort ?: return@item
                val selectedSort = (sort.ordinal + 1) / 2
                val isDesc = sort.rawValue.endsWith("_DESC")
                val selectedSortIndex = if (isDesc) selectedSort - 1 else selectedSort
                val selectedSortOrder = if (isDesc) AlSortOrder.DESC else AlSortOrder.ASC

                val sortMenus =
                    stringArrayResource(id = R.array.airing_sort).mapIndexed { index, s ->
                        AlSortMenuItem(
                            s,
                            if (index == selectedSortIndex) selectedSortOrder else AlSortOrder.NONE
                        )
                    }

                SortSelectMenu(
                    label = stringResource(id = R.string.sort),
                    entries = sortMenus,
                    allowNone = false
                ) { index, selectedItem ->
                    selectedItem ?: return@SortSelectMenu
                    val order = selectedItem.order
                    val airingIndex = index * 2
                    val airingSort: AiringSort =
                        AiringSort.values()[if (order == AlSortOrder.DESC) airingIndex + 1 else airingIndex]
                    field.sort = airingSort
                }
            }
        }
    }

}


@Composable
private fun AiringScheduleAction(
    isWeeklyTypeDate: Boolean = false,
    onNext: OnClick,
    onPrevious: OnClick,
    onFilter: OnClick,
    onCalendar: OnClick,
    onWeekly: OnClick,
) {
    ActionMenu(iconRes = R.drawable.ic_chevron_left, onClick = onPrevious)
    ActionMenu(iconRes = R.drawable.ic_chevron_right, onClick = onNext)
    OverflowMenu {
        OverflowMenuItem(
            textRes = R.string.filter,
            iconRes = R.drawable.ic_filter,
            onClick = onFilter,
            contentDescriptionRes = R.string.filter
        )
        OverflowMenuItem(
            textRes = R.string.select_date,
            iconRes = R.drawable.ic_calendar,
            onClick = onCalendar,
            contentDescriptionRes = R.string.select_date,
        )
        OverflowMenuItem(
            textRes = R.string.weekly,
            iconRes = R.drawable.ic_time,
            onClick = onWeekly,
            contentDescriptionRes = R.string.weekly,
            isChecked = isWeeklyTypeDate,
            onCheckedChange = {
                onWeekly.invoke()
            }
        )
    }
}