package com.revolgenx.anilib.service

import com.revolgenx.anilib.model.*
import com.revolgenx.anilib.field.MediaOverviewField
import com.revolgenx.anilib.field.overview.*
import com.revolgenx.anilib.model.stats.*
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.network.converter.toMediaOverviewModel
import com.revolgenx.anilib.repository.network.converter.toModel
import com.revolgenx.anilib.repository.util.ERROR
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.util.pmap
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class MediaBrowseServiceImpl(graphRepository: BaseGraphRepository) :
    MediaBrowseService(graphRepository) {

    override fun getMediaOverview(
        field: MediaOverviewField,
        compositeDisposable: CompositeDisposable?
    ): MutableOverviewType {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map { response ->
                response.data()?.Media()!!.toMediaOverviewModel()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mediaOverviewLiveData.value = Resource.success(it)
            }, {
                Timber.e(it)
                mediaOverviewLiveData.value = Resource.error(it.message ?: ERROR, null)
            })
        compositeDisposable?.add(disposable)

        return mediaOverviewLiveData
    }

    override fun getMediaWatch(
        field: MediaWatchField,
        compositeDisposable: CompositeDisposable?
    ): MutableWatchType {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map { response ->
                response.data()?.Media()!!.toModel()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mediaWatchLiveData.value = Resource.success(it)
            }, {
                Timber.e(it)
                mediaWatchLiveData.value = Resource.error(it.message ?: ERROR, null)
            })

        compositeDisposable?.add(disposable)

        return mediaWatchLiveData
    }

    override fun getMediaCharacter(
        field: MediaCharacterField,
        compositeDisposable: CompositeDisposable?
    ): MutableCharacterType {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map { response ->
                runBlocking {
                    response.data()?.Media()?.characters()?.edges()?.pmap {
                        MediaCharacterModel().also { charac ->
                            charac.mediaId = it.id() ?: -1
                            charac.role = it.role()?.ordinal
                            it.node()?.let { node ->
                                charac.name = node.name()?.full()
                            }
                            charac.voiceActors = it.voiceActors()?.pmap {
                                VoiceActorModel().also { model ->
                                    model.name = it.name()?.full()
                                    model.actorId = it.id()
                                }
                            }
                        }
                    }
                }

            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mediaCharacterLiveData.value = Resource.success(it)
            }, {
                Timber.e(it)
                mediaCharacterLiveData.value = Resource.error(it.message ?: ERROR, null)
            })

        compositeDisposable?.add(disposable)
        return mediaCharacterLiveData
    }

    override fun getMediaStaff(
        field: MediaStaffField,
        compositeDisposable: CompositeDisposable?
    ): MutableStaffType {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                runBlocking {
                    it.data()?.Media()?.staff()?.edges()?.pmap { s ->
                        MediaStaffModel().also { model ->
                            model.role = s.role()
                            s.node()?.let { staff ->
                                model.mediaId = staff.id()
                                model.name = staff.name()?.full()
                            }
                        }
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mediaStaffLiveData.value = Resource.success(it)
            }, {
                Timber.e(it)
                mediaStaffLiveData.value = Resource.error(it.message ?: ERROR, null)
            })

        compositeDisposable?.add(disposable)
        return mediaStaffLiveData
    }

    override fun getMediaReview(
        field: MediaReviewField,
        compositeDisposable: CompositeDisposable?
    ): MutableReviewType {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map { response ->
                runBlocking {
                    response.data()?.Media()?.reviews()?.edges()?.pmap { edge ->
                        MediaReviewModel().also { model ->
                            edge.node()?.let { node ->
                                model.mediaId = node.id()
                                model.rating = node.rating()
                                model.ratingAmount = node.ratingAmount()
                                model.summary = node.summary()
                                model.userRating = node.userRating()?.ordinal
                                model.user = node.user()?.let {
                                    MediaReviewUserModel().also { model ->
                                        model.userId = it.id()
                                        model.name = it.name()
                                        model.avatar = UserAvatarImageModel().also { imageModel ->
                                            imageModel.medium = it.avatar()?.medium()
                                            imageModel.large = it.avatar()?.large()
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mediaReviewLiveData.value = Resource.success(it)
            }, {
                Timber.e(it)
                mediaReviewLiveData.value = Resource.error(it.message ?: ERROR, null)
            })

        compositeDisposable?.add(disposable)
        return mediaReviewLiveData
    }

    override fun getMediaStats(
        field: MediaStatsField,
        compositeDisposable: CompositeDisposable?
    ): MutableStatsType {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map { response ->
                response.data()?.Media()?.let { media ->
                    MediaStatsModel().also { model ->
                        runBlocking {
                            model.rankings = media.rankings()?.pmap { rank ->
                                MediaStatsRankingModel().also { rankModel ->
                                    rankModel.id = rank.id()
                                    rankModel.context = rank.context()
                                    rankModel.allTime = rank.allTime() ?: false
                                    rankModel.rank = rank.rank()
                                    rankModel.season = rank.season()?.ordinal
                                    rankModel.year = rank.year()
                                    rankModel.type = rank.type().ordinal
                                }
                            }


                            model.trends = media.trends()?.nodes()?.pmap { node ->
                                MediaStatsTrendsModel().also { trendModel ->
                                    trendModel.date = node.date()
                                    trendModel.trending = node.trending()
                                }
                            }

                            model.statusDistribution =
                                media.stats()?.statusDistribution()?.pmap { status ->
                                    MediaStatsStatusDistributionModel().also { statusModel ->
                                        statusModel.amount = status.amount()
                                        statusModel.status = status.status()?.ordinal
                                    }
                                }

                            model.scoreDistribution =
                                media.stats()?.scoreDistribution()?.pmap { score ->
                                    MediaStatsScoreDistributionModel().also { scoreModel ->
                                        scoreModel.amount = score.amount()
                                        scoreModel.score = score.score()
                                    }
                                }

                        }
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mediaStatsLiveData.value = Resource.success(it)
            }, {
                Timber.e(it)
                mediaStatsLiveData.value = Resource.error(it.message ?: ERROR, null)
            })
        compositeDisposable?.add(disposable)
        return mediaStatsLiveData
    }

}