package com.revolgenx.anilib.relation.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.androidx.AndroidScreen
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen

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
        R.string.following,
        R.drawable.ic_person_check
    ),
    UserRelationScreenPage(
        UserRelationScreenPageType.FOLLOWERS,
        R.string.followers,
        R.drawable.ic_group
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

    PagerScreenScaffold(
        pages = pages,
        pagerState = pagerState,
        actions = {}
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (pages[page].type) {
                UserRelationScreenPageType.FOLLOWING -> UserRelationScreen(userId, false)
                UserRelationScreenPageType.FOLLOWERS -> UserRelationScreen(userId, true)
            }
        }
    }
}