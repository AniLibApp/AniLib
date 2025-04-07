package com.revolgenx.anilib.common.ui.screen.tab

import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.DefaultScreenLifecycleOwner
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleOwner
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleProvider
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.tab.Tab

abstract class BaseTabScreen : Tab, ScreenLifecycleProvider {
    abstract val tabIcon: ImageVector
    abstract val selectedIcon: ImageVector

    override val key: ScreenKey = uniqueScreenKey
    @OptIn(InternalVoyagerApi::class)
    override fun getLifecycleOwner(): ScreenLifecycleOwner = DefaultScreenLifecycleOwner
}