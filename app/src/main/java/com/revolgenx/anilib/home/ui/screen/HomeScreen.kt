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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.R
import com.revolgenx.anilib.browse.ui.screen.BrowseScreen
import com.revolgenx.anilib.common.ext.localNavigator
import com.revolgenx.anilib.common.ui.component.action.ActionMenuItem
import com.revolgenx.anilib.common.ui.component.action.ActionsMenu
import com.revolgenx.anilib.common.ui.component.common.isLoggedIn
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.screen.BaseTabScreen
import com.revolgenx.anilib.common.ui.screen.PagerScreen
import com.revolgenx.anilib.home.explore.ui.screen.ExploreScreen
import com.revolgenx.anilib.home.recommendation.ui.screen.RecommendationsScreen
import com.revolgenx.anilib.home.review.ui.screen.ReviewsScreen
import com.revolgenx.anilib.home.season.ui.screen.SeasonScreen
import com.revolgenx.anilib.notification.ui.screen.NotificationScreen
import kotlinx.coroutines.launch

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
    val pagerState = rememberPagerState()
    val navigator = localNavigator()
    var isMenuOpen by remember {
        mutableStateOf(true)
    }

    val isLoggedIn = isLoggedIn()
    val actionItems = remember {
        derivedStateOf {
            listOfNotNull(
                ActionMenuItem.AlwaysShown(
                    titleRes = R.string.search,
                    onClick = {
                        navigator.push(BrowseScreen())
                    },
                    iconRes = R.drawable.ic_search,
                    contentDescriptionRes = R.string.search,
                ),
                isLoggedIn.takeIf { it }?.let {
                    ActionMenuItem.AlwaysShown(
                        titleRes = R.string.notifcations,
                        onClick = {
                            navigator.push(NotificationScreen())
                        },
                        iconRes = R.drawable.ic_notification,
                        contentDescriptionRes = R.string.notifcations,
                    )
                }
            )
        }
    }

    PagerScreenScaffold(
        pages = pages,
        pagerState = pagerState,
        scrollBehavior = scrollBehavior,
        navigationIcon = {},
        actions = {
            ActionsMenu(
                items = actionItems.value,
                isOpen = isMenuOpen,
                onToggleOverflow = { isMenuOpen = !isMenuOpen },
                maxVisibleItems = 2
            )
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



