package com.revolgenx.anilib.common.ui.component.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import com.revolgenx.anilib.common.ui.component.action.NavigationIcon
import com.revolgenx.anilib.common.ui.component.appbar.AppBar
import com.revolgenx.anilib.common.ui.component.appbar.AppBarColors
import com.revolgenx.anilib.common.ui.component.appbar.AppBarDefaults
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayout
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutColors
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutDefaults
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.composition.LocalSnackbarHostState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenScaffold(
    modifier: Modifier = Modifier,
    title: String = "",
    subTitle: String? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    attachScrollBehaviorConnection: Boolean = true,
    bottomNestedScrollConnection: BottomNestedScrollConnection? = null,
    attachBottomScrollConnection: Boolean = true,
    topBar: (@Composable () -> Unit)? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    floatingActionButton: @Composable () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    appBarLayoutColors: AppBarLayoutColors = AppBarLayoutDefaults.appBarLayoutColors(),
    appBarColors: AppBarColors = AppBarDefaults.transparentColor(),
    actions: (@Composable RowScope.() -> Unit)? = null,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable BoxScope.(snackbarHostState: SnackbarHostState) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
        Scaffold(
            modifier = modifier,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            contentWindowInsets = contentWindowInsets,
            floatingActionButton = floatingActionButton,
            containerColor = containerColor,
            topBar = topBar ?: {
                ScreenTopAppbar(
                    title = title,
                    subTitle = subTitle,
                    appBarLayoutColors = appBarLayoutColors,
                    appBarColors = appBarColors,
                    scrollBehavior = scrollBehavior,
                    navigationIcon = navigationIcon,
                    actions = actions,
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .let {
                        if (attachBottomScrollConnection && bottomNestedScrollConnection != null) {
                            it.nestedScroll(bottomNestedScrollConnection)
                        } else {
                            it
                        }
                    }.let {
                        if (attachScrollBehaviorConnection && scrollBehavior != null) {
                            it.nestedScroll(scrollBehavior.nestedScrollConnection)
                        } else {
                            it
                        }
                    }
            ) {
                content(snackbarHostState)
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTopAppbar(
    title: String,
    subTitle: String? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    appBarLayoutColors: AppBarLayoutColors = AppBarLayoutDefaults.appBarLayoutColors(),
    appBarColors: AppBarColors = AppBarDefaults.transparentColor(),
) {
    AppBarLayout(
        scrollBehavior = scrollBehavior,
        windowInsets = windowInsets,
        colors = appBarLayoutColors
    ) {
        AppBar(
            title = {
                Column {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    subTitle?.let {
                        LightText(
                            text = it,
                            maxLines = 1,
                        )
                    }
                }
            },
            navigationIcon = navigationIcon ?: {
                NavigationIcon()
            },
            actions = actions ?: {},
            colors = appBarColors
        )
    }
}