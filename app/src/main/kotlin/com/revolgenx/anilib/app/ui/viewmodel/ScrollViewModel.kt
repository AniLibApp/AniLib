package com.revolgenx.anilib.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.ext.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter

enum class ScrollTarget {
    HOME,
    ACTIVITY,
    MEDIA_LIST,
    USER,
    MEDIA_SOCIAL_ACTIVITY,
}

class ScrollViewModel : ViewModel() {
    private val _scrollEvents = MutableSharedFlow<ScrollTarget>()

    fun scrollToTop(target: ScrollTarget) {
        launch {
            _scrollEvents.emit(target)
        }
    }

    fun scrollEventFor(target: ScrollTarget): Flow<ScrollTarget> {
        return _scrollEvents.filter { it == target }
    }
}