package com.revolgenx.anilib.common.ui.composition

import androidx.compose.runtime.compositionLocalOf
import cafe.adriel.voyager.navigator.Navigator

val LocalMainNavigator = compositionLocalOf<Navigator> {
    error("Navigator not initialized")
}