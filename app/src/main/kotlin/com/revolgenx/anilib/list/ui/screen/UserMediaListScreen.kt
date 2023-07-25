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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.component.search.RowDockedSearchBar
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import com.revolgenx.anilib.common.ui.theme.onSurface
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.list.ui.viewmodel.AnimeListViewModel
import com.revolgenx.anilib.list.ui.viewmodel.MangaListViewModel
import com.revolgenx.anilib.list.ui.viewmodel.MediaListViewModel
import org.koin.androidx.compose.koinViewModel

class UserMediaListScreen(val userId: Int) : AndroidScreen() {
    @Composable
    override fun Content() {
        UserMediaListScreenContent(userId)
    }
}

private typealias MediaListScreenPage = PagerScreen<MediaListScreenPageType>

private enum class MediaListScreenPageType {
    ANIME,
    MANGA,
}

private val pages = listOf(
    MediaListScreenPage(MediaListScreenPageType.ANIME, R.string.anime),
    MediaListScreenPage(MediaListScreenPageType.MANGA, R.string.manga),
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun UserMediaListScreenContent(userId: Int) {
    val pagerState = rememberPagerState() { pages.size }
    var animeSearchBar by rememberSaveable { mutableStateOf(false) }
    var mangaSearchBar by rememberSaveable { mutableStateOf(false) }

    PagerScreenScaffold(
        pagerState = pagerState,
        pages = pages,
        actions = {
            ActionMenu(iconRes = R.drawable.ic_search) {
                when (pagerState.currentPage) {
                    0 -> {
                        animeSearchBar = !animeSearchBar
                    }

                    1 -> {
                        mangaSearchBar = !mangaSearchBar
                    }

                    else -> {}
                }
            }
            ActionMenu(iconRes = R.drawable.ic_filter) {

            }
        },
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (pages[page].type) {
                MediaListScreenPageType.ANIME -> AnimeListContent(userId, animeSearchBar) {
                    animeSearchBar = false
                }

                MediaListScreenPageType.MANGA -> MangaListContent(userId, mangaSearchBar) {
                    mangaSearchBar = false
                }
            }
        }
    }
}


@Composable
private fun AnimeListContent(userId: Int?, showSearchBar: Boolean, hideSearchBar: OnClick) {
    val viewModel = koinViewModel<AnimeListViewModel>()
    viewModel.field.userId = userId
    MediaListCommonContent(viewModel, showSearchBar, hideSearchBar)
}


@Composable
private fun MangaListContent(userId: Int?, showSearchBar: Boolean, hideSearchBar: OnClick) {
    val viewModel = koinViewModel<MangaListViewModel>()
    viewModel.field.userId = userId
    MediaListCommonContent(viewModel, showSearchBar, hideSearchBar)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaListCommonContent(
    viewModel: MediaListViewModel,
    showSearchBar: Boolean,
    hideSearchBar: OnClick
) {
    var active by rememberSaveable { mutableStateOf(false) }
    val openFilterBottomSheet = rememberSaveable { mutableStateOf(false) }
    if (!showSearchBar) {
        active = false
    }
    Column {
        AnimatedVisibility(visible = showSearchBar) {
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
                    height = 50.dp,
                    onActiveChange = {
                        active = if (it && viewModel.searchHistory.isNotEmpty()) {
                            it
                        } else {
                            false
                        }
                    },
                    placeholder = { Text(text = stringResource(id = R.string.search)) },
                    trailingIcon = {
                        if (viewModel.query.isNotEmpty()) {
                            IconButton(onClick = {
                                viewModel.search = ""
                                viewModel.searchNow()
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_cancel),
                                    contentDescription = stringResource(id = R.string.clear)
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
                        colors = AssistChipDefaults.assistChipColors(leadingIconContentColor = onSurface),
                        trailingIcon = {
                            Icon(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable {
                                        openFilterBottomSheet.value = true
                                    },
                                painter = painterResource(id = R.drawable.ic_cancel),
                                contentDescription = stringResource(id = R.string.clear)
                            )
                        })
                }
            }
        }
        MediaListContent(viewModel, openFilterBottomSheet)
    }

    BackHandler(enabled = showSearchBar) {
        hideSearchBar()
    }
}
