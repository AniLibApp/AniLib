package com.revolgenx.anilib.home.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.component.action.ActionMenuItem
import com.revolgenx.anilib.common.ui.component.action.ActionsMenu
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.screen.BaseTabScreen
import com.revolgenx.anilib.common.ui.screen.PagerScreen
import com.revolgenx.anilib.explore.ui.screen.ExploreScreen
import com.revolgenx.anilib.season.ui.screen.SeasonScreen

object HomeScreen : BaseTabScreen() {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.home)
            val icon = painterResource(id = R.drawable.ic_home)

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
        HomeScreenContent()
    }
}

private typealias HomeScreenPage = PagerScreen<HomeScreenPages>

private enum class HomeScreenPages {
    EXPLORE,
    SEASON,
    RECOMMENDATION,
    REVIEW,
}

private val pages = listOf(
    HomeScreenPage(HomeScreenPages.EXPLORE, R.string.explore),
    HomeScreenPage(HomeScreenPages.SEASON, R.string.season),
    HomeScreenPage(HomeScreenPages.RECOMMENDATION, R.string.recommendation),
    HomeScreenPage(HomeScreenPages.REVIEW, R.string.review),
)


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun HomeScreenContent() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val pagerState = rememberPagerState()
    var isMenuOpen by remember {
        mutableStateOf(true)
    }

    PagerScreenScaffold(
        pages = pages,
        pagerState = pagerState,
        scrollBehavior = scrollBehavior,
        navigationIcon = {},
        actions = {
            ActionsMenu(
                items = listOf(
                    ActionMenuItem.AlwaysShown(
                        titleRes = R.string.search,
                        onClick = {},
                        iconRes = R.drawable.ic_voice,
                        contentDescriptionRes = R.string.search,
                    ),
                    ActionMenuItem.AlwaysShown(
                        titleRes = R.string.notifcations,
                        onClick = {},
                        iconRes = R.drawable.ic_notification,
                        contentDescriptionRes = R.string.notifcations,
                    )
                ),
                isOpen = isMenuOpen,
                onToggleOverflow = { isMenuOpen = !isMenuOpen },
                maxVisibleItems = 2
            )
            IconButton(onClick = {/* Do Something*/ }) {
                Icon(Icons.Filled.Search, null)
            }
            IconButton(onClick = {/* Do Something*/ }) {
                Icon(Icons.Filled.Notifications, null)
            }
        },
        contentWindowInsets = WindowInsets(0)
    ) { page ->
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


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreenContent()
}



