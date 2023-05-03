package com.revolgenx.anilib.setting.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.androidx.AndroidScreen

class SettingScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        Text("Settings")
    }
}