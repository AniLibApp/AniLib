package com.revolgenx.anilib.staff.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
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
import cafe.adriel.voyager.navigator.Navigator
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.m3.BottomSheet
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.characterScreen
import com.revolgenx.anilib.common.ext.emptyWindowInsets
import com.revolgenx.anilib.common.ui.component.action.BottomSheetConfirmation
import com.revolgenx.anilib.common.ui.component.action.DisappearingFAB
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.menu.SelectMenu
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.toggle.TextSwitch
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.media.ui.component.MediaComponentState
import com.revolgenx.anilib.media.ui.component.rememberMediaComponentState
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import com.revolgenx.anilib.staff.data.field.StaffMediaCharacterField
import com.revolgenx.anilib.staff.data.field.StaffMediaCharacterSort
import com.revolgenx.anilib.staff.ui.component.StaffMediaCharacterCard
import com.revolgenx.anilib.staff.ui.viewmodel.StaffMediaCharacterFilterViewModel
import com.revolgenx.anilib.staff.ui.viewmodel.StaffMediaCharacterViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffMediaCharacterScreen(viewModel: StaffMediaCharacterViewModel) {

    val filterViewModel: StaffMediaCharacterFilterViewModel = koinViewModel()
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
        val mediaComponentState = rememberMediaComponentState(navigator = navigator)
        StaffMediaCharacterPagingContent(viewModel, mediaComponentState, navigator)

        StaffMediaCharacterFilterBottomSheet(
            bottomSheetState = filterBottomSheetState,
            viewModel = filterViewModel
        ) {
            viewModel.field = it
            viewModel.refresh()
        }
    }
}

@Composable
private fun StaffMediaCharacterPagingContent(
    viewModel: StaffMediaCharacterViewModel,
    mediaComponentState: MediaComponentState,
    navigator: Navigator
) {
    val pagingItems = viewModel.collectAsLazyPagingItems()
    LazyPagingList(
        pagingItems = pagingItems,
        onRefresh = {
            viewModel.refresh()
        },
    ) { model ->
        model ?: return@LazyPagingList
        StaffMediaCharacterCard(
            mediaModel = model,
            character = model.character,
            mediaComponentState = mediaComponentState,
            onCharacterClick = {
                navigator.characterScreen(it)
            }
        )
    }
}


@Composable
private fun StaffMediaCharacterFilterBottomSheet(
    bottomSheetState: BottomSheetState,
    viewModel: StaffMediaCharacterFilterViewModel,
    onFilter: (field: StaffMediaCharacterField) -> Unit
) {
    val scope = rememberCoroutineScope()


    val dismiss: () -> Unit = {
        scope.launch {
            bottomSheetState.collapse()
        }
    }

    BottomSheet(state = bottomSheetState, skipPeeked = true) {
        StaffMediaCharacterFilterBottomSheetContent(
            viewModel = viewModel,
            dismiss = dismiss,
            onFilter = onFilter
        )
    }
}

@Composable
private fun StaffMediaCharacterFilterBottomSheetContent(
    viewModel: StaffMediaCharacterFilterViewModel,
    dismiss: () -> Unit,
    onFilter: (field: StaffMediaCharacterField) -> Unit
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
                field.sort = StaffMediaCharacterSort.entries[selectedSort]
            }

            TextSwitch(
                title = stringResource(id = anilib.i18n.R.string.on_list),
                checked = field.onList,
                onCheckedChanged = {
                    field.onList = it
                })
        }
    }
}