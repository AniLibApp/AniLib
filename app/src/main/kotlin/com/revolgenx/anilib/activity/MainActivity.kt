package com.revolgenx.anilib.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowCompat
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.revolgenx.anilib.common.ui.component.common.ShowIfLoggedIn
import com.revolgenx.anilib.common.ui.component.navigation.NavigationBar
import com.revolgenx.anilib.common.ui.composition.GlobalViewModelStoreOwner
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.composition.LocalMediaState
import com.revolgenx.anilib.common.ui.composition.LocalSnackbarHostState
import com.revolgenx.anilib.common.ui.composition.LocalUserState
import com.revolgenx.anilib.common.ui.screen.BaseTabScreen
import com.revolgenx.anilib.common.ui.screen.LoginScreen
import com.revolgenx.anilib.common.ui.screen.SpoilerBottomSheet
import com.revolgenx.anilib.common.ui.theme.AppTheme
import com.revolgenx.anilib.home.ui.screen.HomeScreen
import com.revolgenx.anilib.list.ui.screen.AnimeListScreen
import com.revolgenx.anilib.list.ui.screen.MangaListScreen
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.social.ui.screen.ActivityUnionScreen
import com.revolgenx.anilib.user.ui.screen.UserScreen

/*
* todo: handle customtab cancel result
* */
class MainActivity : BaseActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AlMarkwonFactory.init(this, this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AppTheme {
                Navigator(
                    screen = MainActivityScreen(),
                    disposeBehavior = NavigatorDisposeBehavior(false, false)
                ) { navigator ->
                    this@MainActivity.navigator = navigator
                    CompositionLocalProvider(
                        LocalMainNavigator provides navigator,
                        LocalUserState provides viewModel.userState,
                        LocalMediaState provides viewModel.mediaState,
                        GlobalViewModelStoreOwner provides this@MainActivity
                    ) {
                        CurrentScreen()
                    }
                }

                val bottomSheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                )
                SpoilerBottomSheet(
                    openBottomSheet = viewModel.openSpoilerBottomSheet,
                    bottomSheetState = bottomSheetState,
                    spanned = viewModel.spoilerSpanned
                )
            }
        }
    }

}


class MainActivityScreen : Screen {
    @Composable
    override fun Content() {
        MainActivityScreenContent()
    }
}

private var userScreen: UserScreen? = null

@Composable
fun MainActivityScreenContent() {
    val snackbarHostState = remember { SnackbarHostState() }
    TabNavigator(tab = HomeScreen) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                NavigationBar {
                    TabNavigationItem(tab = HomeScreen)
                    ShowIfLoggedIn {
                        TabNavigationItem(tab = AnimeListScreen)
                        TabNavigationItem(tab = MangaListScreen)
                        TabNavigationItem(tab = ActivityUnionScreen)
                    }
                    ShowIfLoggedIn(
                        orElse = {
                            TabNavigationItem(tab = LoginScreen)
                        },
                        content = { userId ->
                            userScreen = userScreen ?: UserScreen(userId, true)
                            TabNavigationItem(tab = userScreen!!)
                        }
                    )
                }
            },
            contentWindowInsets = WindowInsets(0)
        ) { contentPadding ->
            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                Box(Modifier.padding(contentPadding)) {
                    CurrentTab()
                }
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: BaseTabScreen) {
    val tabNavigator = LocalTabNavigator.current
    val selected = tabNavigator.current == tab

    NavigationBarItem(
        selected = selected,
        onClick = { tabNavigator.current = tab },
        icon = {
            Icon(
                painter = painterResource(id = if (selected) tab.selectedIconRes!! else tab.iconRes!!),
                contentDescription = tab.options.title
            )
        }
    )
}

