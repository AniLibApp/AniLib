package com.revolgenx.anilib.media.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.media.ui.model.MediaModel

class MediaViewModel : ViewModel() {
    var media = mutableStateOf(MediaModel())
}