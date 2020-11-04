package com.revolgenx.anilib.ui.viewmodel.media

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.data.field.media.MediaReviewField
import com.revolgenx.anilib.infrastructure.service.media.MediaBrowseService
import com.revolgenx.anilib.infrastructure.source.MediaReviewSource
import io.reactivex.disposables.CompositeDisposable

class MediaReviewViewModel(
    private val browseService: MediaBrowseService
) : ViewModel() {

    var reviewSource: MediaReviewSource? = null
    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    fun createSource(field: MediaReviewField): MediaReviewSource {
        reviewSource = MediaReviewSource(field, browseService, compositeDisposable)
        return reviewSource!!
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
