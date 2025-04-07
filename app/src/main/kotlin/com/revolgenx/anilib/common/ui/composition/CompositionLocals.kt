package com.revolgenx.anilib.common.ui.composition

import android.content.res.Configuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.ViewModelStoreOwner
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.revolgenx.anilib.common.data.state.MediaState
import com.revolgenx.anilib.common.data.state.UserState

val LocalMainNavigator = compositionLocalOf<Navigator> {
    error("Navigator not initialized")
}

val LocalMainTabNavigator = compositionLocalOf<TabNavigator> {
    error("Navigator not initialized")
}

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState?> {
    null
}

val LocalUserState = compositionLocalOf {
    UserState()
}

val LocalMediaState = compositionLocalOf {
    MediaState()
}

val GlobalViewModelStoreOwner = compositionLocalOf<ViewModelStoreOwner> {
    error("GlobalViewModelStoreOwner not initialized")
}


@Composable
fun localUser() = LocalUserState.current

@Composable
fun localNavigator() = LocalMainNavigator.current

@Composable
fun localTabNavigator() = LocalMainTabNavigator.current

@Composable
fun localMediaState() = LocalMediaState.current

@Composable
fun isLandScape() = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE