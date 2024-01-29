package com.revolgenx.anilib.studio.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import anilib.i18n.R
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.m3.BottomSheet
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.prettyNumberFormat
import com.revolgenx.anilib.common.ui.component.action.BottomSheetConfirmation
import com.revolgenx.anilib.common.ui.component.action.DisappearingFAB
import com.revolgenx.anilib.common.ui.component.action.OpenInBrowserOverflowMenu
import com.revolgenx.anilib.common.ui.component.action.OverflowMenu
import com.revolgenx.anilib.common.ui.component.action.ShareOverflowMenu
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.common.HeaderBox
import com.revolgenx.anilib.common.ui.component.menu.SelectMenu
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.toggle.TextSwitch
import com.revolgenx.anilib.common.ui.compose.paging.GridOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.ui.icons.appicon.IcHeart
import com.revolgenx.anilib.common.ui.icons.appicon.IcHeartOutline
import com.revolgenx.anilib.common.ui.model.HeaderModel
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.media.ui.component.MediaCard
import com.revolgenx.anilib.media.ui.component.MediaComponentState
import com.revolgenx.anilib.media.ui.component.rememberMediaComponentState
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import com.revolgenx.anilib.studio.data.field.StudioField
import com.revolgenx.anilib.studio.data.field.StudioMediaSort
import com.revolgenx.anilib.studio.ui.model.StudioModel
import com.revolgenx.anilib.studio.ui.viewmodel.StudioFilterViewModel
import com.revolgenx.anilib.studio.ui.viewmodel.StudioViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

class StudioScreen(private val studioId: Int) : AndroidScreen() {
    @Composable
    override fun Content() {
        StudioScreenContent(studioId)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StudioScreenContent(studioId: Int) {
    val viewModel: StudioViewModel = koinViewModel()
    viewModel.field.studioId = studioId
    val filterViewModel: StudioFilterViewModel = koinViewModel()

    val studio = stringResource(id = I18nR.string.studio)

    val navigator = localNavigator()
    val scope = rememberCoroutineScope()

    val filterBottomSheetState = rememberBottomSheetState()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val studioModel = viewModel.studio.value
    val context = localContext()


    val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
    val bottomScrollConnection =
        remember { BottomNestedScrollConnection(state = scrollState) }


    ScreenScaffold(
        title = studioModel?.name ?: studio,
        floatingActionButton = {
            DisappearingFAB(scrollState = scrollState, icon = AppIcons.IcFilter) {
                filterViewModel.field = viewModel.field.copy()
                scope.launch {
                    filterBottomSheetState.expand()
                }
            }
        },
        actions = {
            studioModel?.let { s ->
                FilledTonalButton(
                    onClick = {
                        viewModel.toggleFavorite()
                    }
                ) {
                    Text(
                        text = s.favourites?.prettyNumberFormat().orEmpty(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.size(2.dp))
                    Icon(
                        imageVector = if (s.isFavourite.value) AppIcons.IcHeart else AppIcons.IcHeartOutline,
                        contentDescription = null
                    )
                }
            }
            studioModel?.siteUrl?.let { site ->
                OverflowMenu {
                    OpenInBrowserOverflowMenu(link = site)
                    ShareOverflowMenu(text = site)
                }
            }
        },
        bottomNestedScrollConnection = bottomScrollConnection,
        scrollBehavior = scrollBehavior,
        contentWindowInsets = horizontalBottomWindowInsets()
    ) {snackbarHostState->
        LaunchedEffect(viewModel.showToggleErrorMsg.value) {
            if (viewModel.showToggleErrorMsg.value) {
                snackbarHostState.showSnackbar(context.getString(R.string.operation_failed), withDismissAction = true)
                viewModel.showToggleErrorMsg.value = false
            }
        }

        val mediaComponentState = rememberMediaComponentState(navigator = navigator)
        StudioPagingContent(viewModel, mediaCardState = mediaComponentState)

        StudioFilterBottomSheet(
            bottomSheetState = filterBottomSheetState,
            viewModel = filterViewModel
        ) {
            viewModel.field = it
            viewModel.refresh()
        }
    }
}

@Composable
private fun StudioPagingContent(
    viewModel: StudioViewModel,
    mediaCardState: MediaComponentState
) {
    val pagingItems = viewModel.collectAsLazyPagingItems()
    LazyPagingList(
        type = ListPagingListType.GRID,
        pagingItems = pagingItems,
        onRefresh = {
            viewModel.refresh()
        },
        span = { index ->
            val item = pagingItems[index]
            GridItemSpan(if (item is HeaderModel) maxLineSpan else 1)
        },
        gridOptions = GridOptions(GridCells.Adaptive(120.dp))
    ) { baseModel ->
        baseModel ?: return@LazyPagingList

        if (viewModel.studio.value == null && baseModel is MediaModel) {
            baseModel.studio?.let { s ->
                viewModel.studio.value = StudioModel(
                    id = s.id,
                    name = s.name,
                    siteUrl = s.siteUrl,
                    isFavourite = s.isFavourite,
                    favourites = s.favourites
                )
            }
        }

        when (baseModel) {
            is HeaderModel -> {
                HeaderBox(header = baseModel)
            }

            is MediaModel -> {
                MediaCard(media = baseModel, mediaComponentState = mediaCardState)
            }
        }
    }
}



@Composable
private fun StudioFilterBottomSheet(
    bottomSheetState: BottomSheetState,
    viewModel: StudioFilterViewModel,
    onFilter: (field: StudioField) -> Unit
) {
    val scope = rememberCoroutineScope()


    val dismiss: () -> Unit = {
        scope.launch {
            bottomSheetState.collapse()
        }
    }

    BottomSheet(state = bottomSheetState, skipPeeked = true) {
        StudioScreenBottomSheetContent(
            viewModel = viewModel,
            dismiss = dismiss,
            onFilter = onFilter
        )
    }
}

@Composable
private fun StudioScreenBottomSheetContent(
    viewModel: StudioFilterViewModel,
    dismiss: () -> Unit,
    onFilter: (field: StudioField) -> Unit
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
                entries = stringArrayResource(id = com.revolgenx.anilib.R.array.staff_character_studio_media_sort_menu),
                selectedItemPosition = selectedItem
            ) {
                val selectedSort = if (it == 5) {
                    when (viewModel.titleType) {
                        MediaTitleModel.type_english -> 6
                        MediaTitleModel.type_native -> 7
                        else -> 5
                    }
                } else it
                field.sort = StudioMediaSort.entries[selectedSort]
            }

            TextSwitch(
                title = stringResource(id = I18nR.string.on_list),
                checked = field.onList,
                onCheckedChanged = {
                    field.onList = it
                })
        }
    }
}