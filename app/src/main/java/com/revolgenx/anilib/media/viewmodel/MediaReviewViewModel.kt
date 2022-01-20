package com.revolgenx.anilib.media.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.media.data.field.MediaReviewField
import com.revolgenx.anilib.media.service.MediaInfoService
import com.revolgenx.anilib.infrastructure.source.MediaReviewSource
import io.reactivex.disposables.CompositeDisposable

class MediaReviewViewModel(
    private val browseService: MediaInfoService
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
