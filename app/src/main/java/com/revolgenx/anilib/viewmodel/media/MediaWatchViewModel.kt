package com.revolgenx.anilib.viewmodel.media

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.field.media.MediaWatchField
import com.revolgenx.anilib.service.media.MediaBrowseService
import com.revolgenx.anilib.source.MediaWatchSource
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
