package com.revolgenx.anilib.common.ui.composition

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModelStoreOwner
import cafe.adriel.voyager.navigator.Navigator
import com.revolgenx.anilib.common.data.state.UserState

val LocalMainNavigator = compositionLocalOf<Navigator> {
    error("Navigator not initialized")
}

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided")
}

val LocalUserState = compositionLocalOf<UserState> {
    error("UserState not initialized")
}


val GlobalViewModelStoreOwner = compositionLocalOf<ViewModelStoreOwner> {
    error("GlobalViewModelStoreOwner not initialized")
}


