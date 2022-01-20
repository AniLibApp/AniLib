package com.revolgenx.anilib.media.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.media.data.field.MediaWatchField
import com.revolgenx.anilib.media.service.MediaInfoService
import com.revolgenx.anilib.infrastructure.source.MediaWatchSource
import io.reactivex.disposables.CompositeDisposable

class MediaWatchViewModel(private val mediaBrowseService: MediaInfoService) : ViewModel() {

    var watchSource: MediaWatchSource? = null

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    fun createSource(field: MediaWatchField): MediaWatchSource {
        watchSource =
            MediaWatchSource(field, mediaBrowseService, compositeDisposable)
        return watchSource!!
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
