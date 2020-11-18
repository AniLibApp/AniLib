package com.revolgenx.anilib.ui.viewmodel.media

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.data.field.media.MediaStatsField
import com.revolgenx.anilib.data.model.user.stats.MediaStatsModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.media.MediaBrowseService
import io.reactivex.disposables.CompositeDisposable

class MediaStatsViewModel(private val browseService: MediaBrowseService) : ViewModel() {
    val statsLiveData by lazy {
        MediatorLiveData<Resource<MediaStatsModel>>().also { liveData ->
            liveData.addSource(browseService.mediaStatsLiveData) {
                liveData.value = it
            }
        }
    }

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    fun getStats(field: MediaStatsField) {
        statsLiveData.value = Resource.loading(null)
        browseService.getMediaStats(field, compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
