package com.revolgenx.anilib.character.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import anilib.i18n.R as I18nR
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.m3.BottomSheet
import com.dokar.sheets.m3.BottomSheetDefaults
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.R
import com.revolgenx.anilib.character.data.field.CharacterMediaField
import com.revolgenx.anilib.character.data.field.CharacterMediaSort
import com.revolgenx.anilib.character.ui.viewmodel.CharacterMediaFilterViewModel
import com.revolgenx.anilib.character.ui.viewmodel.CharacterMediaViewModel
import com.revolgenx.anilib.common.ui.component.action.BottomSheetConfirmation
import com.revolgenx.anilib.common.ui.component.action.DisappearingFAB
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.menu.SelectMenu
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.toggle.TextSwitch
import com.revolgenx.anilib.common.ui.compose.paging.GridOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.media.ui.component.MediaItemColumnCard
import com.revolgenx.anilib.media.ui.component.MediaComponentState
import com.revolgenx.anilib.media.ui.component.rememberMediaComponentState
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterMediaScreen(characterId: Int) {
    val viewModel: CharacterMediaViewModel = koinViewModel()
    viewModel.field.characterId = characterId

    val filterBottomSheetState = rememberBottomSheetState()
    val filterViewModel: CharacterMediaFilterViewModel = koinViewModel()

    val navigator = localNavigator()
    val scope = rememberCoroutineScope()

    val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
    val bottomScrollConnection =
        remember { BottomNestedScrollConnection(state = scrollState) }

    ScreenScaffold(
        topBar = {},
        floatingActionButton = {
            DisappearingFAB(scrollState = scrollState, icon = AppIcons.IcFilter) {
                filterViewModel.field = viewModel.field.copy()
                scope.launch {
                    filterBottomSheetState.expand()
                }
            }
        },
        bottomNestedScrollConnection = bottomScrollConnection,
    ) {
        val mediaComponentState = rememberMediaComponentState(navigator = navigator)
        CharacterMediaPagingContent(viewModel, mediaComponentState)
        CharacterMediaFilterBottomSheet(
            bottomSheetState = filterBottomSheetState,
            viewModel = filterViewModel
        ) {
            viewModel.field = it
            viewModel.refresh()
        }
    }
}

@Composable
private fun CharacterMediaPagingContent(
    viewModel: CharacterMediaViewModel,
    mediaComponentState: MediaComponentState
) {
    val pagingItems = viewModel.collectAsLazyPagingItems()
    LazyPagingList(
        pagingItems = pagingItems,
        type = ListPagingListType.GRID,
        onRefresh = {
            viewModel.refresh()
        },
        gridOptions = GridOptions(GridCells.Adaptive(120.dp)),
    ) { model ->
        model ?: return@LazyPagingList
        MediaItemColumnCard(media = model, mediaComponentState = mediaComponentState)
    }
}


@Composable
private fun CharacterMediaFilterBottomSheet(
    bottomSheetState: BottomSheetState,
    viewModel: CharacterMediaFilterViewModel,
    onFilter: (field: CharacterMediaField) -> Unit
) {
    val scope = rememberCoroutineScope()


    val dismiss: () -> Unit = {
        scope.launch {
            bottomSheetState.collapse()
        }
    }

    BottomSheet(
        state = bottomSheetState,
        skipPeeked = true,
        behaviors = BottomSheetDefaults.dialogSheetBehaviors(navigationBarColor = BottomSheetDefaults.backgroundColor)
    ) {
        CharacterMediaScreenBottomSheetContent(
            viewModel = viewModel,
            dismiss = dismiss,
            onFilter = onFilter
        )
    }
}

@Composable
private fun CharacterMediaScreenBottomSheetContent(
    viewModel: CharacterMediaFilterViewModel,
    dismiss: () -> Unit,
    onFilter: (field: CharacterMediaField) -> Unit
) {
    val field = viewModel.field
    Column(
        modifier = Modifier
            .padding(bottom = 4.dp)
    ) {
        BottomSheetConfirmation(
            onConfirm = {
                onFilter(field)
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
                field.sort = CharacterMediaSort.entries[selectedSort]
            }

            TextSwitch(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = I18nR.string.on_list),
                checked = field.onList,
                onCheckedChanged = {
                    field.onList = it
                })
        }
    }
}