package com.revolgenx.anilib.airing.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.airing.data.field.AiringScheduleField

class AiringScheduleFilterViewModel : ViewModel() {
    var field = mutableStateOf(AiringScheduleField())
}