package com.revolgenx.anilib.media.ui.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.constant.AlMediaSort
import com.revolgenx.anilib.common.data.tuples.to
import com.revolgenx.anilib.common.ext.getOrEmpty
import com.revolgenx.anilib.common.ui.component.action.BottomSheetConfirmation
import com.revolgenx.anilib.common.ui.component.menu.AlSortMenuItem
import com.revolgenx.anilib.common.ui.component.menu.AlSortOrder
import com.revolgenx.anilib.common.ui.component.menu.SelectMenu
import com.revolgenx.anilib.common.ui.component.menu.MultiSelectMenu
import com.revolgenx.anilib.common.ui.component.menu.SortSelectMenu
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.media.ui.model.toMediaFormat
import com.revolgenx.anilib.media.ui.model.toMediaSeason
import com.revolgenx.anilib.media.ui.model.toMediaStatus
import com.revolgenx.anilib.media.ui.viewmodel.MediaFilterBottomSheetViewModel
import com.revolgenx.anilib.type.MediaSort
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar
import anilib.i18n.R as I18nR


private val yearLesser = Calendar.getInstance().get(Calendar.YEAR) + 2
private val yearGreater = 1940
private val yearList by lazy {
    (yearLesser downTo yearGreater).map { it.toString() }
}


@Composable
private fun MediaFilterBottomSheetContent(
    modifier: Modifier = Modifier,
    field: MediaField,
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val selectedFormats = field.formatsIn?.map { it.ordinal }.getOrEmpty()
            val formats = stringArrayResource(id = R.array.media_format)
            MultiSelectMenu(
                label = stringResource(id = I18nR.string.format),
                entries = formats.mapIndexed { index, s ->
                    selectedFormats.contains(
                        index
                    ) to s
                },
            ) { selectedItems ->
                field.formatsIn = selectedItems.takeIf { it.isNotEmpty() }
                    ?.mapNotNull { it.first.toMediaFormat() }
            }
            SelectMenu(
                label = stringResource(id = I18nR.string.status),
                entries = stringArrayResource(id = R.array.media_status).toList(),
                selectedItemPosition = field.status?.ordinal,
                showNoneItem = true
            ) { selectedItem ->
                field.status = selectedItem.takeIf { it > -1 }?.toMediaStatus()
            }
            SelectMenu(
                label = stringResource(id = I18nR.string.season),
                entries = stringArrayResource(id = R.array.media_season).toList(),
                selectedItemPosition = field.season?.ordinal
            ) { selectedItem ->
                field.season = selectedItem.takeIf { it > -1 }?.toMediaSeason()
            }
            val sort = field.sort
            var selectedSortIndex: Int? = null
            var selectedSortOrder: AlSortOrder = AlSortOrder.NONE

            if (sort != null) {
                val isDesc = sort.rawValue.endsWith("_DESC")
                val alMediaSort =
                    AlMediaSort.from(if (isDesc) sort.ordinal - 1 else sort.ordinal)
                selectedSortIndex = alMediaSort?.ordinal
                selectedSortOrder = if (isDesc) AlSortOrder.DESC else AlSortOrder.ASC
            }

            val sortMenus =
                stringArrayResource(id = R.array.media_sort).mapIndexed { index, s ->
                    AlSortMenuItem(
                        s,
                        if (index == selectedSortIndex) selectedSortOrder else AlSortOrder.NONE
                    )
                }

            SortSelectMenu(
                label = stringResource(id = I18nR.string.sort),
                entries = sortMenus,
            ) { index, selectedItem ->
                var mediaSort: MediaSort? = null

                if (selectedItem != null) {
                    val alMediaSort = AlMediaSort.values()[index].sort
                    val selectedSort = if (selectedItem.order == AlSortOrder.DESC) {
                        alMediaSort + 1
                    } else {
                        alMediaSort
                    }
                    mediaSort = MediaSort.values()[selectedSort]
                }

                field.sort = mediaSort
            }
            SelectMenu(
                label = stringResource(id = I18nR.string.year),
                entries = yearList,
                selectedItemPosition = field.seasonYear?.takeIf { it in yearGreater..yearLesser }
                    ?.let { yearList.indexOf(it.toString()) }
            ) { selectedItem ->
                field.seasonYear =
                    selectedItem.takeIf { it > -1 }?.let { yearList[it].toInt() }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaFilterBottomSheet(
    openBottomSheet: MutableState<Boolean> = mutableStateOf(false),
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    viewModel: MediaFilterBottomSheetViewModel = koinViewModel()
) {
    if (openBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet.value = false },
            sheetState = bottomSheetState
        ) {
            MediaFilterBottomSheetContent(
                field = viewModel.field,
                onPositiveClicked = {
                    viewModel.updateFilter()
                }
            ) {
                openBottomSheet.value = false
            }
        }
    }
}

