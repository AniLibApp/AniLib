package com.revolgenx.anilib.home.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.component.appbar.AppBar
import com.revolgenx.anilib.common.ui.component.appbar.AppBarDefaults
import com.revolgenx.anilib.explore.ui.screen.ExploreScreen
import com.revolgenx.anilib.season.ui.screen.SeasonScreen
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayout
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutDefaults
import com.revolgenx.anilib.common.ui.component.tab.ScrollableTabRow
import com.revolgenx.anilib.common.ui.component.tab.TabContent
import com.revolgenx.anilib.common.ui.component.tab.pagerTabIndicatorOffset
import kotlinx.coroutines.launch


enum class HomeScreenPages(@StringRes val screen: Int) {
    EXPLORE(R.string.explore),
    SEASON(R.string.season),
    RECOMMENDATION(R.string.recommendation),
    REVIEW(R.string.review),
}

private val pages = HomeScreenPages.values()


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun HomeScreenContent() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            HomeScreenTopBar(
                pagerState = pagerState,
                scrollBehavior = scrollBehavior,
                selectedTabIndex = pagerState.currentPage,
            ) { index ->
                coroutineScope.launch {
                    pagerState.scrollToPage(index)
                }
            }
        },
        contentWindowInsets = WindowInsets(0)
    ) {
        HorizontalPager(
            pageCount = pages.size,
            state = pagerState,
            modifier = Modifier
                .padding(it)
        ) { page ->
            // Our content for each page
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                when (HomeScreenPages.values()[page]) {
                    HomeScreenPages.EXPLORE -> ExploreScreen()
                    HomeScreenPages.SEASON -> SeasonScreen()
                    HomeScreenPages.RECOMMENDATION -> ExploreScreen()
                    HomeScreenPages.REVIEW -> ExploreScreen()
                }
            }
        }

    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun HomeScreenTopBar(
    pagerState: PagerState = rememberPagerState(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    selectedTabIndex: Int = 0,
    onClick: (index: Int) -> Unit
) {
    AppBarLayout(
        colors = AppBarLayoutDefaults.appBarLayoutColors(),
        scrollBehavior = scrollBehavior
    ) {
        AppBar(
            colors = AppBarDefaults.appBarColors(),
            title = {
                HomeScreenTabRow(
                    selectedTabIndex, pagerState, onClick
                )
            },
            actions = {
                IconButton(onClick = {/* Do Something*/ }) {
                    Icon(Icons.Filled.Search, null)
                }
                IconButton(onClick = {/* Do Something*/ }) {
                    Icon(Icons.Filled.Notifications, null)
                }
            }
        )

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenTabRow(
    selectedTabIndex: Int = 0,
    pagerState: PagerState = rememberPagerState(),
    onClick: (index: Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        },
        divider = {}
    ) {
        pages.forEachIndexed { index, title ->
            Tab(
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selected = selectedTabIndex == index,
                onClick = {
                    onClick.invoke(index)
                }
            ) {
                TabContent() {
                    Text(stringResource(id = title.screen))
                }
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreenContent()
}