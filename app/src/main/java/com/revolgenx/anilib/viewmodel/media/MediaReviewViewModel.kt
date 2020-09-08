package com.revolgenx.anilib.viewmodel.media

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.field.media.MediaReviewField
import com.revolgenx.anilib.service.media.MediaBrowseService
import com.revolgenx.anilib.source.MediaReviewSource
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
