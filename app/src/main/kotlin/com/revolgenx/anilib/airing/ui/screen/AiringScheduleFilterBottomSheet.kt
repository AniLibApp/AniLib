package com.revolgenx.anilib.airing.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import anilib.i18n.R
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.m3.BottomSheet
import com.dokar.sheets.m3.BottomSheetDefaults
import com.revolgenx.anilib.airing.data.field.AiringScheduleField
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleFilterViewModel
import com.revolgenx.anilib.common.ui.component.action.BottomSheetConfirmation
import com.revolgenx.anilib.common.ui.component.common.ShowIfLoggedIn
import com.revolgenx.anilib.common.ui.component.menu.SortMenuItem
import com.revolgenx.anilib.common.ui.component.menu.SortOrder
import com.revolgenx.anilib.common.ui.component.menu.SortSelectMenu
import com.revolgenx.anilib.common.ui.component.toggle.TextSwitch
import com.revolgenx.anilib.type.AiringSort
import kotlinx.coroutines.launch


@Composable
fun AiringScheduleFilterBottomSheet(
    bottomSheetState: BottomSheetState,
    viewModel: AiringScheduleFilterViewModel,
) {
    val scope = rememberCoroutineScope()
    BottomSheet(
        state = bottomSheetState,
        skipPeeked = true,
        behaviors = BottomSheetDefaults.dialogSheetBehaviors(navigationBarColor = BottomSheetDefaults.backgroundColor)
    ) {
        AiringScheduleFilterBottomSheetContent(
            field = viewModel.field,
            onPositiveClicked = {
                viewModel.updateFilter()
            },
        ) {
            scope.launch {
                bottomSheetState.collapse()
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
            onConfirm = {
                onPositiveClicked?.invoke()
                dismiss?.invoke()
            },
            onDismiss = {
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
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.include_already_aired),
                checked = !field.notYetAired,
                onCheckedChanged = {
                    field.notYetAired = !it
                })
            ShowIfLoggedIn {
                TextSwitch(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.show_only_from_your_watching_list),
                    checked = field.showOnlyWatching,
                    onCheckedChanged = {
                        field.showOnlyWatching = it
                    })

                TextSwitch(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.show_only_from_your_planning_list),
                    checked = field.showOnlyPlanning,
                    onCheckedChanged = {
                        field.showOnlyPlanning = it
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
    val selectedSortOrder = if (isDesc) SortOrder.DESC else SortOrder.ASC
    val sortMenus =
        stringArrayResource(id = com.revolgenx.anilib.R.array.airing_sort).mapIndexed { index, s ->
            SortMenuItem(
                s,
                if (index == selectedSortIndex) selectedSortOrder else SortOrder.NONE
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
            AiringSort.entries[if (order == SortOrder.DESC) airingIndex + 1 else airingIndex]
        field.sort = airingSort
    }
}