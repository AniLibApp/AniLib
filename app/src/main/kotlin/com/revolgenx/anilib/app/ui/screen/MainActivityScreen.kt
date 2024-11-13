package com.revolgenx.anilib.app.ui.screen

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import anilib.i18n.R
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.revolgenx.anilib.app.ui.viewmodel.DeepLinkPath
import com.revolgenx.anilib.app.ui.viewmodel.MainActivityViewModel
import com.revolgenx.anilib.app.ui.viewmodel.ScrollTarget
import com.revolgenx.anilib.app.ui.viewmodel.ScrollViewModel
import com.revolgenx.anilib.app.ui.viewmodel.userScreen
import com.revolgenx.anilib.common.data.constant.MainPageOrder
import com.revolgenx.anilib.common.ext.activityViewModel
import com.revolgenx.anilib.common.ext.componentActivity
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ui.component.navigation.NavigationBar
import com.revolgenx.anilib.common.ui.composition.LocalMainTabNavigator
import com.revolgenx.anilib.common.ui.composition.LocalSnackbarHostState
import com.revolgenx.anilib.common.ui.composition.localUser
import com.revolgenx.anilib.common.ui.screen.tab.BaseTabScreen
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.home.ui.screen.HomeScreen
import com.revolgenx.anilib.list.ui.screen.AnimeListScreen
import com.revolgenx.anilib.list.ui.screen.MangaListScreen
import com.revolgenx.anilib.setting.ui.screen.SettingScreen
import com.revolgenx.anilib.social.ui.screen.ActivityUnionScreen
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


@Composable
private fun MainActivityScreenContent() {
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel: MainActivityViewModel = activityViewModel()
    val scrollViewModel: ScrollViewModel = activityViewModel()

    val localUser = localUser()
    val mainPageScreen = remember {
        viewModel.mainPageOrder.first().let {
            when (it.value) {
                MainPageOrder.HOME -> HomeScreen
                MainPageOrder.ANIME -> AnimeListScreen
                MainPageOrder.MANGA -> MangaListScreen
                MainPageOrder.ACTIVITY -> ActivityUnionScreen
            }
        }
    }

    TabNavigator(
        tab = mainPageScreen
    ) { tabNavigator ->
        viewModel.tabWrapperNavigator = LocalNavigator.current
        CheckDeepLinkPath(viewModel, tabNavigator)
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                NavigationBar {
                    viewModel.mainPageOrder.forEach {
                        if (it.value == MainPageOrder.HOME) {
                            TabNavigationItem(
                                tab = HomeScreen,
                                tabNavigator = tabNavigator,
                                onCurrentTabClick = {
                                    scrollViewModel.scrollToTop(ScrollTarget.HOME)
                                }
                            )
                        } else {
                            if (localUser.isLoggedIn.not()) return@forEach
                            when (it.value) {
                                MainPageOrder.ANIME -> TabNavigationItem(
                                    tab = AnimeListScreen,
                                    tabNavigator = tabNavigator,
                                    onCurrentTabClick = {
                                        scrollViewModel.scrollToTop(ScrollTarget.MEDIA_LIST)
                                    }
                                )

                                MainPageOrder.MANGA -> TabNavigationItem(
                                    tab = MangaListScreen,
                                    tabNavigator = tabNavigator,
                                    onCurrentTabClick = {
                                        scrollViewModel.scrollToTop(ScrollTarget.MEDIA_LIST)
                                    }
                                )

                                MainPageOrder.ACTIVITY -> TabNavigationItem(
                                    tab = ActivityUnionScreen,
                                    tabNavigator = tabNavigator,
                                    onCurrentTabClick = {
                                        scrollViewModel.scrollToTop(ScrollTarget.ACTIVITY)
                                    }
                                )

                                else -> {}
                            }
                        }
                    }

                    if (localUser.isLoggedIn) {
                        TabNavigationItem(
                            tab = userScreen.also { it.id = localUser.userId },
                            tabNavigator = tabNavigator,
                            onCurrentTabClick = {
                                scrollViewModel.scrollToTop(ScrollTarget.USER)
                            }
                        )
                    } else {
                        SettingScreen.isTab = true
                        TabNavigationItem(
                            tab = SettingScreen,
                            tabNavigator = tabNavigator,
                            onCurrentTabClick = {}
                        )
                    }
                }
            },
            contentWindowInsets = NavigationBarDefaults.windowInsets,
        ) { contentPadding ->
            NotificationPermission(viewModel = viewModel, snackbarHostState)
            Box(
                Modifier
                    .padding(contentPadding)
                    .consumeWindowInsets(contentPadding)
            ) {
                CompositionLocalProvider(
                    LocalMainTabNavigator provides tabNavigator,
                    LocalSnackbarHostState provides snackbarHostState
                ) {
                    CurrentTab()
                }
                BackPress(snackbarHostState = snackbarHostState, onDispose = {
                    viewModel.disposeTabs()
                })
            }
        }
    }
}

@Composable
private fun CheckDeepLinkPath(
    viewModel: MainActivityViewModel,
    tabNavigator: TabNavigator
) {
    LaunchedEffect(viewModel.deepLinkPath.value) {
        viewModel.deepLinkPath.value?.let {
            when (it.first) {
                DeepLinkPath.ANIME_LIST -> {
                    tabNavigator.current = AnimeListScreen
                    viewModel.deepLinkPath.value = null
                }

                DeepLinkPath.MANGA_LIST -> {
                    tabNavigator.current = MangaListScreen
                    viewModel.deepLinkPath.value = null
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(
    tab: BaseTabScreen,
    tabNavigator: TabNavigator,
    onCurrentTabClick: OnClick
) {
    val selected = tabNavigator.current == tab

    NavigationBarItem(
        selected = selected,
        onClick = {
            tabNavigator.current = tab
            if(selected){
                onCurrentTabClick()
            }
        },
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
    snackbarHostState: SnackbarHostState,
    onDispose: OnClick
) {
    val scope = rememberCoroutineScope()
    val context = localContext()

    val backPressed = remember {
        mutableStateOf(false)
    }

    val msg = stringResource(id = R.string.press_again_to_exit)
    BackHandler {
        if (backPressed.value) {
            onDispose()
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun NotificationPermission(
    viewModel: MainActivityViewModel,
    snackbarHostState: SnackbarHostState
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || viewModel.isNotificationPermissionChecked) return

    val notificationPermission = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    )
    if (!notificationPermission.status.isGranted) {
        if (notificationPermission.status.shouldShowRationale) {
            viewModel.isNotificationPermissionChecked = true
            val context = localContext()
            val notificationMsg =
                stringResource(id = R.string.grant_notification_permission_message)
            val settings = stringResource(id = R.string.settings)
            LaunchedEffect(Unit) {
                val action = snackbarHostState.showSnackbar(
                    notificationMsg,
                    actionLabel = settings,
                    withDismissAction = true,
                    duration = SnackbarDuration.Long
                )
                when (action) {
                    SnackbarResult.ActionPerformed -> {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.data = Uri.parse("package:${context.packageName}")
                        context.startActivity(intent)
                    }

                    else -> {}
                }
            }
        } else {
            // Request the permission
            SideEffect {
                notificationPermission.launchPermissionRequest()
            }
        }
    }
}
