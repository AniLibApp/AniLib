package com.revolgenx.anilib.list.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayout
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutColors
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutDefaults
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.search.RowDockedSearchBar
import com.revolgenx.anilib.common.ui.screen.BaseTabScreen
import com.revolgenx.anilib.list.ui.viewmodel.MediaListViewModel
import com.revolgenx.anilib.media.ui.model.isAnime

abstract class MediaListScreen : BaseTabScreen() {
    @Composable
    override fun Content() {
        val viewModel = mediaListViewModel()
        MediaListScreenContent(viewModel)
    }

    @Composable
    abstract fun mediaListViewModel(): MediaListViewModel
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MediaListScreenContent(viewModel: MediaListViewModel) {

    var active by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val openFilterBottomSheet = rememberSaveable { mutableStateOf(false) }
    val isAnime = viewModel.field.type.isAnime()
    ScreenScaffold(
        topBar = {
            AppBarLayout(
                scrollBehavior = scrollBehavior,
                colors = AppBarLayoutDefaults.appBarLayoutColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                ),
                containerHeight = 56.dp
            ) {
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
                        placeholder = { Text(text = stringResource(id = if (isAnime) R.string.search_anime else R.string.search_manga)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = stringResource(id = if (isAnime) R.string.search_anime else R.string.search_manga)
                            )
                        },
                        trailingIcon = {
                            Row {
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
                                IconButton(onClick = {
                                    openFilterBottomSheet.value = true
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_filter),
                                        contentDescription = stringResource(id = R.string.filter)
                                    )
                                }
                            }
                        }
                    ) {

                    }
                }
            }
        },
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal)
    ) {
        Box(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        ) {
            MediaListContent(viewModel = viewModel, openFilterBottomSheet)
        }
    }
}