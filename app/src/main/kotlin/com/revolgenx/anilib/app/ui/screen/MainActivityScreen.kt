package com.revolgenx.anilib.app.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import anilib.i18n.R
import cafe.adriel.voyager.androidx.AndroidScreenLifecycleOwner
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.revolgenx.anilib.common.ext.componentActivity
import com.revolgenx.anilib.common.ext.emptyWindowInsets
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ui.component.common.ShowIfLoggedIn
import com.revolgenx.anilib.common.ui.component.navigation.NavigationBar
import com.revolgenx.anilib.common.ui.composition.LocalMainTabNavigator
import com.revolgenx.anilib.common.ui.composition.LocalSnackbarHostState
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.screen.tab.BaseTabScreen
import com.revolgenx.anilib.home.ui.screen.HomeScreen
import com.revolgenx.anilib.list.ui.screen.AnimeListScreen
import com.revolgenx.anilib.list.ui.screen.MangaListScreen
import com.revolgenx.anilib.setting.ui.screen.SettingScreen
import com.revolgenx.anilib.social.ui.screen.ActivityUnionScreen
import com.revolgenx.anilib.user.ui.screen.UserScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

private val yearLesser = Calendar.getInstance().get(Calendar.YEAR) + 2
private val yearGreater = 1940
private val yearList by lazy {
    (yearLesser downTo yearGreater).map { it.toString() }
}

object MainActivityScreen : Screen {
    @Composable
    override fun Content() {
        MainActivityScreenContent()
    }
}

private var userScreen: UserScreen = UserScreen(isTab = true)

@OptIn(ExperimentalVoyagerApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MainActivityScreenContent() {
    val snackbarHostState = remember { SnackbarHostState() }
    val navigator = localNavigator()

    TabNavigator(
        tab = HomeScreen,
        tabDisposable = {
//            if (backPressed.value) {
//                TabDisposable(
//                    navigator = it,
//                    tabs = listOf(
//                        HomeScreen,
//                        AnimeListScreen,
//                        MangaListScreen,
//                        ActivityUnionScreen,
//                        SettingScreen,
//                        userScreen
//                    )
//                )
//            }
        }
    ) { tabNavigator ->
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
                            TabNavigationItem(tab = userScreen.also { it.id = userId })
                        }
                    )
                }
            },
            contentWindowInsets = NavigationBarDefaults.windowInsets,
        ) { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                CompositionLocalProvider(
                    LocalMainTabNavigator provides tabNavigator,
                    LocalSnackbarHostState provides snackbarHostState
                ) {
                    CurrentTab()
                }
                BackPress(navigator = navigator, snackbarHostState)
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
                imageVector = if (selected) tab.selectedIcon else tab.tabIcon,
                contentDescription = tab.options.title
            )
        }
    )
}

@Composable
private fun BackPress(
    navigator: Navigator,
    snackbarHostState: SnackbarHostState,
) {
    val scope = rememberCoroutineScope()
    val context = localContext()
    val backPressed = remember {
        mutableStateOf(false)
    }

    val msg = stringResource(id = R.string.press_again_to_exit)
    BackHandler {
        if (backPressed.value) {
            for (screen in navigator.items) {
                AndroidScreenLifecycleOwner.get(screen).onDispose(screen)
            }
            context.componentActivity()?.finish()
        } else {
            backPressed.value = true
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = msg,
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )
                backPressed.value = false
            }
        }
    }
}