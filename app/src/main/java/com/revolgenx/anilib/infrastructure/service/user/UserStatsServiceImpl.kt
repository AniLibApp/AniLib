package com.revolgenx.anilib.infrastructure.service.user

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.UserStatsQuery
import com.revolgenx.anilib.data.field.stats.UserStatsField
import com.revolgenx.anilib.data.field.user.stats.StatsOverviewField
import com.revolgenx.anilib.data.model.user.stats.*
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.annotations.Nullable
import timber.log.Timber

class UserStatsServiceImpl(private val baseGraphRepository: BaseGraphRepository) :
    UserStatsService {
    override val statsOverviewLiveData: MutableLiveData<Resource<StatsOverviewModel>> =
        MutableLiveData()

    override fun getStatsOverview(
        field: StatsOverviewField,
        compositeDisposable: CompositeDisposable
    ) {
        val disposable =
            baseGraphRepository.request(field.toQueryOrMutation()).map {
                if (it.data()?.User()?.statistics()?.anime() != null) {
                    it.data()?.User()?.statistics()?.anime()
                        ?.let { anime ->
                            StatsOverviewModel().also { model ->
                                model.id = field.userId
                                model.name = field.userName
                                model.count = anime.count()
                                model.meanScore = anime.meanScore()
                                model.standardDeviation = anime.standardDeviation()
                                model.scoreFormat = it.data()?.User()?.mediaListOptions()?.scoreFormat()?.ordinal
                                model.daysWatched = anime.minutesWatched().toFloat()
                                model.episodesWatched = anime.episodesWatched()

                                model.scoresDistribution = anime.scores()?.map { scr ->
                                    StatsOverviewScoreModel().apply {
                                        count = scr.count()
                                        meanScore = scr.meanScore()
                                        minutesWatched = scr.minutesWatched()
                                        score = scr.score()
                                    }
                                }


                                model.statusDistribution = anime.statuses()?.map { stat ->
                                    StatsStatusDistributionModel().apply {
                                        count = stat.count()
                                        meanScore = stat.meanScore()
                                        minutesWatched = stat.minutesWatched()
                                        status = stat.status()?.ordinal
                                    }
                                }

                                model.formatDistribution = anime.formats()?.map { fmt ->
                                    StatsFormatDistributionModel().apply {
                                        count = fmt.count()
                                        meanScore = fmt.meanScore()
                                        minutesWatched = fmt.minutesWatched()
                                        format = fmt.format()?.ordinal
                                    }
                                }

                                model.countryDistribution = anime.countries()?.map { cnt ->
                                    StatsCountryDistributionModel().apply {
                                        count = cnt.count()
                                        meanScore = cnt.meanScore()
                                        minutesWatched = cnt.minutesWatched()
                                        country = cnt.country()?.toString()
                                    }
                                }

                                model.releaseYear = anime.releaseYears()?.map { yr ->
                                    StatsYearModel().apply {
                                        count = yr.count()
                                        meanScore = yr.meanScore()
                                        minutesWatched = yr.minutesWatched()
                                        year = yr.releaseYear()
                                    }
                                }?.sortedWith(compareBy { it.year })

                                model.watchYear = anime.startYears()?.map { yr ->
                                    StatsYearModel().apply {
                                        count = yr.count()
                                        meanScore = yr.meanScore()
                                        minutesWatched = yr.minutesWatched()
                                        year = yr.startYear()
                                    }
                                }?.sortedWith(compareBy { it.year })
                            }
                        }
                } else {
                    it.data()?.User()?.statistics()?.manga()
                        ?.let { manga ->
                            StatsOverviewModel().also { model ->
                                model.id = field.userId
                                model.name = field.userName
                                model.count = manga.count()
                                model.meanScore = manga.meanScore()
                                model.standardDeviation = manga.standardDeviation()
                                model.scoreFormat = it.data()?.User()?.mediaListOptions()?.scoreFormat()?.ordinal
                                model.volumesRead = manga.volumesRead()
                                model.chaptersRead = manga.chaptersRead()

                                model.scoresDistribution = manga.scores()?.map { scr ->
                                    StatsOverviewScoreModel().apply {
                                        count = scr.count()
                                        meanScore = scr.meanScore()
                                        minutesWatched = scr.minutesWatched()
                                        score = scr.score()
                                    }
                                }


                                model.statusDistribution = manga.statuses()?.map { stat ->
                                    StatsStatusDistributionModel().apply {
                                        count = stat.count()
                                        meanScore = stat.meanScore()
                                        minutesWatched = stat.minutesWatched()
                                        status = stat.status()?.ordinal
                                    }
                                }

                                model.formatDistribution = manga.formats()?.map { fmt ->
                                    StatsFormatDistributionModel().apply {
                                        count = fmt.count()
                                        meanScore = fmt.meanScore()
                                        minutesWatched = fmt.minutesWatched()
                                        format = fmt.format()?.ordinal
                                    }
                                }

                                model.countryDistribution = manga.countries()?.map { cnt ->
                                    StatsCountryDistributionModel().apply {
                                        count = cnt.count()
                                        meanScore = cnt.meanScore()
                                        minutesWatched = cnt.minutesWatched()
                                        country = cnt.country()?.toString()
                                    }
                                }

                                model.releaseYear = manga.releaseYears()?.map { yr ->
                                    StatsYearModel().apply {
                                        count = yr.count()
                                        meanScore = yr.meanScore()
                                        minutesWatched = yr.minutesWatched()
                                        year = yr.releaseYear()
                                    }
                                }?.sortedWith(compareBy { it.year })

                                model.watchYear = manga.startYears()?.map { yr ->
                                    StatsYearModel().apply {
                                        count = yr.count()
                                        meanScore = yr.meanScore()
                                        minutesWatched = yr.minutesWatched()
                                        year = yr.startYear()
                                    }
                                }?.sortedWith(compareBy { it.year })
                            }
                        }
                }
            }.observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    statsOverviewLiveData.value = Resource.success(it)
                }, {
                    Timber.e(it)
                    statsOverviewLiveData.value = Resource.error(it.message ?: ERROR, null, it)
                })

        compositeDisposable.add(disposable)
    }

    override fun getUserStats(
        field: UserStatsField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<BaseStatsModel>>) -> Unit
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation()).map {
            it.data()?.User()?.statistics()?.let {
                if (it.anime() != null) {
                    if (it.anime()!!.genres() != null) {
                        getStatsGenreModel(it.anime()!!.genres()!!)
                    } else if (it.anime()!!.tags() != null) {
                        getStatsTagModel(it.anime()!!.tags()!!)
                    } else if (it.anime()!!.studios() != null) {
                        getStatsStudioModel(it.anime()!!.studios()!!)
                    } else if (it.anime()!!.voiceActors() != null) {
                        getStatsVoiceActorModel(it.anime()!!.voiceActors()!!)
                    } else if (it.anime()!!.staff() != null) {
                        getStatsStaffModel(it.anime()!!.staff()!!)
                    } else {
                        null
                    }
                } else {
                    if (it.manga() != null) {
                        if (it.manga()!!.genres() != null) {
                            getStatsGenreModel1(it.manga()!!.genres()!!)
                        } else if (it.manga()!!.tags() != null) {
                            getStatsTagModel1(it.manga()!!.tags()!!)
                        } else if (it.manga()!!.staff() != null) {
                            getStatsStaffModel1(it.manga()!!.staff()!!)
                        } else {
                            null
                        }
                    } else {
                        null
                    }
                }

            }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                Timber.e(it)
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }

    private fun getStatsGenreModel(genres: @Nullable MutableList<UserStatsQuery.Genre>): List<StatsGenreModel> {
        return genres.mapIndexed { index, genre ->
            StatsGenreModel().also { model ->
                model.id = index
                model.genre = genre.genre()
                model.count = genre.count()
                model.minutesWatched = genre.minutesWatched()
                model.meanScore = genre.meanScore()
                model.mediaIds = genre.mediaIds()
            }
        }
    }

    private fun getStatsGenreModel1(genres: @Nullable MutableList<UserStatsQuery.Genre1>): List<StatsGenreModel> {
        return genres.mapIndexed { index, genre ->
            StatsGenreModel().also { model ->
                model.id = index
                model.genre = genre.genre()
                model.count = genre.count()
                model.chaptersRead = genre.chaptersRead()
                model.meanScore = genre.meanScore()
                model.mediaIds = genre.mediaIds()
            }
        }
    }

    private fun getStatsTagModel(tags: @Nullable MutableList<UserStatsQuery.Tag>): List<StatsTagModel> {
        return tags.mapIndexed { _, tag ->
            StatsTagModel().also { model ->
                model.id = tag.tag()?.id()
                model.tag = tag.tag()?.name()
                model.count = tag.count()
                model.meanScore = tag.meanScore()
                model.minutesWatched = tag.minutesWatched()
                model.mediaIds = tag.mediaIds()
            }
        }
    }

    private fun getStatsTagModel1(tags: @Nullable MutableList<UserStatsQuery.Tag2>): List<StatsTagModel> {
        return tags.mapIndexed { _, tag ->
            StatsTagModel().also { model ->
                model.id = tag.tag()?.id()
                model.tag = tag.tag()?.name()
                model.count = tag.count()
                model.meanScore = tag.meanScore()
                model.chaptersRead = tag.chaptersRead()
                model.mediaIds = tag.mediaIds()
            }
        }
    }


    private fun getStatsStaffModel(staff: @Nullable MutableList<UserStatsQuery.Staff>): List<StatsStaffModel> {
        return staff.mapIndexed { _, staf ->
            StatsStaffModel().also { model ->
                model.staffId = staf.staff()?.id()
                model.name = staf.staff()?.name()?.full()
                model.count = staf.count()
                model.meanScore = staf.meanScore()
                model.minutesWatched = staf.minutesWatched()
                model.image = staf.staff()?.image()?.let { it.large() ?: it.large() }
                model.mediaIds = staf.mediaIds()
            }
        }
    }


    private fun getStatsStaffModel1(staff: @Nullable MutableList<UserStatsQuery.Staff2>): List<StatsStaffModel> {
        return staff.mapIndexed { _, staf ->
            StatsStaffModel().also { model ->
                model.staffId = staf.staff()?.id()
                model.name = staf.staff()?.name()?.full()
                model.count = staf.count()
                model.meanScore = staf.meanScore()
                model.chaptersRead = staf.chaptersRead()
                model.image = staf.staff()?.image()?.let { it.large() ?: it.large() }
                model.mediaIds = staf.mediaIds()
            }
        }
    }


    private fun getStatsStudioModel(studios: @Nullable MutableList<UserStatsQuery.Studio>): List<StatsStudioModel> {
        return studios.mapIndexed { _, studio ->
            StatsStudioModel().also { model ->
                model.id = studio.studio()?.id()
                model.studio = studio.studio()?.name()
                model.count = studio.count()
                model.meanScore = studio.meanScore()
                model.minutesWatched = studio.minutesWatched()
                model.mediaIds = studio.mediaIds()
            }
        }
    }

    private fun getStatsVoiceActorModel(voiceActors: @Nullable MutableList<UserStatsQuery.VoiceActor>): List<StatsVoiceActorModel> {
        return voiceActors.mapIndexed { _, voiceActor ->
            StatsVoiceActorModel().also { model ->
                model.voiceActorId = voiceActor.voiceActor()?.id()
                model.name = voiceActor.voiceActor()?.name()?.full()
                model.count = voiceActor.count()
                model.meanScore = voiceActor.meanScore()
                model.minutesWatched = voiceActor.minutesWatched()
                model.image = voiceActor.voiceActor()?.image()?.let { it.large() ?: it.medium() }
                model.mediaIds = voiceActor.mediaIds()
            }
        }
    }
}