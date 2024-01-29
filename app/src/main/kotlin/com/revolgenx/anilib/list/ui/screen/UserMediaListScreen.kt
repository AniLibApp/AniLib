package com.revolgenx.anilib.list.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.component.search.RowDockedSearchBar
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcBook
import com.revolgenx.anilib.common.ui.icons.appicon.IcCancel
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.ui.icons.appicon.IcMedia
import com.revolgenx.anilib.common.ui.icons.appicon.IcSearch
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.list.ui.viewmodel.AnimeListFilterViewModel
import com.revolgenx.anilib.list.ui.viewmodel.AnimeListViewModel
import com.revolgenx.anilib.list.ui.viewmodel.MangaListFilterViewModel
import com.revolgenx.anilib.list.ui.viewmodel.MangaListViewModel
import com.revolgenx.anilib.list.ui.viewmodel.MediaListFilterViewModel
import com.revolgenx.anilib.list.ui.viewmodel.MediaListViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

class UserMediaListScreen(
    private val userId: Int,
    private var mangaTab: Boolean
) :
    AndroidScreen() {
    @Composable
    override fun Content() {
        UserMediaListScreenContent(userId, mangaTab)
        mangaTab = false
    }
}

private typealias MediaListScreenPage = PagerScreen<MediaListScreenPageType>

private enum class MediaListScreenPageType {
    ANIME,
    MANGA,
}

private val pages = listOf(
    MediaListScreenPage(MediaListScreenPageType.ANIME, I18nR.string.anime, AppIcons.IcMedia),
    MediaListScreenPage(MediaListScreenPageType.MANGA, I18nR.string.manga, AppIcons.IcBook),
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun UserMediaListScreenContent(userId: Int, mangaTab: Boolean) {
    val pagerState = rememberPagerState() { pages.size }
    if (mangaTab) {
        LaunchedEffect(userId) {
            pagerState.scrollToPage(1)
        }
    }

    val animeSearchBar = rememberSaveable { mutableStateOf(false) }
    val mangaSearchBar = rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()


    val animeListViewModel: AnimeListViewModel = koinViewModel()
    val mangaListViewModel: MangaListViewModel = koinViewModel()

    val animeListFilterViewModel: AnimeListFilterViewModel = koinViewModel()
    val mangaListFilterViewModel: MangaListFilterViewModel = koinViewModel()

    val filterBottomSheetState = rememberBottomSheetState()

    animeListViewModel.field.userId = userId
    mangaListViewModel.field.userId = userId

    PagerScreenScaffold(
        pagerState = pagerState,
        pages = pages,
        actions = {
            ActionMenu(icon = AppIcons.IcSearch) {
                when (pagerState.currentPage) {
                    0 -> animeSearchBar.value = !animeSearchBar.value
                    1 -> mangaSearchBar.value = !mangaSearchBar.value
                }
            }
            ActionMenu(icon = AppIcons.IcFilter) {
                scope.launch {
                    when (pagerState.currentPage) {
                        0 -> {
                            animeListViewModel.filter?.let {
                                animeListFilterViewModel.filter = it.copy()
                                filterBottomSheetState.expand()
                            }
                        }

                        1 -> {
                            mangaListViewModel.filter?.let {
                                mangaListFilterViewModel.filter = it.copy()
                                filterBottomSheetState.expand()
                            }
                        }
                    }
                }
            }
        },
        contentWindowInsets = horizontalBottomWindowInsets()
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (pages[page].type) {
                MediaListScreenPageType.ANIME ->
                    MediaListCommonContent(
                        viewModel = animeListViewModel,
                        filterViewModel = animeListFilterViewModel,
                        filterBottomSheetState = filterBottomSheetState,
                        showSearchBar = animeSearchBar,
                        hideSearchBar = {
                            animeSearchBar.value = false
                        }
                    )

                MediaListScreenPageType.MANGA ->
                    MediaListCommonContent(
                        viewModel = mangaListViewModel,
                        filterViewModel = mangaListFilterViewModel,
                        filterBottomSheetState = filterBottomSheetState,
                        showSearchBar = mangaSearchBar,
                        hideSearchBar = {
                            mangaSearchBar.value = false
                        }
                    )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaListCommonContent(
    viewModel: MediaListViewModel,
    filterViewModel: MediaListFilterViewModel,
    filterBottomSheetState: BottomSheetState,
    showSearchBar: MutableState<Boolean>,
    hideSearchBar: OnClick
) {
    var active by rememberSaveable { mutableStateOf(false) }

    if (!showSearchBar.value) {
        active = false
    }
    Column {
        AnimatedVisibility(visible = showSearchBar.value) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                RowDockedSearchBar(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(8.dp),
                    query = viewModel.query,
                    onQueryChange = { viewModel.search = it },
                    onSearch = {
                        active = false
                        viewModel.searchNow()
                    },
                    active = active,
                    onActiveChange = {
                        active = if (it && viewModel.searchHistory.isNotEmpty()) {
                            it
                        } else {
                            false
                        }
                    },
                    placeholder = { Text(text = stringResource(id = I18nR.string.search)) },
                    trailingIcon = {
                        if (viewModel.query.isNotEmpty()) {
                            IconButton(onClick = {
                                viewModel.search = ""
                                viewModel.searchNow()
                            }) {
                                Icon(
                                    imageVector = AppIcons.IcCancel,
                                    contentDescription = stringResource(id = I18nR.string.clear)
                                )
                            }
                        }
                    }
                ) {
                    AssistChip(
                        onClick = {
                            viewModel.search = "hello"
                            viewModel.searchNow()
                        },
                        label = { Text(text = "hello") },
                        colors = AssistChipDefaults.assistChipColors(leadingIconContentColor = MaterialTheme.colorScheme.onSurface),
                        trailingIcon = {
                            Icon(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable {

                                    },
                                imageVector = AppIcons.IcCancel,
                                contentDescription = stringResource(id = I18nR.string.clear)
                            )
                        })
                }
            }
        }
        MediaListContent(
            viewModel = viewModel,
            filterViewModel = filterViewModel,
            bottomSheetState = filterBottomSheetState
        )
    }

    HandleBackPressed(showSearchBar, hideSearchBar)
}

@Composable
private fun HandleBackPressed(
    showSearchBar: MutableState<Boolean>,
    hideSearchBar: OnClick
) {
    BackHandler(enabled = showSearchBar.value) {
        hideSearchBar()
    }
}
