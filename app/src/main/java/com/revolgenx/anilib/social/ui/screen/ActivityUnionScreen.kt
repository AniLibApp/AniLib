package com.revolgenx.anilib.social.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.activityComposerScreen
import com.revolgenx.anilib.common.ext.activityScreen
import com.revolgenx.anilib.common.ui.component.appbar.AppBar
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayout
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.menu.SelectMenu
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.screen.BaseTabScreen
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import com.revolgenx.anilib.type.ActivityType
import org.koin.androidx.compose.koinViewModel

object ActivityUnionScreen : BaseTabScreen() {
    override val options: TabOptions
        @Composable get() {
            val title = stringResource(R.string.activity)
            val icon = painterResource(id = R.drawable.ic_forum)

            return remember {
                TabOptions(
                    index = 0u, title = title, icon = icon
                )
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<ActivityUnionViewModel>()
        viewModel.field.type = ActivityType.TEXT

        val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
        val bottomNestedScrollConnection =
            remember { BottomNestedScrollConnection(state = scrollState) }
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

        val navigator = localNavigator()

        ScreenScaffold(
            topBar = {
                AppBarLayout(
                    scrollBehavior = scrollBehavior,
                ) {
                    AppBar(
                        title = {
                            Text(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                text = stringResource(id = R.string.activity)
                            )
                        },
                        actions = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_filter),
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            },
            navigationIcon = {},
            floatingActionButton = {
                AnimatedVisibility(visible = scrollState.value == ScrollState.ScrollDown,
                    enter = fadeIn() + expandIn { IntSize(1, 1) }) {
                    FloatingActionButton(elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp
                    ),
                        onClick = {
                            navigator.activityComposerScreen()
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_create),
                            contentDescription = null
                        )
                    }
                }
            },
            scrollBehavior = scrollBehavior,
            contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal)
        ) {
            Box(
                modifier = Modifier
                    .nestedScroll(bottomNestedScrollConnection)
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
            ) {
                ActivityUnionScreenContent(viewModel = viewModel)
            }
        }
    }
}