package com.revolgenx.anilib.common.ui.component.scaffold

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.common.ui.component.appbar.AppBar
import com.revolgenx.anilib.common.ui.component.appbar.AppBarColors
import com.revolgenx.anilib.common.ui.component.appbar.AppBarDefaults
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayout
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutColors
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutDefaults
import com.revolgenx.anilib.common.ui.component.navigation.NavigationIcon
import com.revolgenx.anilib.common.ui.composition.LocalSnackbarHostState



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenScaffold(
    modifier: Modifier = Modifier,
    title: String = "",
    subTitle: String? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    topBar: (@Composable () -> Unit)? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    floatingActionButton: @Composable () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.background,
    appBarLayoutColors: AppBarLayoutColors = AppBarLayoutDefaults.appBarLayoutColors(),
    appBarColors: AppBarColors = AppBarDefaults.appBarColors(),
    actions: (@Composable RowScope.() -> Unit)? = null,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    onMoreClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
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
                    onMoreClick = onMoreClick,
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                content()
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
    appBarColors: AppBarColors = AppBarDefaults.appBarColors(),
    onMoreClick: (() -> Unit)? = null
) {
    AppBarLayout(
        scrollBehavior = scrollBehavior,
        windowInsets = windowInsets,
        colors = appBarLayoutColors
    ) {
        AppBar(
            title = {
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = title
                    )
                    subTitle?.let {
                        Text(
                            text = it,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            navigationIcon = navigationIcon ?: {
                NavigationIcon()
            },
            actions = actions ?: {
                IconButton(onClick = {
                    onMoreClick?.invoke()
                }) {
                    Icon(Icons.Filled.MoreVert, null)
                }
            },
            colors = appBarColors
        )
    }
}