package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.field.overview.MediaStatsField
import com.revolgenx.anilib.model.stats.MediaStatsModel
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.service.MediaBrowseService
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
