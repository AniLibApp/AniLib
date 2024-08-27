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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.common.data.store.MediaListDisplayMode
import com.revolgenx.anilib.common.data.store.toStringRes
import com.revolgenx.anilib.common.ext.emptyWindowInsets
import com.revolgenx.anilib.common.ext.topWindowInsets
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.action.OverflowMenu
import com.revolgenx.anilib.common.ui.component.action.OverflowMenuItem
import com.revolgenx.anilib.common.ui.component.action.OverflowRadioMenuItem
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayout
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutDefaults
import com.revolgenx.anilib.common.ui.component.chip.ClearAssistChip
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.search.RowDockedSearchBar
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcCancel
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.ui.icons.appicon.IcLayoutStyle
import com.revolgenx.anilib.common.ui.icons.appicon.IcList
import com.revolgenx.anilib.common.ui.icons.appicon.IcSearch
import com.revolgenx.anilib.common.ui.screen.tab.BaseTabScreen
import com.revolgenx.anilib.list.ui.viewmodel.MediaListFilterViewModel
import com.revolgenx.anilib.list.ui.viewmodel.MediaListViewModel
import com.revolgenx.anilib.media.ui.model.isAnime
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber
import anilib.i18n.R as I18nR

abstract class MediaListScreen : BaseTabScreen() {
    @Composable
    override fun Content() {
        val viewModel = mediaListViewModel()
        val filterViewModel = mediaListFilterViewModel()
        MediaListScreenContent(viewModel, filterViewModel)
    }

    @Composable
    abstract fun mediaListViewModel(): MediaListViewModel

    @Composable
    abstract fun mediaListFilterViewModel(): MediaListFilterViewModel

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MediaListScreenContent(
    viewModel: MediaListViewModel,
    filterViewModel: MediaListFilterViewModel
) {
    val openFilterBottomSheet = rememberBottomSheetState()
    val scope = rememberCoroutineScope()


    var active by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val appbarHeight = if (active) 120.dp else 64.dp
    val appbarAnimation by animateDpAsState(
        targetValue = appbarHeight,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val isAnime = viewModel.field.type.isAnime

    ScreenScaffold(
        topBar = {
            AppBarLayout(
                scrollBehavior = scrollBehavior,
                colors = AppBarLayoutDefaults.transparentColors(),
                containerHeight = appbarAnimation,
                windowInsets = topWindowInsets()
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
                            viewModel.updateSearchHistory()
                            viewModel.searchNow()
                        },
                        active = active,
                        onActiveChange = {
                            active = it
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

                                val displayModePref = viewModel.displayMode
                                val displayMode = displayModePref.collectAsState()

                                OverflowMenu(
                                    icon = AppIcons.IcLayoutStyle
                                ) { isOpen ->
                                    MediaListDisplayMode.entries.forEach {
                                        OverflowRadioMenuItem(
                                            text = stringResource(id = it.toStringRes()),
                                            selected = displayMode.value == it.value
                                        ) {
                                            isOpen.value = false
                                            scope.launch {
                                                displayModePref.set(it.value)
                                            }
                                        }
                                    }
                                }
                                IconButton(onClick = {
                                    scope.launch {
                                        viewModel.filter?.let {
                                            filterViewModel.filter = it.copy()
                                            openFilterBottomSheet.expand()
                                        }
                                    }
                                }) {
                                    Icon(
                                        imageVector = AppIcons.IcFilter,
                                        contentDescription = stringResource(id = I18nR.string.filter)
                                    )
                                }
                            }
                        }
                    ) {
                        val searchHistory = viewModel.searchHistory.value
                        if (searchHistory.isEmpty()) {
                            AssistChip(onClick = {}, label = {
                                Text(text = stringResource(id = anilib.i18n.R.string.empty))
                            })
                        } else {
                            searchHistory.forEach { search ->
                                ClearAssistChip(
                                    text = search,
                                    onClick = {
                                        viewModel.search = search
                                        viewModel.searchNow()
                                    }, onClear = {
                                        viewModel.deleteSearchHistory(search)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
    ) {
        Box(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        ) {
            MediaListContent(
                viewModel = viewModel,
                filterViewModel = filterViewModel,
                bottomSheetState = openFilterBottomSheet
            )
        }
    }
}