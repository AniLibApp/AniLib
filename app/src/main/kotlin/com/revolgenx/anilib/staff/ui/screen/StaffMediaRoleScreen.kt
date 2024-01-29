package com.revolgenx.anilib.staff.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.revolgenx.anilib.common.ui.component.card.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.m3.BottomSheet
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.emptyWindowInsets
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ui.component.action.BottomSheetConfirmation
import com.revolgenx.anilib.common.ui.component.action.DisappearingFAB
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.common.HeaderBox
import com.revolgenx.anilib.common.ui.component.menu.SelectMenu
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.toggle.TextSwitch
import com.revolgenx.anilib.common.ui.compose.paging.GridOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.ui.model.HeaderModel
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClickWithValue
import com.revolgenx.anilib.media.ui.component.MediaComponentState
import com.revolgenx.anilib.media.ui.component.MediaItemRowContent
import com.revolgenx.anilib.media.ui.component.rememberMediaComponentState
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import com.revolgenx.anilib.staff.data.field.StaffMediaRoleField
import com.revolgenx.anilib.staff.data.field.StaffMediaRoleSort
import com.revolgenx.anilib.staff.ui.viewmodel.StaffMediaRoleFilterViewModel
import com.revolgenx.anilib.staff.ui.viewmodel.StaffMediaRoleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffMediaRoleScreen(viewModel: StaffMediaRoleViewModel) {

    val filterViewModel: StaffMediaRoleFilterViewModel = koinViewModel()
    val filterBottomSheetState = rememberBottomSheetState()
    val scope = rememberCoroutineScope()


    val navigator = localNavigator()
    val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
    val bottomScrollConnection =
        remember { BottomNestedScrollConnection(state = scrollState) }

    ScreenScaffold(
        topBar = {},
        floatingActionButton = {
            DisappearingFAB(scrollState = scrollState, icon = AppIcons.IcFilter) {
                scope.launch {
                    filterViewModel.field = viewModel.field.copy()
                    filterBottomSheetState.expand()
                }
            }
        },
        bottomNestedScrollConnection = bottomScrollConnection,
    ) {
        val snackbar = localSnackbarHostState()
        val mediaComponentState =
            rememberMediaComponentState(navigator = navigator, snackbarHostState = snackbar)
        StaffMediaRolePagingContent(viewModel, scope, snackbar, mediaComponentState)

        StaffMediaRoleFilterBottomSheet(
            bottomSheetState = filterBottomSheetState,
            viewModel = filterViewModel
        ) {
            viewModel.field = it
            viewModel.refresh()
        }
    }
}

@Composable
private fun StaffMediaRolePagingContent(
    viewModel: StaffMediaRoleViewModel,
    scope: CoroutineScope,
    snackbar: SnackbarHostState,
    mediaComponentState: MediaComponentState
) {
    val pagingItems = viewModel.collectAsLazyPagingItems()

    LazyPagingList(
        type = ListPagingListType.GRID,
        gridOptions = GridOptions(GridCells.Adaptive(168.dp)),
        pagingItems = pagingItems,
        onRefresh = {
            viewModel.refresh()
        },
        span = { index ->
            val item = pagingItems[index]
            GridItemSpan(if (item is HeaderModel) maxLineSpan else 1)
        }
    ) { model ->
        when (model) {
            is HeaderModel -> {
                HeaderBox(header = model)
            }

            is MediaModel -> {
                StaffMediaRoleItem(
                    model = model,
                    onRoleClick = {
                        scope.launch {
                            snackbar.showSnackbar(
                                it,
                                withDismissAction = true,
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    mediaComponentState = mediaComponentState
                )
            }
        }
    }
}


@Composable
private fun StaffMediaRoleItem(
    model: MediaModel,
    onRoleClick: OnClickWithValue<String>,
    mediaComponentState: MediaComponentState
) {
    Card(
        modifier = Modifier
            .width(168.dp)
            .height(124.dp)
            .padding(6.dp)
    ) {
        MediaItemRowContent(
            media = model,
            content = {
                Box(modifier = Modifier.fillMaxHeight()) {
                    model.staffRole?.let {
                        LightText(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .clickable {
                                    onRoleClick(it)
                                },
                            text = it,
                            maxLines = 2
                        )
                    }
                }
            },
            mediaComponentState = mediaComponentState
        )
    }
}


@Composable
private fun StaffMediaRoleFilterBottomSheet(
    bottomSheetState: BottomSheetState,
    viewModel: StaffMediaRoleFilterViewModel,
    onFilter: (field: StaffMediaRoleField) -> Unit
) {
    val scope = rememberCoroutineScope()


    val dismiss: () -> Unit = {
        scope.launch {
            bottomSheetState.collapse()
        }
    }

    BottomSheet(state = bottomSheetState, skipPeeked = true) {
        StaffMediaRoleFilterBottomSheetContent(
            viewModel = viewModel,
            dismiss = dismiss,
            onFilter = onFilter
        )
    }
}

@Composable
private fun StaffMediaRoleFilterBottomSheetContent(
    viewModel: StaffMediaRoleFilterViewModel,
    dismiss: () -> Unit,
    onFilter: (field: StaffMediaRoleField) -> Unit
) {
    val field = viewModel.field
    Column(
        modifier = Modifier
            .padding(bottom = 4.dp)
    ) {
        BottomSheetConfirmation(
            confirmClicked = {
                onFilter(field)
                dismiss()
            },
            dismissClicked = {
                dismiss()
            }
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(vertical = 8.dp),
        ) {

            val selectedItem = field.sort.ordinal.takeIf { it < 6 } ?: 5
            SelectMenu(
                entries = stringArrayResource(id = R.array.staff_character_studio_media_sort_menu),
                selectedItemPosition = selectedItem
            ) {
                val selectedSort = if (it == 5) {
                    when (viewModel.titleType) {
                        MediaTitleModel.type_english -> 6
                        MediaTitleModel.type_native -> 7
                        else -> 5
                    }
                } else it
                field.sort = StaffMediaRoleSort.entries[selectedSort]
            }

            TextSwitch(
                title = stringResource(id = anilib.i18n.R.string.on_list),
                checked = field.onList,
                onCheckedChanged = {
                    field.onList = it
                }
            )
        }
    }
}
