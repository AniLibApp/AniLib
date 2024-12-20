package com.revolgenx.anilib.media.ui.filter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.m3.BottomSheet
import com.dokar.sheets.m3.BottomSheetDefaults
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.constant.AlMediaSort
import com.revolgenx.anilib.common.data.tuples.to
import com.revolgenx.anilib.common.ui.component.action.BottomSheetConfirmation
import com.revolgenx.anilib.common.ui.component.menu.MultiSelectMenu
import com.revolgenx.anilib.common.ui.component.menu.MultiSelectModel
import com.revolgenx.anilib.common.ui.component.menu.SelectFilterMenu
import com.revolgenx.anilib.common.ui.component.menu.SelectMenu
import com.revolgenx.anilib.common.ui.component.menu.SelectSearchFilterMenu
import com.revolgenx.anilib.common.ui.component.menu.SelectType
import com.revolgenx.anilib.common.ui.component.menu.SortMenuItem
import com.revolgenx.anilib.common.ui.component.menu.SortOrder
import com.revolgenx.anilib.common.ui.component.menu.SortSelectMenu
import com.revolgenx.anilib.home.season.ui.viewmodel.SeasonFilterViewModel
import com.revolgenx.anilib.media.ui.model.toMediaSeason
import com.revolgenx.anilib.media.ui.model.toMediaStatus
import com.revolgenx.anilib.media.ui.viewmodel.MediaFilterBottomSheetViewModel
import com.revolgenx.anilib.type.MediaFormat
import com.revolgenx.anilib.type.MediaSort
import kotlinx.coroutines.launch
import java.util.Calendar
import anilib.i18n.R as I18nR


private val yearLesser = Calendar.getInstance().get(Calendar.YEAR) + 2
private const val yearGreater = 1940
private val yearList by lazy {
    (yearLesser downTo yearGreater).map { it.toString() }.toTypedArray()
}


@Composable
fun MediaFilterBottomSheet(
    bottomSheetState: BottomSheetState,
    viewModel: MediaFilterBottomSheetViewModel
) {
    val scope = rememberCoroutineScope()

    BottomSheet(
        state = bottomSheetState,
        skipPeeked = true,
        behaviors = BottomSheetDefaults.dialogSheetBehaviors(navigationBarColor = BottomSheetDefaults.backgroundColor)
    ) {
        MediaFilterBottomSheetContent(
            viewModel = viewModel,
            onPositiveClicked = {
                viewModel.updateFilter()
            },
            onDismiss = {
                scope.launch {
                    bottomSheetState.collapse()
                }
            }

        )
    }

}


@Composable
private fun MediaFilterBottomSheetContent(
    viewModel: MediaFilterBottomSheetViewModel,
    onPositiveClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    val field = viewModel.field
    val isSeason = viewModel is SeasonFilterViewModel

    Column(
        modifier = Modifier
            .padding(bottom = 4.dp)
    ) {
        BottomSheetConfirmation(
            onConfirm = {
                onPositiveClicked.invoke()
                onDismiss.invoke()
            },
            onDismiss = {
                onDismiss.invoke()
            }
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(vertical = 8.dp)
        ) {
            val selectedFormats = field.formatsIn?.map { it.ordinal }.orEmpty()
            val formats = stringArrayResource(id = R.array.media_format)


            MultiSelectMenu(
                label = stringResource(id = I18nR.string.format),
                text = { it.second },
                entries = formats.mapIndexed { index, s ->
                    MultiSelectModel(
                        mutableStateOf(
                            selectedFormats.contains(
                                index
                            )
                        ),
                        index to s
                    )
                },
            ) { selectedItems ->
                field.formatsIn = selectedItems.takeIf { it.isNotEmpty() }
                    ?.mapNotNull { MediaFormat.entries.getOrNull(it.first) }
            }

            SelectMenu(
                label = stringResource(id = I18nR.string.status),
                entries = stringArrayResource(id = R.array.media_status),
                selectedItemPosition = field.status?.ordinal,
                showNoneItem = true
            ) { selectedItem ->
                field.status = selectedItem.takeIf { it > -1 }?.toMediaStatus()
            }

            SelectMenu(
                label = stringResource(id = I18nR.string.season),
                entries = stringArrayResource(id = R.array.media_season),
                showNoneItem = !isSeason,
                selectedItemPosition = field.season?.ordinal
            ) { selectedItem ->
                field.season = selectedItem.takeIf { it > -1 }?.toMediaSeason()
            }

            if (isSeason) {
                val sort = field.sort
                var selectedSortIndex: Int? = null
                var selectedSortOrder: SortOrder = SortOrder.NONE

                if (sort != null) {
                    val isDesc = sort.rawValue.endsWith("_DESC")
                    val alMediaSort =
                        AlMediaSort.from(if (isDesc) sort.ordinal - 1 else sort.ordinal)
                    selectedSortIndex = alMediaSort?.ordinal
                    selectedSortOrder = if (isDesc) SortOrder.DESC else SortOrder.ASC
                }

                val sortMenus =
                    stringArrayResource(id = R.array.media_sort).mapIndexed { index, s ->
                        SortMenuItem(
                            s,
                            if (index == selectedSortIndex) selectedSortOrder else SortOrder.NONE
                        )
                    }

                SortSelectMenu(
                    label = stringResource(id = I18nR.string.sort),
                    entries = sortMenus,
                ) { index, selectedItem ->
                    var mediaSort: MediaSort? = null

                    if (selectedItem != null) {
                        val alMediaSort = AlMediaSort.entries[index].sort
                        val selectedSort = if (selectedItem.order == SortOrder.DESC) {
                            alMediaSort + 1
                        } else {
                            alMediaSort
                        }
                        mediaSort = MediaSort.entries.toTypedArray()[selectedSort]
                    }

                    field.sort = mediaSort
                }

            }

            SelectMenu(
                label = stringResource(id = I18nR.string.year),
                entries = yearList,
                showNoneItem = !isSeason,
                selectedItemPosition = field.seasonYear?.takeIf { it in yearGreater..yearLesser }
                    ?.let { yearList.indexOf(it.toString()) }
            ) { selectedItem ->
                field.seasonYear =
                    selectedItem.takeIf { it > -1 }?.let { yearList[it].toInt() }
            }

            SelectFilterMenu(
                label = stringResource(id = anilib.i18n.R.string.genre),
                entries = viewModel.selectGenreCollections,
                onItemsSelected = {
                    viewModel.field.genreIn =
                        it.filter { it.selected.value == SelectType.INCLUDED }
                            .map { it.data }
                            .toMutableList()

                    viewModel.field.genreNotIn =
                        it.filter { it.selected.value == SelectType.EXCLUDED }
                            .map { it.data }
                            .toMutableList()
                }
            )

            SelectSearchFilterMenu(
                label = stringResource(id = anilib.i18n.R.string.tags),
                entries = viewModel.selectMediaTagCollections,
                showSearchFilter = true,
                onItemsSelected = {
                    viewModel.field.tagIn =
                        it.filter { it.selected.value == SelectType.INCLUDED }
                            .map { it.data }
                            .toMutableList()
                    viewModel.field.tagNotIn =
                        it.filter { it.selected.value == SelectType.EXCLUDED }
                            .map { it.data }
                            .toMutableList()
                }
            )



        }
    }
}

