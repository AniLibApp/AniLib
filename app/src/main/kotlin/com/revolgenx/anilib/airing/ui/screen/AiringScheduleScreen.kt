package com.revolgenx.anilib.airing.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import com.revolgenx.anilib.R
import com.revolgenx.anilib.airing.data.field.AiringScheduleField
import com.revolgenx.anilib.airing.ui.model.AiringAtModel
import com.revolgenx.anilib.airing.ui.model.AiringScheduleModel
import com.revolgenx.anilib.airing.ui.model.AiringScheduleTimer
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleFilterViewModel
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleViewModel
import com.revolgenx.anilib.common.ext.isNull
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.action.BottomSheetConfirmation
import com.revolgenx.anilib.common.ui.component.action.OverflowMenu
import com.revolgenx.anilib.common.ui.component.action.OverflowMenuItem
import com.revolgenx.anilib.common.ui.component.common.HeaderBox
import com.revolgenx.anilib.common.ui.component.common.ShowIfLoggedIn
import com.revolgenx.anilib.common.ui.component.date.CalendarBottomSheet
import com.revolgenx.anilib.common.ui.component.date.CalendarRangeBottomSheet
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.component.menu.AlSortMenuItem
import com.revolgenx.anilib.common.ui.component.menu.AlSortOrder
import com.revolgenx.anilib.common.ui.component.menu.SortSelectMenu
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.text.LargeSemiBoldText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.component.toggle.TextSwitch
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcCalendar
import com.revolgenx.anilib.common.ui.icons.appicon.IcChevronLeft
import com.revolgenx.anilib.common.ui.icons.appicon.IcChevronRight
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.ui.icons.appicon.IcTime
import com.revolgenx.anilib.common.ui.model.HeaderModel
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.media.ui.component.MediaCoverImageType
import com.revolgenx.anilib.media.ui.component.MediaTitleType
import com.revolgenx.anilib.media.ui.model.toStringRes
import com.revolgenx.anilib.type.AiringSort
import org.koin.androidx.compose.koinViewModel
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import anilib.i18n.R as I18nR

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

    val context = localContext()
    val field = viewModel.field
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val openBottomSheet = rememberSaveable { mutableStateOf(false) }
    val navigator = localNavigator()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val openCalendarDialog = rememberSaveable { mutableStateOf(false) }

    val calendarRange = remember {
        derivedStateOf {
            Pair(
                viewModel.startDateTime.toLocalDate().atStartOfDay(ZoneOffset.UTC).toInstant()
                    .toEpochMilli(),
                viewModel.endDateTime.toLocalDate().atStartOfDay(ZoneOffset.UTC).toInstant()
                    .toEpochMilli()
            )
        }
    }

    val dayRangeTitle = remember {
        derivedStateOf {
            if (viewModel.field.isWeeklyTypeDate) {
                context.getString(I18nR.string.day_range_string).format(
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
            val dateFormatPattern = context.getString(I18nR.string.date_format_pattern)

            if (viewModel.field.isWeeklyTypeDate) {
                context.getString(I18nR.string.day_range_string).format(
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
                    openCalendarDialog.value = true
                },
                onWeekly = {
                    viewModel.updateDateRange(!field.isWeeklyTypeDate)
                }
            )
        },
        scrollBehavior = scrollBehavior
    ) {
        val pagingItems = viewModel.collectAsLazyPagingItems()

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
                    HeaderBox(modifier = Modifier.padding(horizontal = 8.dp),header = model)
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


    if (field.isWeeklyTypeDate) {
        CalendarRangeBottomSheet(
            openBottomSheet = openCalendarDialog,
            initialSelectedStartDateMillis = calendarRange.value.first,
            initialSelectedEndDateMillis = calendarRange.value.second,
        ) { selectedStartDateMillis, selectedEndDateMillis ->
            viewModel.updateDates(selectedStartDateMillis, selectedEndDateMillis)
        }
    } else {
        CalendarBottomSheet(
            openBottomSheet = openCalendarDialog,
            initialSelectedDateMillis = calendarRange.value.first,
        ) { selectedDateMillis ->
            viewModel.updateStartDate(selectedDateMillis)
        }
    }

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
                ImageAsync(
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
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp, bottom = 4.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    MediaTitleType { type ->
                        MediumText(
                            text = media.title?.title(type).naText(),
                            fontSize = 16.sp,
                            lineHeight = 18.sp
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

                val timeUntilAiringModel = airingScheduleModel.timeUntilAiringModel
                val epAiringTitle = if (media.episodes.isNull()) {
                    val epAiredRes = if (timeUntilAiringModel.alreadyAired) {
                        I18nR.string.ep_s_aired_on
                    } else {
                        I18nR.string.ep_s_airing_in
                    }
                    stringResource(id = epAiredRes).format(airingScheduleModel.episode)
                } else {
                    val epAiredRes = if (timeUntilAiringModel.alreadyAired) {
                        I18nR.string.ep_s_of_s_aired_on
                    } else {
                        I18nR.string.ep_s_of_s_airing_in
                    }
                    stringResource(id = epAiredRes).format(
                        airingScheduleModel.episode,
                        media.episodes
                    )
                }
                Column {
                    Text(
                        text = epAiringTitle,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )
                    AiringScheduleTimer(
                        airingScheduleModel.airingAtModel,
                        airingScheduleModel.airingScheduleTimer!!,
                    )
                }
            }
        }
    }
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
    LargeSemiBoldText(
        text = scheduleTimeText,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
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
            sheetState = bottomSheetState
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
            .padding(bottom = 4.dp)
    ) {
        BottomSheetConfirmation(
            confirmClicked = {
                onPositiveClicked?.invoke()
                dismiss?.invoke()
            },
            dismissClicked = {
                onNegativeClicked?.invoke()
                dismiss?.invoke()
            }
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AiringSortMenu(field)
            TextSwitch(
                title = stringResource(id = I18nR.string.include_already_aired),
                checked = !field.notYetAired,
                onCheckedChanged = {
                    field.notYetAired = !it
                })
            ShowIfLoggedIn {
                TextSwitch(
                    title = stringResource(id = I18nR.string.show_only_from_your_watching_list),
                    checked = field.showOnlyWatching,
                    onCheckedChanged = {
                        field.showOnlyWatching = !it
                    })

                TextSwitch(
                    title = stringResource(id = I18nR.string.show_only_from_your_planning_list),
                    checked = field.showOnlyPlanning,
                    onCheckedChanged = {
                        field.showOnlyPlanning = !it
                    })
            }
        }
    }
}

@Composable
private fun AiringSortMenu(field: AiringScheduleField) {
    val selectedSort = (field.sort.ordinal + 1) / 2
    val isDesc = field.sort.rawValue.endsWith("_DESC")
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
        label = stringResource(id = I18nR.string.sort),
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


@Composable
private fun AiringScheduleAction(
    isWeeklyTypeDate: Boolean = false,
    onNext: OnClick,
    onPrevious: OnClick,
    onFilter: OnClick,
    onCalendar: OnClick,
    onWeekly: OnClick,
) {
    ActionMenu(icon = AppIcons.IcChevronLeft, onClick = onPrevious)
    ActionMenu(icon = AppIcons.IcChevronRight, onClick = onNext)
    OverflowMenu { expanded ->
        OverflowMenuItem(
            textRes = I18nR.string.filter,
            icon = AppIcons.IcFilter,
            onClick = {
                expanded.value = false
                onFilter()
            },
            contentDescriptionRes = I18nR.string.filter
        )
        OverflowMenuItem(
            textRes = I18nR.string.select_date,
            icon = AppIcons.IcCalendar,
            onClick = {
                expanded.value = false
                onCalendar()
            },
            contentDescriptionRes = I18nR.string.select_date,
        )
        OverflowMenuItem(
            textRes = I18nR.string.weekly,
            icon = AppIcons.IcTime,
            contentDescriptionRes = I18nR.string.weekly,
            isChecked = isWeeklyTypeDate,
            onCheckedChange = {
                onWeekly()
            },
        ) {
            expanded.value = false
            onWeekly()
        }
    }
}