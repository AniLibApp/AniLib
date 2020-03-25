package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.field.overview.MediaCharacterField
import com.revolgenx.anilib.service.MediaBrowseService
import com.revolgenx.anilib.source.MediaCharacterSource
import io.reactivex.disposables.CompositeDisposable

class MediaCharacterViewModel(private val mediaBrowserService: MediaBrowseService) : SourceViewModel<MediaCharacterSource, MediaCharacterField>() {
    override fun createSource(field: MediaCharacterField): MediaCharacterSource {
        source = MediaCharacterSource(
            field = field,
            mediaBrowseService = mediaBrowserService,
            compositeDisposable = compositeDisposable
        )
        return source!!
    }
}
