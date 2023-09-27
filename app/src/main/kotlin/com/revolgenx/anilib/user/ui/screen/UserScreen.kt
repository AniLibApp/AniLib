package com.revolgenx.anilib.user.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.emptyWindowInsets
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ext.horizontalWindowInsets
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.common.ext.prettyNumberFormat
import com.revolgenx.anilib.common.ext.userMediaListScreen
import com.revolgenx.anilib.common.ext.userRelationScreen
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.appbar.CollapsingAppbar
import com.revolgenx.anilib.common.ui.component.appbar.collapse
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.composition.localTabNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcBookOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcGroupOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcMediaOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcPerson
import com.revolgenx.anilib.common.ui.icons.appicon.IcPersonCheckOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcPersonOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcSettings
import com.revolgenx.anilib.common.ui.screen.tab.BaseTabScreen
import com.revolgenx.anilib.common.ui.theme.onSurfaceVariant
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.list.ui.screen.AnimeListScreen
import com.revolgenx.anilib.list.ui.screen.MangaListScreen
import com.revolgenx.anilib.setting.ui.screen.SettingScreen
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.user.ui.screen.userStats.UserStatsScreen
import com.revolgenx.anilib.user.ui.viewmodel.UserScreenPageType
import com.revolgenx.anilib.user.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

class UserScreen(
    private val id: Int? = null,
    private val userName: String? = null,
    private val isTab: Boolean = false
) : BaseTabScreen() {
    override val tabIcon: ImageVector get() = AppIcons.IcPersonOutline
    override val selectedIcon: ImageVector get() = AppIcons.IcPerson

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(I18nR.string.user)
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

    val pagerState = rememberPagerState() { pages.size }
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState(), canScroll = {
            pagerState.currentPage == UserScreenPageType.OVERVIEW.ordinal
        })


    ScreenScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            val isLoggedInUser = viewModel.isLoggedInUser.collectAsState(initial = false)
            UserScreenTopAppbar(
                viewModel = viewModel,
                scrollBehavior = scrollBehavior,
                isLoggedInUser = isLoggedInUser.value,
                isTab = isTab
            )
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
    isLoggedInUser: Boolean,
    isTab: Boolean = false,
) {
    val tabNavigator = if (isTab) localTabNavigator() else null
    val navigator = localNavigator()

    val user = viewModel.resource.value?.stateValue
    val containerHeight = 310.dp

    CollapsingAppbar(
        scrollBehavior = scrollBehavior,
        containerHeight = containerHeight,
        maxAlpha = 1f,
        alphaFraction = 0.7f,
        collapseFraction = 0.7f,
        containerContent = { _ ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.height(200.dp)
                ) {
                    ImageAsync(
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
                        viewable = true
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
                            ImageAsync(
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
                                viewable = true
                            )
                        }

                        user?.takeIf { !isLoggedInUser }?.let {
                            FilledTonalButton(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                onClick = { }
                            ) {
                                Text(
                                    text = stringResource(id = if (it.isFollowing) I18nR.string.following else I18nR.string.follow)
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .height(100.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = user?.name.orEmpty(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.1.sp
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        UserCountInfo(
                            label = stringResource(id = I18nR.string.following),
                            count = user?.following.orZero().prettyNumberFormat(),
                            icon = AppIcons.IcPersonCheckOutline,
                            onClick = {
                                user?.id?.let {
                                    navigator.userRelationScreen(userId = it)
                                }
                            }
                        )
                        UserCountInfo(
                            label = stringResource(id = I18nR.string.followers),
                            count = user?.followers.orZero().prettyNumberFormat(),
                            icon = AppIcons.IcGroupOutline,
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
                            label = stringResource(id = I18nR.string.anime),
                            count = user?.statistics?.anime?.count.orZero().prettyNumberFormat(),
                            icon = AppIcons.IcMediaOutline,
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
                            label = stringResource(id = I18nR.string.manga),
                            count = user?.statistics?.manga?.count.orZero().prettyNumberFormat(),
                            icon = AppIcons.IcBookOutline,
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
        },
        title = { isCollapsed ->
            if (isCollapsed) {
                Text(
                    modifier = Modifier.padding(horizontal = 6.dp),
                    text = user?.name ?: stringResource(id = I18nR.string.user),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
        },
        navigationIcon = if (isTab.not()) null else ({}),
        actions = { isCollapsed ->
            ActionMenu(icon = AppIcons.IcSettings, tonalButton = !isCollapsed) {
                SettingScreen.isTab = false
                navigator.push(SettingScreen)
            }
        }
    )


}

@Composable
private fun UserCountInfo(
    label: String,
    count: String,
    icon: ImageVector? = null,
    onClick: OnClick = {}
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 2.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(
                modifier = Modifier.size(17.dp),
                imageVector = icon,
                contentDescription = null,
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
