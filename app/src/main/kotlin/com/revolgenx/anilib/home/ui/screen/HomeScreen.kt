package com.revolgenx.anilib.home.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.R
import com.revolgenx.anilib.browse.ui.screen.BrowseScreen
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.common.ShowIfLoggedIn
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.screen.tab.BaseTabScreen
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import com.revolgenx.anilib.home.explore.ui.screen.ExploreScreen
import com.revolgenx.anilib.home.recommendation.ui.screen.RecommendationsScreen
import com.revolgenx.anilib.home.review.ui.screen.ReviewsScreen
import com.revolgenx.anilib.home.season.ui.screen.SeasonScreen
import com.revolgenx.anilib.notification.ui.screen.NotificationScreen

object HomeScreen : BaseTabScreen() {
    override val iconRes: Int = R.drawable.ic_home_outline
    override val selectedIconRes: Int = R.drawable.ic_home
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
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
    RECOMMENDATIONS,
    REVIEWS,
}

private val pages = listOf(
    HomeScreenPage(HomeScreenPages.EXPLORE, R.string.explore),
    HomeScreenPage(HomeScreenPages.SEASON, R.string.season),
    HomeScreenPage(HomeScreenPages.RECOMMENDATIONS, R.string.recommendations),
    HomeScreenPage(HomeScreenPages.REVIEWS, R.string.reviews),
)


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun HomeScreenContent() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val pagerState = rememberPagerState() { pages.size }
    val navigator = localNavigator()

    PagerScreenScaffold(
        pages = pages,
        pagerState = pagerState,
        scrollBehavior = scrollBehavior,
        navigationIcon = {},
        actions = {
            ActionMenu(iconRes = R.drawable.ic_search) {
                navigator.push(BrowseScreen())
            }
            ShowIfLoggedIn {
                ActionMenu(iconRes = R.drawable.ic_notification) {
                    navigator.push(NotificationScreen())
                }
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
                HomeScreenPages.RECOMMENDATIONS -> RecommendationsScreen()
                HomeScreenPages.REVIEWS -> ReviewsScreen()
            }
        }
    }

}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreenContent()
}



