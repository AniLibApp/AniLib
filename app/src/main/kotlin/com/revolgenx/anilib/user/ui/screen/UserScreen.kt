package com.revolgenx.anilib.user.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.store.logout
import com.revolgenx.anilib.common.ext.emptyText
import com.revolgenx.anilib.common.ext.emptyWindowInsets
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ext.horizontalWindowInsets
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.naInt
import com.revolgenx.anilib.common.ext.prettyNumberFormat
import com.revolgenx.anilib.common.ext.userMediaListScreen
import com.revolgenx.anilib.common.ext.userRelationScreen
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.action.OverflowMenu
import com.revolgenx.anilib.common.ui.component.action.OverflowMenuItem
import com.revolgenx.anilib.common.ui.component.appbar.AppBar
import com.revolgenx.anilib.common.ui.component.appbar.AppBarDefaults
import com.revolgenx.anilib.common.ui.component.appbar.AppBarHeight
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayout
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutDefaults
import com.revolgenx.anilib.common.ui.component.appbar.CollapsibleAppBarLayout
import com.revolgenx.anilib.common.ui.component.appbar.collapse
import com.revolgenx.anilib.common.ui.component.appbar.collapseProgress
import com.revolgenx.anilib.common.ui.component.common.MediaTitleType
import com.revolgenx.anilib.common.ui.component.common.ShowIfLoggedIn
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.ui.component.navigation.NavigationIcon
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.composition.localTabNavigator
import com.revolgenx.anilib.common.ui.screen.tab.BaseTabScreen
import com.revolgenx.anilib.common.ui.theme.onSurfaceVariant
import com.revolgenx.anilib.common.ui.theme.surfaceContainer
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.list.ui.screen.AnimeListScreen
import com.revolgenx.anilib.list.ui.screen.MangaListScreen
import com.revolgenx.anilib.setting.ui.screen.SettingScreen
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.user.ui.screen.userStats.UserStatsScreen
import com.revolgenx.anilib.user.ui.viewmodel.UserScreenPageType
import com.revolgenx.anilib.user.ui.viewmodel.UserViewModel
import com.skydoves.landscapist.ImageOptions
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class UserScreen(
    private val id: Int? = null,
    private val userName: String? = null,
    private val isTab: Boolean = false
) : BaseTabScreen() {
    override val iconRes: Int = R.drawable.ic_person_outline
    override val selectedIconRes: Int = R.drawable.ic_person

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.activity)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                )
            }
        }

    @Composable
    override fun Content() {
        id?.let {
            UserScreenContent(userId = id, userName = userName, isTab = isTab)
        }
    }
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun UserScreenContent(
    userId: Int?,
    userName: String?,
    isTab: Boolean,
) {
    val viewModel: UserViewModel = koinViewModel()
    val activityUnionViewModel: ActivityUnionViewModel = koinViewModel()

    viewModel.userId.value = userId
    viewModel.field.userName = userName
    viewModel.field.userId = viewModel.userId.value
    activityUnionViewModel.field.userId = viewModel.userId.value

    LaunchedEffect(viewModel.userId.value) {
        if (viewModel.userId.value != null) {
            viewModel.showAllPages()
        }
    }

    val pages by remember {
        derivedStateOf { viewModel.pages.filter { it.isVisible.value } }
    }

    LaunchedEffect(userId) {
        viewModel.getResource()
    }

    val scope = rememberCoroutineScope()
    val context = localContext()
    val pagerState = rememberPagerState() { pages.size }
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState(), canScroll = {
            pagerState.currentPage == UserScreenPageType.OVERVIEW.ordinal
        })


    ScreenScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            UserScreenTopAppbar(
                viewModel = viewModel,
                scrollBehavior = scrollBehavior,
                isLoggedInUser = viewModel.isLoggedInUser,
                isTab = isTab,
                onLogout = {
                    scope.launch {
                        context.logout()
                    }
                })
        },
        contentWindowInsets = emptyWindowInsets()
    ) {
        PagerScreenScaffold(
            pages = pages,
            pagerState = pagerState,
            navigationIcon = {},
            actions = {},
            contentWindowInsets = if (isTab) horizontalWindowInsets() else horizontalBottomWindowInsets(),
            windowInsets = emptyWindowInsets()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                when (pages[page].type) {
                    UserScreenPageType.OVERVIEW -> UserOverviewScreen(viewModel)
                    UserScreenPageType.ACTIVITY -> UserActivityUnionScreen(
                        activityUnionViewModel
                    )

                    UserScreenPageType.FAVOURITES -> UserFavouritesScreen(viewModel.userId.value)
                    UserScreenPageType.ANIME_STATS -> UserStatsScreen(
                        viewModel.userId.value,
                        MediaType.ANIME
                    )

                    UserScreenPageType.MANGA_STATS -> UserStatsScreen(
                        viewModel.userId.value,
                        MediaType.MANGA
                    )
                }

                when (pages[page].type) {
                    UserScreenPageType.OVERVIEW -> {}
                    else -> {
                        LaunchedEffect(scrollBehavior) {
                            scrollBehavior.state.collapse(scrollBehavior.snapAnimationSpec)
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserScreenTopAppbar(
    viewModel: UserViewModel,
    scrollBehavior: TopAppBarScrollBehavior,
    isLoggedInUser: Boolean = false,
    isTab: Boolean = false,
    onLogout: OnClick
) {

    val topAppBarState = scrollBehavior.state
    val progress = topAppBarState.collapseProgress().value
    val imageAlpha = if (progress >= 0.7f) 1f else progress
    val isCollapsed = progress > 0.7f

    val tabNavigator = if (isTab) localTabNavigator() else null
    val navigator = localNavigator()

    val user = viewModel.resource.value?.stateValue
    val userAppBarHeight = 310.dp
    Box {
        CollapsibleAppBarLayout(
            containerHeight = userAppBarHeight,
            maxHeightOffsetLimit = AppBarHeight,
            scrollBehavior = scrollBehavior,
            colors = AppBarLayoutDefaults.transparentColors()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.height(200.dp)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .height(150.dp)
                            .fillMaxWidth(),
                        imageUrl = user?.bannerImage,
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center
                        ),
                        previewPlaceholder = R.drawable.bleach,
                        failure = {}
                    )

                    Row(
                        modifier = Modifier
                            .height(112.dp)
                            .fillMaxWidth()
                            .align(Alignment.BottomStart),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .size(112.dp)
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .align(Alignment.Center),
                                imageUrl = user?.avatar?.image,
                                imageOptions = ImageOptions(
                                    contentScale = ContentScale.Crop,
                                    alignment = Alignment.Center
                                ),
                                previewPlaceholder = R.drawable.bleach,
                            )
                        }

//                user?.takeIf { !isLoggedInUser }?.let {
                        FilledTonalButton(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            onClick = { }
                        ) {
                            Text(
                                text = stringResource(id = if (/*it.isFollowing*/ false) R.string.following else R.string.follow)
                            )
                        }
//                }
                    }
                }

                Column(
                    modifier = Modifier
                        .height(100.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = user?.name.emptyText(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.1.sp
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        UserCountInfo(
                            label = stringResource(id = R.string.following),
                            count = user?.following.naInt().prettyNumberFormat(),
                            iconRes = R.drawable.ic_person_check_outline,
                            onClick = {
                                user?.id?.let {
                                    navigator.userRelationScreen(userId = it)
                                }
                            }
                        )
                        UserCountInfo(
                            label = stringResource(id = R.string.followers),
                            count = user?.followers.naInt().prettyNumberFormat(),
                            iconRes = R.drawable.ic_group_outline,
                            onClick = {
                                user?.id?.let {
                                    navigator.userRelationScreen(userId = it, true)
                                }
                            }
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        UserCountInfo(
                            label = stringResource(id = R.string.anime),
                            count = user?.statistics?.anime?.count.naInt().prettyNumberFormat(),
                            iconRes = R.drawable.ic_media_outline,
                            onClick = {
                                if (isTab) {
                                    tabNavigator?.current = AnimeListScreen
                                } else {
                                    user?.id?.let {
                                        navigator.userMediaListScreen(userId = it)
                                    }
                                }
                            }
                        )
                        UserCountInfo(
                            label = stringResource(id = R.string.manga),
                            count = user?.statistics?.manga?.count.naInt().prettyNumberFormat(),
                            iconRes = R.drawable.ic_book_outline,
                            onClick = {
                                if (isTab) {
                                    tabNavigator?.current = MangaListScreen
                                } else {
                                    user?.id?.let {
                                        navigator.userMediaListScreen(userId = it, true)
                                    }
                                }
                            }
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .height(userAppBarHeight)
                    .fillMaxWidth()
                    .graphicsLayer {
                        alpha = imageAlpha
                    }
                    .background(surfaceContainer)
            ) {}
        }


        AppBarLayout(
            colors = AppBarLayoutDefaults.transparentColors(),
        ) {
            AppBar(
                colors = AppBarDefaults.transparentColor(),
                title = {
                    if (isCollapsed) {
                        Text(
                            modifier = Modifier.padding(horizontal = 6.dp),
                            text = user?.name ?: stringResource(id = R.string.user),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    }
                },
                navigationIcon = {
                    if (isTab.not()) {
                        NavigationIcon()
                    }
                },
                actions = {
                    UserScreenActions(
                        isTab = isTab,
                        onLogout = onLogout
                    )
                }
            )
        }
    }
}


@Composable
private fun UserScreenActions(
    isTab: Boolean = false,
    onLogout: OnClick
) {
    val navigator = localNavigator()

    ActionMenu(iconRes = R.drawable.ic_settings) {
        navigator.push(SettingScreen())
    }

    if (isTab) {
        ShowIfLoggedIn {
            OverflowMenu {
                OverflowMenuItem(
                    textRes = R.string.logout,
                    iconRes = R.drawable.ic_logout,
                    onClick = onLogout
                )
            }
        }
    }
}


@Composable
private fun UserCountInfo(
    label: String,
    count: String,
    iconRes: Int? = null,
    onClick: OnClick = {}
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 2.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconRes?.let {
            Icon(
                modifier = Modifier.size(17.dp),
                painter = painterResource(id = iconRes), contentDescription = null,
                tint = onSurfaceVariant
            )
        }
        Text(
            text = count,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.1.sp
        )
        Text(
            text = label,
            fontSize = 17.sp,
            letterSpacing = 0.1.sp,
            color = onSurfaceVariant
        )
    }
}
