package com.revolgenx.anilib.relation.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcGroup
import com.revolgenx.anilib.common.ui.icons.appicon.IcPersonCheck
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import anilib.i18n.R as I18nR

class UserSocialRelationScreen(private val userId: Int, private var isFollower: Boolean?) :
    AndroidScreen() {
    @Composable
    override fun Content() {
        UserSocialRelationScreenContent(userId, isFollower)
        isFollower = null
    }
}


private typealias UserRelationScreenPage = PagerScreen<UserRelationScreenPageType>

private enum class UserRelationScreenPageType {
    FOLLOWING,
    FOLLOWERS,
}

private val pages = listOf(
    UserRelationScreenPage(
        UserRelationScreenPageType.FOLLOWING,
        I18nR.string.following,
        AppIcons.IcPersonCheck
    ),
    UserRelationScreenPage(
        UserRelationScreenPageType.FOLLOWERS,
        I18nR.string.followers,
        AppIcons.IcGroup
    ),
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UserSocialRelationScreenContent(userId: Int, isFollower: Boolean?) {
    val pagerState = rememberPagerState { pages.size }
    isFollower?.let {
        LaunchedEffect(userId) {
            pagerState.scrollToPage(1)
        }
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    PagerScreenScaffold(
        pages = pages,
        pagerState = pagerState,
        actions = {},
        scrollBehavior = scrollBehavior
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            when (pages[page].type) {
                UserRelationScreenPageType.FOLLOWING -> UserRelationScreen(userId, false)
                UserRelationScreenPageType.FOLLOWERS -> UserRelationScreen(userId, true)
            }
        }
    }
}