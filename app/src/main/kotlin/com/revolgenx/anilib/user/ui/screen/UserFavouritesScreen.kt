package com.revolgenx.anilib.user.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.app.ui.viewmodel.ScrollTarget
import com.revolgenx.anilib.app.ui.viewmodel.ScrollViewModel
import com.revolgenx.anilib.character.ui.component.CharacterCard
import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.common.ext.activityViewModel
import com.revolgenx.anilib.common.ext.animateScrollToItem
import com.revolgenx.anilib.common.ext.characterScreen
import com.revolgenx.anilib.common.ext.hideBottomSheet
import com.revolgenx.anilib.common.ext.staffScreen
import com.revolgenx.anilib.common.ext.studioScreen
import com.revolgenx.anilib.common.ui.component.action.DisappearingFAB
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.radio.TextRadioButton
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.compose.paging.GridOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.media.ui.component.MediaItemColumnCard
import com.revolgenx.anilib.media.ui.component.rememberMediaComponentState
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.staff.ui.component.StaffCard
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.studio.ui.component.StudioItem
import com.revolgenx.anilib.studio.ui.model.StudioModel
import com.revolgenx.anilib.user.data.field.UserFavouriteType
import com.revolgenx.anilib.user.ui.viewmodel.UserFavouriteContentViewModel
import com.revolgenx.anilib.user.ui.viewmodel.UserFavouriteViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.core.qualifier.named
import anilib.i18n.R as I18nR


private typealias UserFavouriteScreenPage = PagerScreen<UserFavouriteType>

val pages = listOf(
    UserFavouriteScreenPage(UserFavouriteType.FAVOURITE_ANIME, I18nR.string.anime),
    UserFavouriteScreenPage(UserFavouriteType.FAVOURITE_MANGA, I18nR.string.manga),
    UserFavouriteScreenPage(UserFavouriteType.CHARACTER, I18nR.string.character),
    UserFavouriteScreenPage(UserFavouriteType.STAFF, I18nR.string.staff),
    UserFavouriteScreenPage(UserFavouriteType.STUDIO, I18nR.string.studio),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserFavouritesScreen(
    userId: Int?
) {
    val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
    val bottomNestedScrollConnection =
        remember { BottomNestedScrollConnection(state = scrollState) }

    val openFavouriteTypeFilter = rememberSaveable { mutableStateOf(false) }


    val viewModel: UserFavouriteViewModel = koinViewModel()
    val favAnimeViewModel: UserFavouriteContentViewModel =
        koinViewModel(named(UserFavouriteType.FAVOURITE_ANIME))
    val favMangaViewModel: UserFavouriteContentViewModel =
        koinViewModel(named(UserFavouriteType.FAVOURITE_MANGA))
    val favCharacterViewModel: UserFavouriteContentViewModel =
        koinViewModel(named(UserFavouriteType.CHARACTER))
    val favStaffViewModel: UserFavouriteContentViewModel =
        koinViewModel(named(UserFavouriteType.STAFF))
    val favStudioViewModel: UserFavouriteContentViewModel =
        koinViewModel(named(UserFavouriteType.STUDIO))
    val scrollViewModel: ScrollViewModel = activityViewModel()

    favAnimeViewModel.field.userId = userId
    favMangaViewModel.field.userId = userId
    favCharacterViewModel.field.userId = userId
    favStaffViewModel.field.userId = userId
    favStudioViewModel.field.userId = userId

    ScreenScaffold(
        floatingActionButton = {
            DisappearingFAB(
                scrollState = scrollState,
                content = {
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(text = stringResource(id = viewModel.favouriteType.value.res))
                    }
                },
                onClick = {
                    openFavouriteTypeFilter.value = true
                }
            )
        },
        topBar = {},
        navigationIcon = {},
        actions = {},
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(bottomNestedScrollConnection)
        ) {
            val contentViewModel = when (viewModel.favouriteType.value) {
                UserFavouriteType.FAVOURITE_ANIME -> favAnimeViewModel
                UserFavouriteType.FAVOURITE_MANGA -> favMangaViewModel
                UserFavouriteType.CHARACTER -> favCharacterViewModel
                UserFavouriteType.STAFF -> favStaffViewModel
                UserFavouriteType.STUDIO -> favStudioViewModel
            }
            UserFavouritePageScreen(contentViewModel, scrollViewModel)
            FavouriteTypeFilterBottomSheet(
                openBottomSheet = openFavouriteTypeFilter,
                selectedFavouriteType = viewModel.favouriteType.value
            ) {
                viewModel.favouriteType.value = it
            }
        }
    }
}

@Composable
private fun UserFavouritePageScreen(
    viewModel: UserFavouriteContentViewModel,
    scrollViewModel: ScrollViewModel
) {
    val pagingItems = viewModel.collectAsLazyPagingItems()
    val navigator = localNavigator()
    val mediaComponentState = rememberMediaComponentState(navigator = navigator)

    val isColumn = viewModel.field.type == UserFavouriteType.STUDIO
    val scrollableState = if(isColumn) rememberLazyListState() else rememberLazyGridState()

    LaunchedEffect(scrollViewModel) {
        scrollViewModel.scrollEventFor(ScrollTarget.USER).collectLatest {
            scrollableState.animateScrollToItem(0)
        }
    }

    LazyPagingList(
        pagingItems = pagingItems,
        type = if (isColumn) ListPagingListType.COLUMN else ListPagingListType.GRID,
        scrollState = scrollableState,
        onRefresh = {
            viewModel.refresh()
        },
        gridOptions = GridOptions(GridCells.Adaptive(120.dp)),
    ) { favModel ->
        when (favModel) {
            is MediaModel -> {
                MediaItemColumnCard(favModel, mediaComponentState = mediaComponentState)
            }

            is CharacterModel -> {
                CharacterCard(favModel) {
                    navigator.characterScreen(it)
                }
            }

            is StaffModel -> {
                StaffCard(favModel) {
                    navigator.staffScreen(it)
                }
            }

            is StudioModel -> {
                StudioItem(favModel) {
                    navigator.studioScreen(it)
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavouriteTypeFilterBottomSheet(
    openBottomSheet: MutableState<Boolean> = mutableStateOf(false),
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    selectedFavouriteType: UserFavouriteType,
    onFavouriteTypeSelected: (type: UserFavouriteType) -> Unit
) {
    val scope = rememberCoroutineScope()

    if (openBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet.value = false },
            sheetState = bottomSheetState
        ) {
            Column(
                modifier = Modifier
                    .selectableGroup()
                    .verticalScroll(rememberScrollState())
            ) {
                UserFavouriteType.values().forEach { type ->
                    TextRadioButton(
                        text = stringResource(type.res),
                        selected = type == selectedFavouriteType
                    ) {
                        onFavouriteTypeSelected(type)
                        scope.hideBottomSheet(bottomSheetState, openBottomSheet)
                    }
                }
            }
        }
    }
}