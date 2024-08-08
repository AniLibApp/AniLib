package com.revolgenx.anilib.setting.ui.model

import androidx.compose.runtime.MutableState
import com.revolgenx.anilib.user.ui.model.UserOptionsModel

data class MediaSettingsModel(
    val options: MutableState<UserOptionsModel>
)