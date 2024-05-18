package com.revolgenx.anilib.app.ui.screen

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
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
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.revolgenx.anilib.app.ui.viewmodel.MainActivityViewModel
import com.revolgenx.anilib.common.ext.componentActivity
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.localSnackbarHostState
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
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
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

@Composable
private fun MainActivityScreenContent() {
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel: MainActivityViewModel = koinViewModel()

    val dispose = remember {
        mutableStateOf(false)
    }

    TabNavigator(
        tab = HomeScreen,
        tabDisposable = {
            if (dispose.value) {
                TabDisposable(
                    navigator = it,
                    tabs = listOf(
                        HomeScreen,
                        AnimeListScreen,
                        MangaListScreen,
                        ActivityUnionScreen,
                        SettingScreen,
                        userScreen
                    )
                )
            }
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
            NotificationPermission(viewModel = viewModel, snackbarHostState)
            Box(Modifier.padding(contentPadding)) {
                CompositionLocalProvider(
                    LocalMainTabNavigator provides tabNavigator,
                    LocalSnackbarHostState provides snackbarHostState
                ) {
                    CurrentTab()
                }
                BackPress(snackbarHostState = snackbarHostState, dispose = dispose)
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
    snackbarHostState: SnackbarHostState,
    dispose: MutableState<Boolean>
) {
    val scope = rememberCoroutineScope()
    val context = localContext()

    val backPressed = remember {
        mutableStateOf(false)
    }

    val msg = stringResource(id = R.string.press_again_to_exit)
    BackHandler {
        if (backPressed.value) {
            dispose.value = true
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
