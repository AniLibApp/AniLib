package com.revolgenx.anilib.activity

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.componentActivity
import com.revolgenx.anilib.common.ext.emptyWindowInsets
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.toStringResource
import com.revolgenx.anilib.common.ui.component.common.ShowIfLoggedIn
import com.revolgenx.anilib.common.ui.component.navigation.NavigationBar
import com.revolgenx.anilib.common.ui.composition.GlobalViewModelStoreOwner
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.composition.LocalMainTabNavigator
import com.revolgenx.anilib.common.ui.composition.LocalMediaState
import com.revolgenx.anilib.common.ui.composition.LocalSnackbarHostState
import com.revolgenx.anilib.common.ui.composition.LocalUserState
import com.revolgenx.anilib.common.ui.screen.auth.LoginScreen
import com.revolgenx.anilib.common.ui.screen.spoiler.SpoilerBottomSheet
import com.revolgenx.anilib.common.ui.screen.tab.BaseTabScreen
import com.revolgenx.anilib.common.ui.screen.transition.SlideTransition
import com.revolgenx.anilib.common.ui.theme.AppTheme
import com.revolgenx.anilib.home.ui.screen.HomeScreen
import com.revolgenx.anilib.list.ui.screen.AnimeListScreen
import com.revolgenx.anilib.list.ui.screen.MangaListScreen
import com.revolgenx.anilib.setting.ui.screen.SettingScreen
import com.revolgenx.anilib.social.ui.screen.ActivityUnionScreen
import com.revolgenx.anilib.user.ui.screen.UserScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
* todo: handle customtab cancel result
* */
class MainActivity : BaseActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme{
                Navigator(
                    screen = MainActivityScreen,
                    disposeBehavior = NavigatorDisposeBehavior(false, false)
                ) { navigator ->
                    this@MainActivity.navigator = navigator
                    CompositionLocalProvider(
                        LocalMainNavigator provides navigator,
                        LocalUserState provides viewModel.userState,
                        LocalMediaState provides viewModel.mediaState,
                        GlobalViewModelStoreOwner provides this@MainActivity
                    ) {
                        SlideTransition(navigator = navigator)
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


object MainActivityScreen : Screen {
    @Composable
    override fun Content() {
        MainActivityScreenContent()
    }
}

private var userScreen: UserScreen? = null

@Composable
fun MainActivityScreenContent() {
    val snackbarHostState = remember { SnackbarHostState() }
    TabNavigator(tab = HomeScreen) { tabNavigator ->
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
                            SettingScreen.isTab = true
                            TabNavigationItem(tab = SettingScreen)
                        },
                        content = { userId ->
                            userScreen = userScreen ?: UserScreen(userId, isTab = true)
                            TabNavigationItem(tab = userScreen!!)
                        }
                    )
                }
            },
            contentWindowInsets = emptyWindowInsets()
        ) { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                CompositionLocalProvider(
                    LocalMainTabNavigator provides tabNavigator,
                    LocalSnackbarHostState provides snackbarHostState
                ) {
                    CurrentTab()
                }
                BackPress(snackbarHostState)
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

@Composable
fun BackPress(snackbarHostState: SnackbarHostState) {
    var exit by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = localContext()
    LaunchedEffect(key1 = exit) {
        if (exit) {
            delay(2000)
            exit = false
        }
    }

    val msg = R.string.press_again_to_exit.toStringResource()
    BackHandler {
        if (exit) {
            context.componentActivity()?.finish()
        } else {
            exit = true
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = msg,
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
}