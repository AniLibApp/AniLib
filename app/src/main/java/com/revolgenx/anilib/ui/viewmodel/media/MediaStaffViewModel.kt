package com.revolgenx.anilib.ui.viewmodel.media

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.data.field.media.MediaStaffField
import com.revolgenx.anilib.infrastructure.service.media.MediaBrowseService
import com.revolgenx.anilib.infrastructure.source.MediaStaffSource
import io.reactivex.disposables.CompositeDisposable

class MediaStaffViewModel(private val browseService: MediaBrowseService) : ViewModel() {
    var staffSource: MediaStaffSource? = null
    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    fun createSource(field: MediaStaffField): MediaStaffSource {
        staffSource = MediaStaffSource(field, browseService, compositeDisposable)
        return staffSource!!
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
