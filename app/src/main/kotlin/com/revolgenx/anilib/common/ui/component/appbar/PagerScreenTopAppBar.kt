package com.revolgenx.anilib.common.ui.component.appbar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ext.isNotNull
import com.revolgenx.anilib.common.ui.component.action.NavigationIcon
import com.revolgenx.anilib.common.ui.component.tab.ScrollableTabRow
import com.revolgenx.anilib.common.ui.component.tab.Tab
import com.revolgenx.anilib.common.ui.component.tab.TabContent
import com.revolgenx.anilib.common.ui.component.tab.pagerTabIndicatorOffset
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun <T> PagerScreenTopAppBar(
    pages: List<PagerScreen<T>> = emptyList(),
    pagerState: PagerState,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    onPageClick: (index: Int) -> Unit,
) {
    AppBarLayout(
        scrollBehavior = scrollBehavior,
        windowInsets = windowInsets
    ) {
        AppBar(
            title = {
                PagerScreenTabRow(pages, pagerState, onPageClick)
            },
            navigationIcon = navigationIcon ?: {
                NavigationIcon()
            },
            actions = actions ?: {}
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun <T> PagerScreenTabRow(
    pages: List<PagerScreen<T>>,
    pagerState: PagerState,
    onClick: (index: Int) -> Unit
) {
    val selectedTabIndex = pagerState.currentPage
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        },
        divider = {}
    ) {
        pages.forEachIndexed { index, page ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    onClick.invoke(index)
                }) {
                TabContent() {
                    val pageTitle = stringResource(id = page.title)
                    if (page.icon.isNotNull()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = page.icon!!,
                                contentDescription = pageTitle
                            )
                            Text(pageTitle)
                        }
                    } else {
                        Text(pageTitle)
                    }
                }
            }
        }
    }
}