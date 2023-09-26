package com.revolgenx.anilib.setting.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.androidx.AndroidScreen
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold

object GeneralSettingScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        AppearanceSettingScreenContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppearanceSettingScreenContent() {
    ScreenScaffold(
        title = stringResource(id = R.string.setting_label_general),
    ) {
        Text(text = "General Setting")
    }
}