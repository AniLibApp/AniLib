package com.revolgenx.anilib.media.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class MediaSocialScreenType{
    ACTIVITY, FOLLOWING;
    fun toStringRes() = when(this){
        ACTIVITY -> anilib.i18n.R.string.activity
        FOLLOWING -> anilib.i18n.R.string.following
    }
}
class MediaSocialScreenViewModel: ViewModel() {
    var screenType by mutableStateOf(MediaSocialScreenType.ACTIVITY)
}