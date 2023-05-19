package com.revolgenx.anilib.user.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.data.store.logout
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.localNavigator
import com.revolgenx.anilib.common.ui.component.action.ActionMenuItem
import com.revolgenx.anilib.common.ui.component.action.ActionsMenu
import com.revolgenx.anilib.common.ui.component.appbar.AlAppBarDefaults
import com.revolgenx.anilib.common.ui.component.appbar.AppBar
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayout
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutColors
import com.revolgenx.anilib.common.ui.component.common.MediaTitleType
import com.revolgenx.anilib.common.ui.component.common.isLoggedIn
import com.revolgenx.anilib.common.ui.component.navigation.NavigationIcon
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.screen.BaseTabScreen
import com.revolgenx.anilib.common.ui.screen.PagerScreen
import com.revolgenx.anilib.setting.ui.screen.SettingScreen
import com.revolgenx.anilib.user.ui.model.UserModel
import com.revolgenx.anilib.user.ui.viewmodel.UserViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.fresco.FrescoImage
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.CollapsingToolbarScaffoldState
import me.onebone.toolbar.CollapsingToolbarScope
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import org.koin.androidx.compose.koinViewModel

class UserScreen(private val id: Int, private val isTab: Boolean = false) : BaseTabScreen() {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.activity)
            val icon = painterResource(id = R.drawable.ic_person)

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
        UserScreenContent(userId = id, isTab = isTab)
    }
}


private typealias UserScreenPage = PagerScreen<UserScreenPageType>


private enum class UserScreenPageType {
    OVERVIEW,
    ACTIVITY,
    FAVOURITES
}

private val pages = listOf(
    UserScreenPage(UserScreenPageType.OVERVIEW, R.string.overview),
    UserScreenPage(UserScreenPageType.ACTIVITY, R.string.activity),
    UserScreenPage(UserScreenPageType.FAVOURITES, R.string.favourites)
)


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun UserScreenContent(
    userId: Int,
    isTab: Boolean,
    viewModel: UserViewModel = koinViewModel()
) {
    LaunchedEffect(userId) {
        viewModel.field.userId = userId
        viewModel.getResource()
    }

    val scope = rememberCoroutineScope()
    val context = localContext()
    val pagerState = rememberPagerState()
    val collapsingToolbarState = rememberCollapsingToolbarScaffoldState()
    val resourceState = viewModel.resource.value
    val userModel = if (resourceState is ResourceState.Success) {
        resourceState.data
    } else null

    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = collapsingToolbarState,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbarModifier = Modifier.background(MaterialTheme.colorScheme.surface),
        enabled = true,
        toolbar = {
            UserScreenTopAppbar(
                user = userModel,
                isTab = isTab,
                onLogout = {
                    scope.launch {
                        context.logout()
                    }
                })
        }
    ) {
        PagerScreenScaffold(
            pages = pages,
            pagerState = pagerState,
            navigationIcon = {},
            actions = {},
            contentWindowInsets = NavigationBarDefaults.windowInsets,
            windowInsets = WindowInsets(0)
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when (pages[page].type) {
                    UserScreenPageType.OVERVIEW -> UserOverviewScreen(viewModel.resource) {
                        viewModel.refresh()
                    }

                    UserScreenPageType.ACTIVITY -> UserActivityScreen(userId)
                    UserScreenPageType.FAVOURITES -> UserFavouritesScreen(userId)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CollapsingToolbarScope.UserScreenTopAppbar(
    user: UserModel? = null,
    isTab: Boolean = false,
    collapsingToolbarState: CollapsingToolbarScaffoldState = rememberCollapsingToolbarScaffoldState(),
    onLogout: () -> Unit
) {
    val progress = collapsingToolbarState.toolbarState.progress
    val imageAlpha = if (progress <= 0.2) 0.2f else progress

    FrescoImage(
        modifier = Modifier
            .parallax(0.5f)
            .height(300.dp)
            .fillMaxWidth()
            .graphicsLayer {
                alpha = imageAlpha
            },
        imageUrl = user?.bannerImage,
        imageOptions = ImageOptions(
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        ),
    )


    AppBarLayout(
        colors = AppBarLayoutColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        ),
    ) {
        AppBar(
            colors = AlAppBarDefaults.appBarColors(
                containerColor = Color.Transparent,
            ),
            title = {
                MediaTitleType {
                    Text(
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


@Composable
private fun UserScreenActions(
    isTab: Boolean = false,
    onLogout: () -> Unit
) {
    val loggedIn = isLoggedIn()
    val navigator = localNavigator()
    val actions = remember{
        derivedStateOf {
            listOfNotNull(
                isTab.takeIf { it }?.let {
                    ActionMenuItem.AlwaysShown(
                        titleRes = R.string.settings,
                        iconRes = R.drawable.ic_settings,
                        onClick = {
                            navigator.push(SettingScreen())
                        }
                    )
                },
                loggedIn.takeIf { it }?.let {
                    ActionMenuItem.NeverShown(
                        titleRes = R.string.logout,
                        iconRes = R.drawable.ic_logout,
                        onClick = onLogout
                    )
                }
            )
        }
    }

    ActionsMenu(
        items = actions.value,
        maxVisibleItems = 1
    )
}

