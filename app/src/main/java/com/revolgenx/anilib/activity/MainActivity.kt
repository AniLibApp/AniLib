package com.revolgenx.anilib.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.revolgenx.anilib.common.ui.component.common.ShowIfLoggedIn
import com.revolgenx.anilib.common.ui.component.navigation.AlNavigationBar
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.screen.LoginScreen
import com.revolgenx.anilib.common.ui.theme.AppTheme
import com.revolgenx.anilib.home.ui.screen.HomeScreen
import com.revolgenx.anilib.list.ui.screen.MediaListScreen
import com.revolgenx.anilib.social.ui.screen.ActivityUnionScreen
import com.revolgenx.anilib.user.ui.screen.UserScreen

/*
* todo: handle customtab cancel result
* */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AppTheme {
                BottomSheetNavigator(
                    sheetShape = MaterialTheme.shapes.extraLarge,
                    sheetBackgroundColor = MaterialTheme.colorScheme.background,
                    scrimColor = MaterialTheme.colorScheme.scrim
                ) {
                    Navigator(
                        screen = MainActivityScreen(),
                        disposeBehavior = NavigatorDisposeBehavior(false, false)
                    ) { navigator ->
                        CompositionLocalProvider(
                            LocalMainNavigator provides navigator
                        ) {
                            CurrentScreen()
                        }
                    }
                }
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
    TabNavigator(tab = HomeScreen) {
        Scaffold(
            bottomBar = {
                AlNavigationBar {
                    TabNavigationItem(tab = HomeScreen)
                    ShowIfLoggedIn {
                        TabNavigationItem(tab = MediaListScreen)
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
            Box(Modifier.padding(contentPadding)) {
                CurrentTab()
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    val options = tab.options
    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = { Icon(painter = options.icon!!, contentDescription = options.title) }
    )
}

