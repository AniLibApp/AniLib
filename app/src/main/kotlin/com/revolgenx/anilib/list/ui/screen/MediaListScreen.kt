package com.revolgenx.anilib.list.ui.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutDefaults
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.search.RowDockedSearchBar
import com.revolgenx.anilib.common.ui.screen.tab.BaseTabScreen
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

    val appbarHeight = if(active) 120.dp else 64.dp
    val appbarAnimation by animateDpAsState(
        targetValue = appbarHeight,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    ScreenScaffold(
        topBar = {
            AppBarLayout(
                scrollBehavior = scrollBehavior,
                colors = AppBarLayoutDefaults.appBarLayoutColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                ),
                containerHeight = appbarAnimation
            ) {
                Box(
                    modifier = Modifier
                        .height(appbarAnimation)
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
                            active = if (it && true/*viewModel.searchHistory.isNotEmpty()*/) {
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
                                            openFilterBottomSheet.value = true
                                        },
                                    painter = painterResource(id = R.drawable.ic_cancel),
                                    contentDescription = stringResource(id = R.string.clear)
                                )
                            })
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