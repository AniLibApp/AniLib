package com.revolgenx.anilib.common.ui.component.scaffold

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.revolgenx.anilib.common.ext.emptyWindowInsets
import com.revolgenx.anilib.common.ui.component.appbar.PagerScreenTopAppBar
import com.revolgenx.anilib.common.ui.composition.LocalSnackbarHostState
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun <T> PagerScreenScaffold(
    pages: List<PagerScreen<T>> = emptyList(),
    pagerState: PagerState,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    userScrollEnabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    contentWindowInsets: WindowInsets = emptyWindowInsets(),
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    content: @Composable (page: Int) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
        val coroutineScope = rememberCoroutineScope()
        Scaffold(
            containerColor = containerColor,
            contentWindowInsets = contentWindowInsets,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                PagerScreenTopAppBar(
                    pages = pages,
                    navigationIcon = navigationIcon,
                    actions = actions,
                    scrollBehavior = scrollBehavior,
                    pagerState = pagerState,
                    windowInsets = windowInsets,
                ) { index ->
                    coroutineScope.launch {
                        pagerState.scrollToPage(index)
                    }
                }
            }
        ) { paddingValues ->
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = userScrollEnabled,
                modifier = Modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
            ) { page ->
                content(page)
            }
        }
    }
}