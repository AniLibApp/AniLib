package com.revolgenx.anilib.list.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.m3.BottomSheet
import com.dokar.sheets.m3.BottomSheetDefaults
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.constant.AlMediaSort
import com.revolgenx.anilib.common.data.store.MediaListDisplayMode
import com.revolgenx.anilib.common.data.tuples.to
import com.revolgenx.anilib.common.ext.hideBottomSheet
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ext.mediaListEntryEditorScreen
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.showLoginMsg
import com.revolgenx.anilib.common.ui.component.action.BottomSheetConfirmation
import com.revolgenx.anilib.common.ui.component.action.DisappearingFAB
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.menu.MultiSelectMenu
import com.revolgenx.anilib.common.ui.component.menu.MultiSelectModel
import com.revolgenx.anilib.common.ui.component.menu.SelectMenu
import com.revolgenx.anilib.common.ui.component.menu.SortMenuItem
import com.revolgenx.anilib.common.ui.component.menu.SortOrder
import com.revolgenx.anilib.common.ui.component.menu.SortSelectMenu
import com.revolgenx.anilib.common.ui.component.radio.TextRadioButton
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.compose.paging.GridOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.LocalUserState
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.composition.localUser
import com.revolgenx.anilib.common.ui.screen.state.EmptyScreen
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.list.data.filter.MediaListCollectionFilter
import com.revolgenx.anilib.list.data.sort.MediaListSortType
import com.revolgenx.anilib.list.ui.component.MediaListColumnCard
import com.revolgenx.anilib.list.ui.component.MediaListCompactColumnCard
import com.revolgenx.anilib.list.ui.component.MediaListRowCard
import com.revolgenx.anilib.list.ui.component.MediaListSmallRowCard
import com.revolgenx.anilib.list.ui.viewmodel.MediaListFilterViewModel
import com.revolgenx.anilib.list.ui.viewmodel.MediaListViewModel
import com.revolgenx.anilib.media.ui.model.toMediaStatus
import com.revolgenx.anilib.type.MediaFormat
import kotlinx.coroutines.launch
import anilib.i18n.R as I18nR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaListContent(
    viewModel: MediaListViewModel,
    filterViewModel: MediaListFilterViewModel,
    bottomSheetState: BottomSheetState
) {
    val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
    val bottomNestedScrollConnection =
        remember { BottomNestedScrollConnection(state = scrollState) }
    val openGroupNameBottomSheet = rememberSaveable { mutableStateOf(false) }
    val groupNameBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    LaunchedEffect(viewModel) {
        viewModel.getResource()
    }
    val navigator = localNavigator()
    val scope = rememberCoroutineScope()
    val context = localContext()
    val localUser = localUser()
    val isLoggedIn = localUser.isLoggedIn

    val snackbar = localSnackbarHostState()

    ScreenScaffold(
        topBar = {},
        floatingActionButton = {
            DisappearingFAB(
                scrollState = scrollState,
                content = {
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(text = viewModel.currentGroupName)
                    }
                }) {
                openGroupNameBottomSheet.value = true
            }
        }
    ) {
        ResourceScreen(
            viewModel = viewModel,
            showRetry = false
        ) {
            Box(
                modifier = Modifier.nestedScroll(bottomNestedScrollConnection),
            ) {
                val openEditorScreen = viewModel.openMediaListEntryEditor.collectAsState()
                val displayModeState = if (viewModel.isLoggedInUserList) {
                    viewModel.displayMode.collectAsState()
                } else {
                    viewModel.otherDisplayMode.collectAsState()
                }
                val displayMode = MediaListDisplayMode.fromValue(displayModeState.value!!)
                val gridOptions = when (displayMode) {
                    MediaListDisplayMode.GRID, MediaListDisplayMode.GRID_COMPACT -> {
                        GridOptions(GridCells.Adaptive(168.dp))
                    }

                    else -> null
                }
                val mediaListCollection = viewModel.mediaListCollection

                if (mediaListCollection.isEmpty()) {
                    EmptyScreen()
                } else {
                    LazyPagingList(
                        items = mediaListCollection,
                        type = if (gridOptions != null) ListPagingListType.GRID else ListPagingListType.COLUMN,
                        gridOptions = gridOptions,
                        onRefresh = {
                            viewModel.refresh()
                        }
                    ) { mediaList ->
                        mediaList ?: return@LazyPagingList

                        val openMediaListEditorScreen = {
                            if (isLoggedIn) {
                                navigator.mediaListEntryEditorScreen(mediaId = mediaList.mediaId)
                            } else {
                                snackbar.showLoginMsg(context = context, scope = scope)
                            }
                        }

                        val onMediaClick = {
                            if (openEditorScreen.value == true) {
                                openMediaListEditorScreen()
                            } else {
                                navigator.mediaScreen(
                                    mediaId = mediaList.mediaId,
                                    type = mediaList.media?.type
                                )
                            }
                        }

                        val onMediaLongClick = {
                            if (openEditorScreen.value == true) {
                                navigator.mediaScreen(
                                    mediaId = mediaList.mediaId,
                                    type = mediaList.media?.type
                                )
                            } else {
                                openMediaListEditorScreen()
                            }
                        }

                        val increaseProgress = {
                            viewModel.increaseProgress(mediaList)
                        }

                        when (displayMode) {
                            MediaListDisplayMode.LIST -> {
                                MediaListRowCard(
                                    list = mediaList,
                                    showIncreaseButton = viewModel.isLoggedInUserList,
                                    increaseProgress = increaseProgress,
                                    onClick = onMediaClick,
                                    onLongClick = onMediaLongClick
                                )
                            }

                            MediaListDisplayMode.LIST_COMPACT -> {
                                MediaListSmallRowCard(
                                    list = mediaList,
                                    showIncreaseButton = viewModel.isLoggedInUserList,
                                    increaseProgress = increaseProgress,
                                    onClick = onMediaClick,
                                    onLongClick = onMediaLongClick
                                )
                            }

                            MediaListDisplayMode.GRID -> {
                                MediaListColumnCard(
                                    list = mediaList,
                                    showIncreaseButton = viewModel.isLoggedInUserList,
                                    increaseProgress = increaseProgress,
                                    onClick = onMediaClick,
                                    onLongClick = onMediaLongClick
                                )
                            }

                            MediaListDisplayMode.GRID_COMPACT -> {
                                MediaListCompactColumnCard(
                                    list = mediaList,
                                    showIncreaseButton = viewModel.isLoggedInUserList,
                                    increaseProgress = increaseProgress,
                                    onClick = onMediaClick,
                                    onLongClick = onMediaLongClick
                                )
                            }
                        }
                    }
                }
            }

        }
    }


    MediaListGroupNameBottomSheet(
        openBottomSheet = openGroupNameBottomSheet,
        bottomSheetState = groupNameBottomSheetState,
        selectedGroupName = viewModel.currentGroupNameWithCount.first,
        groupNamesWithCount = viewModel.groupNamesWithCount
    ) { groupName ->
        viewModel.updateCurrentGroupName(groupName)
    }

    MediaListFilterBottomSheet(
        bottomSheetState = bottomSheetState,
        viewModel = filterViewModel,
    ) {
        viewModel.updateFilter(it)
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaListGroupNameBottomSheet(
    openBottomSheet: MutableState<Boolean> = mutableStateOf(false),
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    groupNamesWithCount: Map<String, Int>,
    selectedGroupName: String,
    onGroupNameSelected: (groupName: String) -> Unit
) {
    val scope = rememberCoroutineScope()

    if (openBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet.value = false },
            sheetState = bottomSheetState
        ) {
            MediaListGroupNameContent(
                groupNamesWithCount = groupNamesWithCount,
                selectedGroupName = selectedGroupName,
                onGroupNameSelected = {
                    onGroupNameSelected(it)
                    scope.hideBottomSheet(bottomSheetState, openBottomSheet)
                }
            )
        }
    }
}

@Composable
private fun MediaListGroupNameContent(
    modifier: Modifier = Modifier,
    groupNamesWithCount: Map<String, Int>,
    selectedGroupName: String,
    onGroupNameSelected: (groupName: String) -> Unit
) {
    Column(
        modifier
            .selectableGroup()
            .verticalScroll(rememberScrollState())
    ) {
        groupNamesWithCount.forEach { (k, v) ->
            TextRadioButton(text = "$k $v", selected = selectedGroupName == k) {
                onGroupNameSelected(k)
            }
        }
    }
}

@Composable
private fun MediaListFilterBottomSheet(
    bottomSheetState: BottomSheetState,
    viewModel: MediaListFilterViewModel,
    onFilter: (filter: MediaListCollectionFilter) -> Unit
) {
    val scope = rememberCoroutineScope()

    BottomSheet(
        state = bottomSheetState,
        skipPeeked = true,
        behaviors = BottomSheetDefaults.dialogSheetBehaviors(navigationBarColor = BottomSheetDefaults.backgroundColor)
    ) {
        MediaListFilterBottomSheetContent(
            viewModel = viewModel,
            dismiss = {
                scope.launch {
                    bottomSheetState.collapse()
                }
            }
        ) {
            onFilter(it)
        }
    }
}

@Composable
fun MediaListFilterBottomSheetContent(
    modifier: Modifier = Modifier,
    viewModel: MediaListFilterViewModel,
    dismiss: OnClick,
    onFilter: (filter: MediaListCollectionFilter) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(bottom = 4.dp)
    ) {
        BottomSheetConfirmation(
            onConfirm = {
                onFilter.invoke(viewModel.filter)
                dismiss()
            },
            onDismiss = {
                dismiss()
            }
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            val selectedFormats = viewModel.filter.formatsIn?.map { it.ordinal }.orEmpty()
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
                viewModel.filter = viewModel.filter.copy(
                    formatsIn = selectedItems.takeIf { it.isNotEmpty() }
                        ?.mapNotNull { MediaFormat.entries.getOrNull(it.first) }
                )
            }
            SelectMenu(
                label = stringResource(id = I18nR.string.status),
                entries = stringArrayResource(id = R.array.media_status),
                selectedItemPosition = viewModel.filter.status?.ordinal,
                showNoneItem = true
            ) { selectedItem ->
                viewModel.filter = viewModel.filter.copy(
                    status = selectedItem.takeIf { it > -1 }?.toMediaStatus()
                )
            }

            val genreList = stringArrayResource(id = R.array.media_genre)
            SelectMenu(
                label = stringResource(id = I18nR.string.genre),
                entries = genreList,
                selectedItemPosition = genreList.indexOf(viewModel.filter.genre),
                showNoneItem = true
            ) { selectedItem ->
                viewModel.filter = viewModel.filter.copy(
                    genre = selectedItem.takeIf { it > -1 }?.let { genreList[it] }
                )
            }

            val sort = viewModel.filter.sort
            var selectedSortIndex: Int? = null
            var selectedSortOrder: SortOrder = SortOrder.NONE

            if (sort != null) {
                val isDesc = sort.toString().endsWith("_DESC")
                selectedSortIndex = sort.ordinal / 2
                selectedSortOrder = if (isDesc) SortOrder.DESC else SortOrder.ASC
            }

            val sortMenus =
                stringArrayResource(id = R.array.media_list_collection_sort).mapIndexed { index, s ->
                    SortMenuItem(
                        s,
                        if (index == selectedSortIndex) selectedSortOrder else SortOrder.NONE
                    )
                }

            SortSelectMenu(
                label = stringResource(id = I18nR.string.sort),
                entries = sortMenus,
            ) { index, selectedItem ->
                var mediaListSortType: MediaListSortType? = null

                if (selectedItem != null) {
                    val alMediaSort = AlMediaSort.entries[index].sort
                    val selectedSort = if (selectedItem.order == SortOrder.DESC) {
                        alMediaSort + 1
                    } else {
                        alMediaSort
                    }
                    mediaListSortType = MediaListSortType.entries.toTypedArray()[selectedSort]
                }

                viewModel.filter = viewModel.filter.copy(
                    sort = mediaListSortType
                )
            }
        }

    }
}