package com.revolgenx.anilib.service.user

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.field.user.stats.StatsOverviewField
import com.revolgenx.anilib.model.user.stats.*
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.util.ERROR
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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
                                model.userId = field.userId
                                model.userName = field.userName
                                model.count = anime.count()
                                model.meanScore = anime.meanScore()
                                model.standardDeviation = anime.standardDeviation()

                                model.daysWatched = anime.minutesWatched().toFloat()
                                model.episodesWatched = anime.episodesWatched()

                                model.scoresDistribution = anime.scores()?.map { scr ->
                                    StatsOverviewScoreModel().apply {
                                        count = scr.count()
                                        meanScore = scr.meanScore()
                                        hourWatched = scr.minutesWatched()
                                        score = scr.score()
                                    }
                                }


                                model.statusDistribution = anime.statuses()?.map { stat ->
                                    StatsStatusDistributionModel().apply {
                                        count = stat.count()
                                        meanScore = stat.meanScore()
                                        hourWatched = stat.minutesWatched()
                                        status = stat.status()?.ordinal
                                    }
                                }

                                model.formatDistribution = anime.formats()?.map { fmt ->
                                    StatsFormatDistributionModel().apply {
                                        count = fmt.count()
                                        meanScore = fmt.meanScore()
                                        hourWatched = fmt.minutesWatched()
                                        format = fmt.format()?.ordinal
                                    }
                                }

                                model.countryDistribution = anime.countries()?.map { cnt ->
                                    StatsCountryDistributionModel().apply {
                                        count = cnt.count()
                                        meanScore = cnt.meanScore()
                                        hourWatched = cnt.minutesWatched()
                                        country = cnt.country()?.toString()
                                    }
                                }

                                model.releaseYear = anime.releaseYears()?.map { yr ->
                                    StatsYearModel().apply {
                                        count = yr.count()
                                        meanScore = yr.meanScore()
                                        hourWatched = yr.minutesWatched()
                                        year = yr.releaseYear()
                                    }
                                }?.sortedWith(compareBy { it.year })

                                model.watchYear = anime.startYears()?.map { yr ->
                                    StatsYearModel().apply {
                                        count = yr.count()
                                        meanScore = yr.meanScore()
                                        hourWatched = yr.minutesWatched()
                                        year = yr.startYear()
                                    }
                                }?.sortedWith(compareBy { it.year })
                            }
                        }
                } else {
                    it.data()?.User()?.statistics()?.manga()
                        ?.let { manga ->
                            StatsOverviewModel().also { model ->
                                model.userId = field.userId
                                model.userName = field.userName
                                model.count = manga.count()
                                model.meanScore = manga.meanScore()
                                model.standardDeviation = manga.standardDeviation()

                                model.volumesRead = manga.volumesRead()
                                model.chaptersRead = manga.chaptersRead()

                                model.scoresDistribution = manga.scores()?.map { scr ->
                                    StatsOverviewScoreModel().apply {
                                        count = scr.count()
                                        meanScore = scr.meanScore()
                                        hourWatched = scr.minutesWatched()
                                        score = scr.score()
                                    }
                                }


                                model.statusDistribution = manga.statuses()?.map { stat ->
                                    StatsStatusDistributionModel().apply {
                                        count = stat.count()
                                        meanScore = stat.meanScore()
                                        hourWatched = stat.minutesWatched()
                                        status = stat.status()?.ordinal
                                    }
                                }

                                model.formatDistribution = manga.formats()?.map { fmt ->
                                    StatsFormatDistributionModel().apply {
                                        count = fmt.count()
                                        meanScore = fmt.meanScore()
                                        hourWatched = fmt.minutesWatched()
                                        format = fmt.format()?.ordinal
                                    }
                                }

                                model.countryDistribution = manga.countries()?.map { cnt ->
                                    StatsCountryDistributionModel().apply {
                                        count = cnt.count()
                                        meanScore = cnt.meanScore()
                                        hourWatched = cnt.minutesWatched()
                                        country = cnt.country()?.toString()
                                    }
                                }

                                model.releaseYear = manga.releaseYears()?.map { yr ->
                                    StatsYearModel().apply {
                                        count = yr.count()
                                        meanScore = yr.meanScore()
                                        hourWatched = yr.minutesWatched()
                                        year = yr.releaseYear()
                                    }
                                }?.sortedWith(compareBy { it.year })

                                model.watchYear = manga.startYears()?.map { yr ->
                                    StatsYearModel().apply {
                                        count = yr.count()
                                        meanScore = yr.meanScore()
                                        hourWatched = yr.minutesWatched()
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
}