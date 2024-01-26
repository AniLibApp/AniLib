package com.revolgenx.anilib.social.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen

class ActivityScreen(private val activityId: Int): AndroidScreen() {
    @Composable
    override fun Content() {
        ActivityScreenContent(activityId = activityId)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActivityScreenContent(activityId: Int) {
    ScreenScaffold() {
    }
}