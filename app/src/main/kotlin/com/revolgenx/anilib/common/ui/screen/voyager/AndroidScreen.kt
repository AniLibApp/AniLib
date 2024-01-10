package com.revolgenx.anilib.common.ui.screen.voyager

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey

abstract class AndroidScreen : Screen {
    override val key: ScreenKey = uniqueScreenKey
}