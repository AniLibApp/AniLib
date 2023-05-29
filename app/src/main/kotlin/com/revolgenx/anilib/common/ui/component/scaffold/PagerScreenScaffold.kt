package com.revolgenx.anilib.common.ui.component.scaffold

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.revolgenx.anilib.common.ui.component.appbar.PagerScreenTopAppBar
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun <T> PagerScreenScaffold(
    pages: List<PagerScreen<T>> = emptyList(),
    pagerState: PagerState,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    onMoreClick: (() -> Unit)? = null,
    content: @Composable (page: Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        contentWindowInsets = contentWindowInsets,
        topBar = {
            PagerScreenTopAppBar(
                pages = pages,
                navigationIcon = navigationIcon,
                actions = actions,
                scrollBehavior = scrollBehavior,
                pagerState = pagerState,
                windowInsets = windowInsets,
                onMoreClick = onMoreClick,
            ) { index ->
                coroutineScope.launch {
                    pagerState.scrollToPage(index)
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(paddingValues)
        ) { page ->
            content(page)
        }
    }
}