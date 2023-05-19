package com.revolgenx.anilib.list.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.screen.ResourceScreen
import com.revolgenx.anilib.list.ui.model.MediaListModel
import com.revolgenx.anilib.list.ui.viewmodel.MediaListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaListContent(viewModel: MediaListViewModel) {
    LaunchedEffect(viewModel) {
        viewModel.getResource()
    }
    val loading = remember { mutableStateOf(false) }
    val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
    val bottomNestedScrollConnection =
        remember { BottomNestedScrollConnection(state = scrollState) }
    val openBottomSheet = rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
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
                        openBottomSheet.value = true
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
            loading = loading,
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
        openBottomSheet = openBottomSheet,
        bottomSheetState = bottomSheetState,
        selectedGroupName = viewModel.currentGroupNameWithCount.first,
        groupNamesWithCount = viewModel.groupNamesWithCount
    ) { groupName ->
        viewModel.updateCurrentGroupName(groupName)
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
private fun MediaListGroupNameBottomSheet(
    openBottomSheet: MutableState<Boolean> = mutableStateOf(false),
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    groupNamesWithCount: Map<String, Int>,
    selectedGroupName: String,
    onGroupNameSelected: (groupName: String) -> Unit
) {
    val scope = rememberCoroutineScope()

    if (openBottomSheet.value) {
        val windowInsets = NavigationBarDefaults.windowInsets

        ModalBottomSheet(
            onDismissRequest = { openBottomSheet.value = false },
            sheetState = bottomSheetState,
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            MediaListGroupNameContent(
                modifier = Modifier.windowInsetsPadding(windowInsets),
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