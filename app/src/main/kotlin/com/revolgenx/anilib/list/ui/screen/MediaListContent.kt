package com.revolgenx.anilib.list.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.constant.AlMediaSort
import com.revolgenx.anilib.common.data.tuples.to
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.action.BottomSheetConfirmationAction
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.menu.AlSortMenuItem
import com.revolgenx.anilib.common.ui.component.menu.AlSortOrder
import com.revolgenx.anilib.common.ui.component.menu.MultiSelectMenu
import com.revolgenx.anilib.common.ui.component.menu.SelectMenu
import com.revolgenx.anilib.common.ui.component.menu.SortSelectMenu
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.screen.ResourceScreen
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.list.data.filter.MediaListCollectionFilter
import com.revolgenx.anilib.list.data.sort.MediaListSortType
import com.revolgenx.anilib.list.ui.model.MediaListModel
import com.revolgenx.anilib.list.ui.viewmodel.MediaListFilterViewModel
import com.revolgenx.anilib.list.ui.viewmodel.MediaListViewModel
import com.revolgenx.anilib.media.ui.model.toMediaFormat
import com.revolgenx.anilib.media.ui.model.toMediaStatus
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaListContent(
    viewModel: MediaListViewModel,
    openFilterBottomSheet: MutableState<Boolean> = mutableStateOf(false)
) {
    LaunchedEffect(viewModel) {
        viewModel.getResource()
    }
    val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
    val bottomNestedScrollConnection =
        remember { BottomNestedScrollConnection(state = scrollState) }
    val openGroupNameBottomSheet = rememberSaveable { mutableStateOf(false) }
    val groupNameBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val filterBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ScreenScaffold(
        topBar = {},
        contentWindowInsets = WindowInsets(0),
        floatingActionButton = {
            AnimatedVisibility(
                visible = scrollState.value == ScrollState.ScrollDown,
                enter = fadeIn() + expandIn { IntSize(1, 1) }
            ) {
                FloatingActionButton(
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                    onClick = {
                        openGroupNameBottomSheet.value = true
                    }) {
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(text = viewModel.currentGroupName)
                    }
                }
            }
        }
    ) {
        ResourceScreen(
            resourceState = viewModel.resource.value,
            refresh = { viewModel.refresh() }) {
            Box(
                modifier = Modifier.nestedScroll(bottomNestedScrollConnection),
            ) {
                LazyPagingList(
                    items = viewModel.mediaListCollection,
                    onRefresh = {
                        viewModel.refresh()
                    }
                ) { mediaList ->
                    mediaList ?: return@LazyPagingList
                    MediaListItem(list = mediaList)
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
        openBottomSheet = openFilterBottomSheet,
        bottomSheetState = filterBottomSheetState,
        viewModel = koinViewModel(),
        filter = viewModel.filter
    ) {
        viewModel.updateFilter(it)
    }

}


@Composable
private fun MediaListItem(list: MediaListModel) {
    val media = list.media ?: return
    Box(modifier = Modifier.padding(16.dp)) {
        Text(text = media.title?.userPreferred.naText())
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
            sheetState = bottomSheetState,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            MediaListGroupNameContent(
                groupNamesWithCount = groupNamesWithCount,
                selectedGroupName = selectedGroupName,
                onGroupNameSelected = {
                    onGroupNameSelected(it)
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            openBottomSheet.value = false
                        }
                    }
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
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (selectedGroupName == k),
                        onClick = { onGroupNameSelected(k) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (selectedGroupName == k),
                    onClick = null // null recommended for accessibility with screenreaders
                )
                Text(
                    text = "$k $v",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MediaListFilterBottomSheet(
    openBottomSheet: MutableState<Boolean> = mutableStateOf(false),
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    filter: MediaListCollectionFilter?,
    viewModel: MediaListFilterViewModel = koinViewModel(),
    onFilter: (filter: MediaListCollectionFilter) -> Unit
) {
    val scope = rememberCoroutineScope()

    if (openBottomSheet.value) {
        if (filter == null) {
            openBottomSheet.value = false
            return
        }
        viewModel.filter = filter

        ModalBottomSheet(
            onDismissRequest = { openBottomSheet.value = false },
            sheetState = bottomSheetState,
            containerColor = MaterialTheme.colorScheme.background
        ) {

            MediaListFilterBottomSheetContent(
                viewModel = viewModel,
                dismiss = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            openBottomSheet.value = false
                        }
                    }
                }
            ) {
                onFilter(it)
            }
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
            .padding(horizontal = 16.dp)
            .padding(bottom = 4.dp)
    ) {
        BottomSheetConfirmationAction(
            onPositiveClicked = {
                onFilter.invoke(viewModel.filter)
                dismiss.invoke()
            },
            onNegativeClicked = {
                dismiss.invoke()
            }
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            val selectedFormats = viewModel.filter.formatsIn?.map { it.ordinal } ?: emptyList()
            val formats = stringArrayResource(id = R.array.media_format)
            MultiSelectMenu(
                label = stringResource(id = R.string.format),
                entries = formats.mapIndexed { index, s ->
                    selectedFormats.contains(
                        index
                    ) to s
                },
            ) { selectedItems ->
                viewModel.filter = viewModel.filter.copy(
                    formatsIn = selectedItems.takeIf { it.isNotEmpty() }
                        ?.mapNotNull { it.first.toMediaFormat() }
                )
            }
            SelectMenu(
                label = stringResource(id = R.string.status),
                entries = stringArrayResource(id = R.array.media_status).toList(),
                selectedItemPosition = viewModel.filter.status?.ordinal,
                showNoneItem = true
            ) { selectedItem ->
                viewModel.filter = viewModel.filter.copy(
                    status = selectedItem.takeIf { it > -1 }?.toMediaStatus()
                )
            }

            val genreList = stringArrayResource(id = R.array.media_genre).toList()
            SelectMenu(
                label = stringResource(id = R.string.genre),
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
            var selectedSortOrder: AlSortOrder = AlSortOrder.NONE

            if (sort != null) {
                val isDesc = sort.toString().endsWith("_DESC")
                selectedSortIndex = sort.ordinal / 2
                selectedSortOrder = if (isDesc) AlSortOrder.DESC else AlSortOrder.ASC
            }

            val sortMenus =
                stringArrayResource(id = R.array.media_list_collection_sort).mapIndexed { index, s ->
                    AlSortMenuItem(
                        s,
                        if (index == selectedSortIndex) selectedSortOrder else AlSortOrder.NONE
                    )
                }

            SortSelectMenu(
                label = stringResource(id = R.string.sort),
                entries = sortMenus,
            ) { index, selectedItem ->
                var mediaListSortType: MediaListSortType? = null

                if (selectedItem != null) {
                    val alMediaSort = AlMediaSort.values()[index].sort
                    val selectedSort = if (selectedItem.order == AlSortOrder.DESC) {
                        alMediaSort + 1
                    } else {
                        alMediaSort
                    }
                    mediaListSortType = MediaListSortType.values()[selectedSort]
                }

                viewModel.filter = viewModel.filter.copy(
                    sort = mediaListSortType
                )
            }
        }

    }
}