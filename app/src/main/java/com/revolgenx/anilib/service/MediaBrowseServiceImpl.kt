package com.revolgenx.anilib.service

import androidx.lifecycle.LiveData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.revolgenx.anilib.BrowseSimpleMediaQuery
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
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import timber.log.Timber

class MediaBrowseServiceImpl(graphRepository: BaseGraphRepository) :
    MediaBrowseService(graphRepository) {

    override fun getSimpleMedia(
        mediaId: Int,
        compositeDisposable: CompositeDisposable
    ): LiveData<Resource<MediaBrowseMediaModel>> {

        val disposable = graphRepository.request(
            BrowseSimpleMediaQuery.builder()
                .mediaId(mediaId)
                .build()
        )
            .map {
                it.data()?.Media()?.let {
                    MediaBrowseMediaModel().also { model ->
                        model.mediaId = it.id()
                        model.title = it.title()?.fragments()?.mediaTitle()?.let {
                            TitleModel().also { tModel ->
                                tModel.userPreferred = it.userPreferred()
                                tModel.native = it.native_()
                                tModel.romaji = it.romaji()
                                tModel.english = it.english()
                            }
                        }
                        model.coverImage = it.coverImage()?.let {
                            CoverImageModel().also { cModel ->
                                cModel.extraLarge = it.extraLarge()
                                cModel.large = it.large()
                                cModel.medium = it.medium()
                            }
                        }
                        model.bannerImage = it.bannerImage() ?: model.coverImage?.largeImage
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                simpleMediaLiveData.value = Resource.success(it)
            }, {
                simpleMediaLiveData.value = Resource.error(it.message ?: ERROR, null, it)
            })

        compositeDisposable.add(disposable)
        return simpleMediaLiveData
    }

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
                Timber.w(it)
                mediaOverviewLiveData.value = Resource.error(it.message ?: ERROR, null, it)
            })

        compositeDisposable?.add(disposable)

        return mediaOverviewLiveData
    }

    override fun getMediaWatch(
        field: MediaWatchField,
        compositeDisposable: CompositeDisposable?,
        resourceCallback: (Resource<List<MediaWatchModel>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map { response ->
                response.data()?.Media()!!.toModel()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it))
            }, {
                Timber.w(it)
                Resource.success(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable?.add(disposable)
    }

    override fun getMediaCharacter(
        field: MediaCharacterField,
        compositeDisposable: CompositeDisposable?,
        resourceCallback: (Resource<List<MediaCharacterModel>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map { response ->
                runBlocking {
                    response.data()?.Media()?.characters()?.edges()?.pmap {
                        MediaCharacterModel().also { charac ->
                            charac.characterId = it.node()?.id()
                            charac.role = it.role()?.ordinal
                            it.node()?.let { node ->
                                charac.name = node.name()?.full()
                                charac.characterImageModel = node.image()?.let {
                                    CharacterImageModel().apply {
                                        large = it.large()
                                        medium = it.medium()
                                    }
                                }
                            }
                            charac.siteUrl = it.node()?.siteUrl()
                            charac.voiceActor = it.voiceActors()?.firstOrNull()?.let {
                                VoiceActorModel().also { model ->
                                    model.actorId = it.id()
                                    model.name = it.name()?.full()
                                    model.language = it.language()?.ordinal
                                    model.voiceActorImageModel = it.image()?.let {
                                        VoiceActorImageModel().apply {
                                            large = it.large()
                                            medium = it.medium()
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
                resourceCallback.invoke(Resource.success(it))
            }, {
                Timber.w(it)
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable?.add(disposable)
    }

    override fun getMediaStaff(
        field: MediaStaffField,
        compositeDisposable: CompositeDisposable?,
        resourceCallback: (Resource<List<MediaStaffModel>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                runBlocking {
                    it.data()?.Media()?.staff()?.edges()?.pmap { s ->
                        MediaStaffModel().also { model ->
                            model.role = s.role()
                            s.node()?.let { staff ->
                                model.staffId = staff.id()
                                model.name = staff.name()?.full()
                                model.image = staff.image()?.let {
                                    StaffImageModel().also { imgModel ->
                                        imgModel.large = it.large()
                                        imgModel.medium = it.medium()
                                    }
                                }
                            }
                        }
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it))
            }, {
                Timber.w(it)
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable?.add(disposable)
    }

    override fun getMediaReview(
        field: MediaReviewField,
        compositeDisposable: CompositeDisposable?,
        resourceCallback: (Resource<List<MediaReviewModel>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map { response ->
                runBlocking {
                    response.data()?.Media()?.reviews()?.edges()?.pmap { edge ->
                        MediaReviewModel().also { model ->
                            edge.node()?.let { node ->
                                model.reviewId = node.id()
                                model.rating = node.rating()
                                model.ratingAmount = node.ratingAmount()
                                model.summary = node.summary()
                                model.userRating = node.userRating()?.ordinal
                                model.user = node.user()?.let {
                                    MediaReviewUserModel().also { model ->
                                        model.userId = it.id()
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
                resourceCallback.invoke(Resource.success(it))
            }, {
                Timber.w(it)
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable?.add(disposable)
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
                                    rankModel.rankType = rank.type().ordinal
                                }
                            }


                            model.trends = media.trends()?.nodes()?.pmap { node ->
                                MediaStatsTrendsModel().also { trendModel ->
                                    trendModel.date = node.date()
                                    trendModel.trending = node.trending()
                                }
                            }

                            model.trendsEntry = model.trends?.sortedBy { it.date }?.pmap {
                                it.date!!.let { date ->
                                    val day = LocalDateTime.ofInstant(
                                        Instant.ofEpochSecond(date.toLong()),
                                        ZoneId.systemDefault()
                                    ).dayOfMonth
                                    Entry(day.toFloat(), it.trending?.toFloat() ?: 0f)
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
                Timber.w(it)
                mediaStatsLiveData.value = Resource.error(it.message ?: ERROR, null, it)
            })
        compositeDisposable?.add(disposable)
        return mediaStatsLiveData
    }

}