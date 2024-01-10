package com.revolgenx.anilib.home.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.R
import com.revolgenx.anilib.browse.ui.screen.BrowseScreen
import com.revolgenx.anilib.common.ext.emptyWindowInsets
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.common.ShowIfLoggedIn
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcHome
import com.revolgenx.anilib.common.ui.icons.appicon.IcHomeOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcNotification
import com.revolgenx.anilib.common.ui.icons.appicon.IcSearch
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import com.revolgenx.anilib.common.ui.screen.tab.BaseTabScreen
import com.revolgenx.anilib.home.explore.ui.screen.ExploreScreen
import com.revolgenx.anilib.home.recommendation.ui.screen.RecommendationScreen
import com.revolgenx.anilib.home.season.ui.screen.SeasonScreen
import com.revolgenx.anilib.notification.ui.screen.NotificationScreen
import com.revolgenx.anilib.review.ui.screen.ReviewListScreen
import anilib.i18n.R as I18nR

object HomeScreen : BaseTabScreen() {
    override val tabIcon: ImageVector = AppIcons.IcHomeOutline
    override val selectedIcon: ImageVector = AppIcons.IcHome
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(I18nR.string.home)

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
    HomeScreenPage(HomeScreenPages.EXPLORE, I18nR.string.explore),
    HomeScreenPage(HomeScreenPages.SEASON, I18nR.string.season),
    HomeScreenPage(HomeScreenPages.RECOMMENDATIONS, I18nR.string.recommendations),
    HomeScreenPage(HomeScreenPages.REVIEWS, I18nR.string.reviews),
)


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun HomeScreenContent() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val pagerState = rememberPagerState() { pages.size }
    val navigator = localNavigator()

    PagerScreenScaffold(
        pages = pages,
        pagerState = pagerState,
        scrollBehavior = scrollBehavior,
        navigationIcon = {},
        actions = {
            ActionMenu(icon = AppIcons.IcSearch) {
                navigator.push(BrowseScreen())
            }
            ShowIfLoggedIn {
                ActionMenu(icon = AppIcons.IcNotification) {
                    navigator.push(NotificationScreen())
                }
            }
        },
        contentWindowInsets = emptyWindowInsets()
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            when (HomeScreenPages.entries[page]) {
                HomeScreenPages.EXPLORE -> ExploreScreen()
                HomeScreenPages.SEASON -> SeasonScreen()
                HomeScreenPages.RECOMMENDATIONS -> RecommendationScreen()
                HomeScreenPages.REVIEWS -> ReviewListScreen()
            }
        }
    }

}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreenContent()
}



