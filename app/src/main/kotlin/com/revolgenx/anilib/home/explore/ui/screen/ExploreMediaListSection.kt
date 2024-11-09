package com.revolgenx.anilib.home.explore.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import anilib.i18n.R
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.m3.BottomSheet
import com.dokar.sheets.m3.BottomSheetDefaults
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ext.maybeLocalSnackbarHostState
import com.revolgenx.anilib.common.ext.mediaListEntryEditorScreen
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.showLoginMsg
import com.revolgenx.anilib.common.ui.component.action.BottomSheetConfirmation
import com.revolgenx.anilib.common.ui.component.menu.SortMenuItem
import com.revolgenx.anilib.common.ui.component.menu.SortOrder
import com.revolgenx.anilib.common.ui.component.menu.SortSelectMenu
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.composition.localTabNavigator
import com.revolgenx.anilib.common.ui.composition.localUser
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcBook
import com.revolgenx.anilib.common.ui.icons.appicon.IcMedia
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.common.util.OnClickWithValue
import com.revolgenx.anilib.common.util.OnLongClickWithValue
import com.revolgenx.anilib.home.explore.component.ExploreMediaListCard
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreMediaListFilterViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreMediaListViewModel
import com.revolgenx.anilib.list.ui.model.MediaListModel
import com.revolgenx.anilib.list.ui.screen.AnimeListScreen
import com.revolgenx.anilib.list.ui.screen.MangaListScreen
import com.revolgenx.anilib.type.MediaListSort
import com.revolgenx.anilib.type.MediaType
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExploreMediaListSection(viewModel: ExploreMediaListViewModel) {
    val filterViewModel: ExploreMediaListFilterViewModel = koinViewModel()
    val tabNavigator = localTabNavigator()
    val filterBottomSheetState = rememberBottomSheetState()
    val scope = rememberCoroutineScope()
    val user = localUser()
    val isLoggedIn = user.isLoggedIn
    val navigator = localNavigator()
    val snackbar = localSnackbarHostState()
    val context = localContext()
    val openEditorScreen = viewModel.openEditorScreen.collectAsState()

    val type = viewModel.field.type
    ExploreMediaListHeader(
        type = type,
        filter = {
            scope.launch {
                filterViewModel.sort = viewModel.field.sort
                filterBottomSheetState.expand()
            }
        }) {
        tabNavigator.current = if (type == MediaType.MANGA) MangaListScreen else AnimeListScreen
    }


    val openMediaListEditorScreen: OnClickWithValue<MediaListModel> by rememberUpdatedState(newValue = { mediaList ->
        if (isLoggedIn) {
            navigator.mediaListEntryEditorScreen(mediaId = mediaList.mediaId)
        } else {
            snackbar.showLoginMsg(context = context, scope = scope)
        }
    })

    val onMediaClick: OnClickWithValue<MediaListModel> by rememberUpdatedState(newValue = { mediaList ->
        if (openEditorScreen.value == true) {
            openMediaListEditorScreen(mediaList)
        } else {
            navigator.mediaScreen(
                mediaId = mediaList.mediaId,
                type = mediaList.media?.type
            )
        }
    })

    val onMediaLongClick: OnLongClickWithValue<MediaListModel> by rememberUpdatedState(newValue = { mediaList ->
        if (openEditorScreen.value == true) {
            navigator.mediaScreen(
                mediaId = mediaList.mediaId,
                type = mediaList.media?.type
            )
        } else {
            openMediaListEditorScreen(mediaList)
        }
    })

    ExploreMediaListContent(viewModel, onClick = onMediaClick, onLongClick = onMediaLongClick)

    ResourceSaveSection(viewModel = viewModel, snackbar = snackbar)
    ExploreMediaListFilterBottomSheet(
        bottomSheetState = filterBottomSheetState,
        viewModel = filterViewModel,
        onDismiss = {
            scope.launch {
                filterBottomSheetState.collapse()
            }
        },
        onFilter = { sort ->
            scope.launch {
                viewModel.updateMediaListSort(sort)
                viewModel.refresh()
                filterBottomSheetState.collapse()
            }
        }
    )
}


@Composable
private fun ResourceSaveSection(
    viewModel: ExploreMediaListViewModel,
    snackbar: SnackbarHostState
) {
    when (viewModel.saveResource) {
        is ResourceState.Error -> {
            val failedToSave = stringResource(id = anilib.i18n.R.string.failed_to_save)
            LaunchedEffect(viewModel) {
                when (snackbar.showSnackbar(
                    failedToSave, duration = SnackbarDuration.Short
                )) {
                    SnackbarResult.Dismissed -> {
                        viewModel.saveResource = null
                    }

                    else -> {}
                }
            }
        }

        else -> {}
    }
}

@Composable
private fun ExploreMediaListFilterBottomSheet(
    bottomSheetState: BottomSheetState,
    viewModel: ExploreMediaListFilterViewModel,
    onDismiss: OnClick,
    onFilter: OnClickWithValue<MediaListSort?>
) {
    BottomSheet(
        state = bottomSheetState,
        skipPeeked = true,
        behaviors = BottomSheetDefaults.dialogSheetBehaviors(lightNavigationBar = true)
    ) {
        val mediaListSortList =
            stringArrayResource(id = com.revolgenx.anilib.R.array.media_list_sort)

        val currentSort = viewModel.sort


        var currentSortIndex: Int? = null
        var currentSortOrder: SortOrder = SortOrder.NONE

        if (currentSort != null) {
            val isDesc = currentSort.rawValue.endsWith("_DESC")
            currentSortIndex = (if (isDesc) currentSort.ordinal - 1 else currentSort.ordinal) / 2
            currentSortOrder = if (isDesc) SortOrder.DESC else SortOrder.ASC
        }

        val mediaListSortMenu = remember(currentSort) {
            mediaListSortList.mapIndexed { index, s ->
                SortMenuItem(
                    title = s,
                    order = if (index == currentSortIndex) currentSortOrder else SortOrder.NONE,
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(bottom = 4.dp)
        ) {
            BottomSheetConfirmation(
                onConfirm = {
                    onFilter(viewModel.sort)
                },
                onDismiss = onDismiss
            )


            SortSelectMenu(
                label = stringResource(id = R.string.sort),
                entries = mediaListSortMenu,
            ) { index, selectedSortItem ->
                var selectedSort: MediaListSort? = null
                if (selectedSortItem != null) {
                    val selectIndex = index * 2
                    selectedSort = if (selectedSortItem.order == SortOrder.DESC) {
                        MediaListSort.entries[selectIndex + 1]
                    } else {
                        MediaListSort.entries[selectIndex]
                    }
                }
                viewModel.sort = selectedSort
            }
        }
    }
}

@Composable
private fun ExploreMediaListContent(
    viewModel: ExploreMediaListViewModel,
    onClick: OnClickWithValue<MediaListModel>,
    onLongClick: OnLongClickWithValue<MediaListModel>
) {
    val pagingItems = viewModel.collectAsLazyPagingItems()

    Box(
        modifier = Modifier.height(ExploreMediaListCardHeight)
    ) {
        LazyPagingList(
            pagingItems = pagingItems,
            type = ListPagingListType.ROW,
            onPullRefresh = false
        ) { mediaList ->
            mediaList ?: return@LazyPagingList

            ExploreMediaListCard(
                list = mediaList,
                increaseProgress = {
                    viewModel.increaseProgress(mediaList)
                },
                onClick = {
                    onClick(mediaList)
                },
                onLongClick = {
                    onLongClick(mediaList)
                }
            )
        }
    }
}

@Composable
private fun ExploreMediaListHeader(type: MediaType, filter: OnClick, more: OnClick) {
    val header = when (type) {
        MediaType.MANGA -> stringResource(id = R.string.reading) to AppIcons.IcBook
        else -> stringResource(id = R.string.watching) to AppIcons.IcMedia
    }

    ExploreScreenHeader(text = header.first, icon = header.second, onFilter = filter, onMore = more)
}

val ExploreMediaListCardWidth = 160.dp
val ExploreMediaListCardHeight = 248.dp
