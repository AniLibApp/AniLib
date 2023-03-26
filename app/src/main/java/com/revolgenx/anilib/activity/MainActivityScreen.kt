package com.revolgenx.anilib.activity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.revolgenx.anilib.home.screen.HomeScreen
import com.revolgenx.anilib.common.ui.component.navigation.NavigationBar
import com.revolgenx.anilib.user.screen.UserLoginTab


class MainScreen : Screen {
    @Composable
    override fun Content() {
        MainActivityScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityScreen() {
    TabNavigator(tab = HomeScreen) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    TabNavigationItem(tab = HomeScreen)
                    TabNavigationItem(tab = UserLoginTab)
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

@Preview
@Composable
fun MainActivityScreenPreview() {
    MainActivityScreen()
}