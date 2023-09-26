package com.revolgenx.anilib.social.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.activityComposerScreen
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.action.DisappearingFAB
import com.revolgenx.anilib.common.ui.component.appbar.AppBar
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayout
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcCreate
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.ui.icons.appicon.IcForum
import com.revolgenx.anilib.common.ui.icons.appicon.IcForumOutline
import com.revolgenx.anilib.common.ui.screen.tab.BaseTabScreen
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import com.revolgenx.anilib.type.ActivityType
import org.koin.androidx.compose.koinViewModel

object ActivityUnionScreen : BaseTabScreen() {

    override val tabIcon: ImageVector = AppIcons.IcForumOutline
    override val selectedIcon: ImageVector = AppIcons.IcForum

    override val options: TabOptions
        @Composable get() {
            val title = stringResource(R.string.activity)

            return remember {
                TabOptions(
                    index = 0u, title = title
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
                            ActionMenu(icon = AppIcons.IcFilter) {

                            }
                        }
                    )
                }
            },
            navigationIcon = {},
            floatingActionButton = {
                DisappearingFAB(
                    scrollState = scrollState,
                    icon = AppIcons.IcCreate
                ) {
                    navigator.activityComposerScreen()
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