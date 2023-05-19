package com.revolgenx.anilib.list.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.activityViewModel
import com.revolgenx.anilib.common.ext.localUser
import com.revolgenx.anilib.common.ui.component.action.ActionMenuItem
import com.revolgenx.anilib.common.ui.component.action.ActionsMenu
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.component.search.RowDockedSearchBar
import com.revolgenx.anilib.common.ui.screen.BaseTabScreen
import com.revolgenx.anilib.common.ui.screen.PagerScreen
import com.revolgenx.anilib.list.ui.viewmodel.AnimeListViewModel
import com.revolgenx.anilib.list.ui.viewmodel.MangaListViewModel
import com.revolgenx.anilib.list.ui.viewmodel.MediaListViewModel

object MediaListScreen : BaseTabScreen() {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.list)
            val icon = painterResource(id = R.drawable.ic_list)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        MediaListScreenContent()
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
private fun MediaListScreenContent() {
    val pagerState = rememberPagerState()
    var animeSearchBar by rememberSaveable { mutableStateOf(false) }
    var mangaSearchBar by rememberSaveable { mutableStateOf(false) }
    val userId = localUser().userId

    PagerScreenScaffold(
        pagerState = pagerState,
        pages = pages,
        navigationIcon = {},
        actions = {
            ActionsMenu(
                items = listOf(
                    ActionMenuItem.AlwaysShown(
                        titleRes = R.string.search,
                        iconRes = R.drawable.ic_search,
                        onClick = {
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
                    ),
                    ActionMenuItem.AlwaysShown(
                        titleRes = R.string.filter,
                        iconRes = R.drawable.ic_filter,
                        onClick = {

                        }
                    )
                )
            )
        },
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal)
    ) { page ->


        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (pages[page].type) {
                MediaListScreenPageType.ANIME -> AnimeListContent(userId, animeSearchBar){
                    animeSearchBar = false
                }
                MediaListScreenPageType.MANGA -> MangaListContent(userId, mangaSearchBar){
                    mangaSearchBar = false
                }
            }
        }
    }
}


@Composable
private fun AnimeListContent(userId: Int?, showSearchBar: Boolean, hideSearchBar: () -> Unit) {
    val viewModel = activityViewModel<AnimeListViewModel>()
    LaunchedEffect(viewModel) {
        viewModel.field.userId = userId
    }
    MediaListCommonContent(viewModel, showSearchBar, hideSearchBar)
}


@Composable
private fun MangaListContent(userId: Int?, showSearchBar: Boolean, hideSearchBar: () -> Unit) {
    val viewModel = activityViewModel<MangaListViewModel>()
    LaunchedEffect(viewModel) {
        viewModel.field.userId = userId
    }
    MediaListCommonContent(viewModel, showSearchBar, hideSearchBar)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaListCommonContent(
    viewModel: MediaListViewModel,
    showSearchBar: Boolean,
    hideSearchBar: () -> Unit
) {
    var active by rememberSaveable { mutableStateOf(false) }
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
                    onActiveChange = { active = it },
                    placeholder = { Text(text = stringResource(id = R.string.search)) },
                    trailingIcon = {
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
                                painter = painterResource(id = R.drawable.ic_cancel),
                                contentDescription = stringResource(id = R.string.clear)
                            )
                        })
                }
            }
        }
        MediaListContent(viewModel)
    }

    BackHandler(enabled = showSearchBar) {
        hideSearchBar()
    }
}
