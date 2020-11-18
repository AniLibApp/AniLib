package com.revolgenx.anilib.ui.viewmodel.media

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.data.field.media.MediaWatchField
import com.revolgenx.anilib.infrastructure.service.media.MediaBrowseService
import com.revolgenx.anilib.infrastructure.source.MediaWatchSource
import io.reactivex.disposables.CompositeDisposable

class MediaWatchViewModel(private val mediaBrowseService: MediaBrowseService) : ViewModel() {

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
