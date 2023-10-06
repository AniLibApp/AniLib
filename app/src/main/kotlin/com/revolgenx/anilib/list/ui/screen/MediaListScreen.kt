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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayout
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutDefaults
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.search.RowDockedSearchBar
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcCancel
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.ui.icons.appicon.IcSearch
import com.revolgenx.anilib.common.ui.screen.tab.BaseTabScreen
import com.revolgenx.anilib.list.ui.viewmodel.MediaListViewModel
import com.revolgenx.anilib.media.ui.model.isAnime
import anilib.i18n.R as I18nR

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
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val openFilterBottomSheet = rememberSaveable { mutableStateOf(false) }
    val isAnime = viewModel.field.type.isAnime

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
                colors = AppBarLayoutDefaults.transparentColors(),
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
                        onActiveChange = {
                            active = if (it && true/*viewModel.searchHistory.isNotEmpty()*/) {
                                it
                            } else {
                                false
                            }
                        },
                        placeholder = { Text(text = stringResource(id = if (isAnime) I18nR.string.search_anime else I18nR.string.search_manga)) },
                        leadingIcon = {
                            Icon(
                                imageVector = AppIcons.IcSearch,
                                contentDescription = stringResource(id = if (isAnime) I18nR.string.search_anime else I18nR.string.search_manga)
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
                                            imageVector = AppIcons.IcCancel,
                                            contentDescription = stringResource(id = I18nR.string.clear)
                                        )
                                    }
                                }
                                IconButton(onClick = {
                                    openFilterBottomSheet.value = true
                                }) {
                                    Icon(
                                        imageVector = AppIcons.IcFilter,
                                        contentDescription = stringResource(id = I18nR.string.filter)
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
                                    imageVector = AppIcons.IcCancel,
                                    contentDescription = stringResource(id = I18nR.string.clear)
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