package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.field.overview.MediaCharacterField
import com.revolgenx.anilib.service.MediaBrowseService
import com.revolgenx.anilib.source.MediaCharacterSource
import io.reactivex.disposables.CompositeDisposable

class MediaCharacterViewModel(private val mediaBrowserService: MediaBrowseService) : ViewModel() {
    var characterSource: MediaCharacterSource? = null
    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    fun createSource(field: MediaCharacterField): MediaCharacterSource {
        characterSource = MediaCharacterSource(
            field = field,
            mediaBrowseService = mediaBrowserService,
            compositeDisposable = compositeDisposable
        )
        return characterSource!!
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
