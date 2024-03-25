package com.revolgenx.anilib.media.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
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
import androidx.compose.ui.unit.dp
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.m3.BottomSheet
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.R
import com.revolgenx.anilib.character.ui.component.CharacterStaffCard
import com.revolgenx.anilib.character.ui.model.CharacterEdgeModel
import com.revolgenx.anilib.common.ext.characterScreen
import com.revolgenx.anilib.common.ext.emptyWindowInsets
import com.revolgenx.anilib.common.ext.staffScreen
import com.revolgenx.anilib.common.ui.component.action.BottomSheetConfirmation
import com.revolgenx.anilib.common.ui.component.action.DisappearingFAB
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.menu.SelectMenu
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.compose.paging.GridOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.media.ui.viewmodel.MediaCharacterFilterViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaCharacterViewModel
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.StaffLanguage
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaCharacterScreen(
    viewModel: MediaCharacterViewModel,
    mediaType: MediaType
) {
    val filterBottomSheetState = rememberBottomSheetState()
    val filterViewModel: MediaCharacterFilterViewModel = koinViewModel()

    val scope = rememberCoroutineScope()
    val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
    val bottomScrollConnection =
        remember { BottomNestedScrollConnection(state = scrollState) }

    ScreenScaffold(
        topBar = {},
        floatingActionButton = {
            if(mediaType == MediaType.ANIME){
                DisappearingFAB(scrollState = scrollState, icon = AppIcons.IcFilter) {
                    filterViewModel.staffLanguage = viewModel.field.language
                    scope.launch {
                        filterBottomSheetState.expand()
                    }
                }
            }
        },
        bottomNestedScrollConnection = bottomScrollConnection,
    ) {
        val pagingItems = viewModel.collectAsLazyPagingItems()
        LazyPagingList(
            pagingItems = pagingItems,
            pullRefresh = true,
            onRefresh = {
                viewModel.refresh()
            },
            type = if(mediaType == MediaType.MANGA) ListPagingListType.GRID else ListPagingListType.COLUMN,
            gridOptions = GridOptions(GridCells.Adaptive(168.dp))
            ) { characterEdgeModel ->
            characterEdgeModel ?: return@LazyPagingList
            MediaCharacterItem(characterEdgeModel)
        }

        MediaCharacterFilterBottomSheet(
            bottomSheetState = filterBottomSheetState,
            viewModel = filterViewModel,
            onFilter = {
                viewModel.field = viewModel.field.copy(language = filterViewModel.staffLanguage)
                viewModel.refresh()
            },
            onDismiss = {
                scope.launch {
                    filterBottomSheetState.collapse()
                }
            })
    }


}

@Composable
private fun MediaCharacterItem(
    characterEdgeModel: CharacterEdgeModel
) {
    val character = characterEdgeModel.node ?: return
    val voiceActors = characterEdgeModel.voiceActors
    val navigator = LocalMainNavigator.current
    CharacterStaffCard(
        character = character,
        characterRole = characterEdgeModel.role,
        staff = voiceActors?.firstOrNull(),
        onCharacterClick = { navigator.characterScreen(it) },
        onStaffClick = { navigator.staffScreen(it) }
    )
}


@Composable
private fun MediaCharacterFilterBottomSheet(
    bottomSheetState: BottomSheetState,
    viewModel: MediaCharacterFilterViewModel,
    onFilter: OnClick,
    onDismiss: OnClick
) {
    BottomSheet(state = bottomSheetState, skipPeeked = true) {
        Column(
            modifier = Modifier
                .padding(bottom = 4.dp)
        ) {
            BottomSheetConfirmation(
                confirmClicked = {
                    onFilter()
                    onDismiss()
                },
                dismissClicked = {
                    onDismiss()
                }
            )

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp),
            ) {
                SelectMenu(
                    entries = stringArrayResource(id = R.array.staff_language_menu),
                    selectedItemPosition = viewModel.staffLanguage.ordinal
                ) {
                    viewModel.staffLanguage = StaffLanguage.entries[it]
                }
            }
        }
    }
}