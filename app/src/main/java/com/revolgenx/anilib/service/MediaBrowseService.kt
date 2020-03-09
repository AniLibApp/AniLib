package com.revolgenx.anilib.service

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.model.*
import com.revolgenx.anilib.model.field.overview.*
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

typealias MutableOverviewType = MutableLiveData<Resource<MediaOverviewModel>>
typealias MutableWatchType = MutableLiveData<Resource<List<MediaWatchModel>>>
typealias MutableCharacterType = MutableLiveData<Resource<List<MediaCharacterModel>>>
typealias MutableStaffType = MutableLiveData<Resource<List<MediaStaffModel>>>
typealias MutableReviewType = MutableLiveData<Resource<List<MediaReviewModel>>>
typealias MutableStatsType = MutableLiveData<Resource<MediaStatsModel>>

abstract class MediaBrowseService(graphRepository: BaseGraphRepository) {
    val mediaOverviewLiveData by lazy {
        MutableOverviewType()
    }
    val mediaWatchLiveData by lazy {
        MutableWatchType()
    }
    val mediaCharacterLiveData by lazy {
        MutableCharacterType()
    }
    val mediaStaffLiveData by lazy {
        MutableStaffType()
    }
    val mediaReviewLiveData by lazy {
        MutableReviewType()
    }
    val mediaStatsLiveData by lazy {
        MutableStatsType()
    }

    abstract fun getMediaOverview(
        mediaId: Int,
        compositeDisposable: CompositeDisposable? = null
    ): MutableOverviewType

    abstract fun getMediaWatch(
        field: MediaWatchField,
        compositeDisposable: CompositeDisposable? = null
    ): MutableWatchType

    abstract fun getMediaCharacter(
        field: MediaCharacterField,
        compositeDisposable: CompositeDisposable? = null
    ): MutableCharacterType

    abstract fun getMediaStaff(
        field: MediaStaffField,
        compositeDisposable: CompositeDisposable? = null
    ): MutableStaffType


    abstract fun getMediaReview(
        field: MediaReviewField,
        compositeDisposable: CompositeDisposable? = null
    ): MutableReviewType


    abstract fun getMediaStats(
        field: MediaStatsField,
        compositeDisposable: CompositeDisposable? = null
    ): MutableStatsType

}