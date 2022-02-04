package com.revolgenx.anilib.media.service

import com.revolgenx.anilib.character.data.model.CharacterEdgeModel
import com.revolgenx.anilib.user.data.model.stats.MediaStatsModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.media.data.field.*
import com.revolgenx.anilib.media.data.model.*
import com.revolgenx.anilib.staff.data.model.StaffEdgeModel
import io.reactivex.disposables.CompositeDisposable


abstract class MediaInfoService(protected val graphRepository: BaseGraphRepository) {
    abstract fun getSimpleMedia(
        mediaId: Int?,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<MediaModel>) -> Unit
    )

    abstract fun getMediaOverview(
        field: MediaOverviewField,
        compositeDisposable: CompositeDisposable? = null,
        callback: (Resource<MediaModel>) -> Unit
    )

    abstract fun getMediaWatch(
        field: MediaWatchField,
        compositeDisposable: CompositeDisposable? = null,
        resourceCallback: (Resource<List<MediaStreamingEpisodeModel>>) -> Unit
    )

    abstract fun getMediaCharacter(
        field: MediaCharacterField,
        compositeDisposable: CompositeDisposable? = null,
        resourceCallback: (Resource<List<CharacterEdgeModel>>) -> Unit
    )

    abstract fun getMediaStaff(
        field: MediaStaffField,
        compositeDisposable: CompositeDisposable? = null,
        resourceCallback: (Resource<List<StaffEdgeModel>>) -> Unit
    )


    abstract fun getMediaReview(
        field: MediaReviewField,
        compositeDisposable: CompositeDisposable? = null,
        resourceCallback: (Resource<List<MediaReviewModel>>) -> Unit
    )


    abstract fun getMediaStats(
        field: MediaStatsField,
        compositeDisposable: CompositeDisposable? = null,
        callback: (Resource<MediaStatsModel>) -> Unit
    )

    abstract fun getMediaSocialFollowing(
        field: MediaSocialFollowingField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<List<MediaSocialFollowingModel>>) -> Unit
    )
}