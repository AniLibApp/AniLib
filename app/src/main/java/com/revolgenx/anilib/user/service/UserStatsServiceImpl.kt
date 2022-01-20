package com.revolgenx.anilib.user.service

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.UserStatsQuery
import com.revolgenx.anilib.app.setting.data.model.MediaListOptionModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.media.data.model.MediaTagModel
import com.revolgenx.anilib.staff.data.model.StaffImageModel
import com.revolgenx.anilib.staff.data.model.StaffModel
import com.revolgenx.anilib.staff.data.model.StaffNameModel
import com.revolgenx.anilib.studio.data.model.StudioModel
import com.revolgenx.anilib.user.data.field.UserStatisticOverviewField
import com.revolgenx.anilib.user.data.field.UserStatsField
import com.revolgenx.anilib.user.data.model.UserModel
import com.revolgenx.anilib.user.data.model.UserStatisticTypesModel
import com.revolgenx.anilib.user.data.model.stats.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.annotations.Nullable
import timber.log.Timber

class UserStatsServiceImpl(private val baseGraphRepository: BaseGraphRepository) :
    UserStatsService {
    override val statsOverviewLiveData: MutableLiveData<Resource<StatsOverviewModel>> =
        MutableLiveData()

    override fun getUserStatisticsOverview(
        field: UserStatisticOverviewField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<UserModel>) -> Unit
    ) {
        val disposable =
            baseGraphRepository.request(field.toQueryOrMutation()).map {
                it.data()?.User()?.let { user ->
                    UserModel().also { userModel ->
                        userModel.id = user.id()
                        userModel.name = user.name()
                        userModel.mediaListOptions = user.mediaListOptions()?.let { option->
                            MediaListOptionModel().also {listOptionModel->
                                listOptionModel.scoreFormat = option.scoreFormat()?.ordinal
                            }
                        }
                        userModel.statistics  = user.statistics()?.let {stats->
                            UserStatisticTypesModel().also { sTypeModel->
                                sTypeModel.anime = stats.anime()?.let {anime->
                                    UserStatisticsModel().also { model ->
                                        model.count = anime.count()
                                        model.meanScore = anime.meanScore()
                                        model.standardDeviation = anime.standardDeviation()
                                        model.minutesWatched = anime.minutesWatched()
                                        model.episodesWatched = anime.episodesWatched()

                                        model.scores = anime.scores()?.map { scr ->
                                            UserScoreStatisticModel().apply {
                                                count = scr.count()
                                                meanScore = scr.meanScore()
                                                minutesWatched = scr.minutesWatched()
                                                score = scr.score()
                                            }
                                        }
                                        model.statuses = anime.statuses()?.map { stat ->
                                            UserStatusStatisticModel().apply {
                                                count = stat.count()
                                                meanScore = stat.meanScore()
                                                minutesWatched = stat.minutesWatched()
                                                status = stat.status()?.ordinal
                                            }
                                        }
                                        model.formats = anime.formats()?.map { fmt ->
                                            UserFormatStatisticModel().apply {
                                                count = fmt.count()
                                                meanScore = fmt.meanScore()
                                                minutesWatched = fmt.minutesWatched()
                                                format = fmt.format()?.ordinal
                                            }
                                        }
                                        model.countries = anime.countries()?.map { cnt ->
                                            UserCountryStatisticModel().apply {
                                                count = cnt.count()
                                                meanScore = cnt.meanScore()
                                                minutesWatched = cnt.minutesWatched()
                                                country = cnt.country()?.toString()
                                            }
                                        }

                                        model.releaseYears = anime.releaseYears()?.map { yr ->
                                            UserReleaseYearStatisticModel().apply {
                                                count = yr.count()
                                                meanScore = yr.meanScore()
                                                minutesWatched = yr.minutesWatched()
                                                year = yr.releaseYear()
                                            }
                                        }?.sortedWith(compareBy { it.year })

                                        model.startYears = anime.startYears()?.map { yr ->
                                            UserStartYearStatisticModel().apply {
                                                count = yr.count()
                                                meanScore = yr.meanScore()
                                                minutesWatched = yr.minutesWatched()
                                                startYear = yr.startYear()
                                            }
                                        }?.sortedWith(compareBy { it.startYear })
                                    }

                                }
                                sTypeModel.manga = stats.manga()?.let {manga->
                                    UserStatisticsModel().also { model ->
                                        model.count = manga.count()
                                        model.meanScore = manga.meanScore()
                                        model.standardDeviation = manga.standardDeviation()
                                        model.volumesRead = manga.volumesRead()
                                        model.chaptersRead = manga.chaptersRead()

                                        model.scores = manga.scores()?.map { scr ->
                                            UserScoreStatisticModel().apply {
                                                count = scr.count()
                                                meanScore = scr.meanScore()
                                                minutesWatched = scr.minutesWatched()
                                                score = scr.score()
                                            }
                                        }
                                        model.statuses = manga.statuses()?.map { stat ->
                                            UserStatusStatisticModel().apply {
                                                count = stat.count()
                                                meanScore = stat.meanScore()
                                                minutesWatched = stat.minutesWatched()
                                                status = stat.status()?.ordinal
                                            }
                                        }
                                        model.formats = manga.formats()?.map { fmt ->
                                            UserFormatStatisticModel().apply {
                                                count = fmt.count()
                                                meanScore = fmt.meanScore()
                                                minutesWatched = fmt.minutesWatched()
                                                format = fmt.format()?.ordinal
                                            }
                                        }
                                        model.countries = manga.countries()?.map { cnt ->
                                            UserCountryStatisticModel().apply {
                                                count = cnt.count()
                                                meanScore = cnt.meanScore()
                                                minutesWatched = cnt.minutesWatched()
                                                country = cnt.country()?.toString()
                                            }
                                        }

                                        model.releaseYears = manga.releaseYears()?.map { yr ->
                                            UserReleaseYearStatisticModel().apply {
                                                count = yr.count()
                                                meanScore = yr.meanScore()
                                                minutesWatched = yr.minutesWatched()
                                                year = yr.releaseYear()
                                            }
                                        }?.sortedWith(compareBy { it.year })

                                        model.startYears = manga.startYears()?.map { yr ->
                                            UserStartYearStatisticModel().apply {
                                                count = yr.count()
                                                meanScore = yr.meanScore()
                                                minutesWatched = yr.minutesWatched()
                                                startYear = yr.startYear()
                                            }
                                        }?.sortedWith(compareBy { it.startYear })
                                    }

                                }
                            }
                        }

                        user.statistics()?.manga()?.let {

                        }
                    }
                }

            }.observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callback.invoke(Resource.success(it))
                }, {
                    Timber.w(it)
                    callback.invoke(Resource.error(it.message ?: ERROR, null, it))
                })

        compositeDisposable.add(disposable)
    }

    override fun getUserStats(
        field: UserStatsField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<BaseStatisticModel>>) -> Unit
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

    private fun getStatsGenreModel(genres: @Nullable MutableList<UserStatsQuery.Genre>): List<UserGenreStatisticModel> {
        return genres.map { genre ->
            UserGenreStatisticModel().also { model ->
                model.genre = genre.genre()
                model.count = genre.count()
                model.minutesWatched = genre.minutesWatched()
                model.meanScore = genre.meanScore()
                model.mediaIds = genre.mediaIds()
            }
        }
    }

    private fun getStatsGenreModel1(genres: @Nullable MutableList<UserStatsQuery.Genre1>): List<UserGenreStatisticModel> {
        return genres.map { genre ->
            UserGenreStatisticModel().also { model ->
                model.genre = genre.genre()
                model.count = genre.count()
                model.chaptersRead = genre.chaptersRead()
                model.meanScore = genre.meanScore()
                model.mediaIds = genre.mediaIds()
            }
        }
    }

    private fun getStatsTagModel(tags: @Nullable MutableList<UserStatsQuery.Tag>): List<UserTagStatisticModel> {
        return tags.map { stats ->
            UserTagStatisticModel().also { model ->
                model.count = stats.count()
                model.meanScore = stats.meanScore()
                model.minutesWatched = stats.minutesWatched()
                model.mediaIds = stats.mediaIds()
                stats.tag()?.let {
                    model.tag = MediaTagModel().also { t ->
                        t.id = it.id()
                        t.name = it.name()
                    }
                }
            }
        }
    }

    private fun getStatsTagModel1(tags: @Nullable MutableList<UserStatsQuery.Tag2>): List<UserTagStatisticModel> {
        return tags.map { stats ->
            UserTagStatisticModel().also { model ->
                model.count = stats.count()
                model.meanScore = stats.meanScore()
                model.chaptersRead = stats.chaptersRead()
                model.mediaIds = stats.mediaIds()
                model.tag = stats.tag()?.let {
                    MediaTagModel().also { t ->
                        t.id = it.id()
                        t.name = it.name()
                    }
                }
            }
        }
    }


    private fun getStatsStaffModel(staff: @Nullable MutableList<UserStatsQuery.Staff>): List<UserStaffStatisticModel> {
        return staff.map { stats ->
            UserStaffStatisticModel().also { model ->
                model.count = stats.count()
                model.meanScore = stats.meanScore()
                model.minutesWatched = stats.minutesWatched()
                model.mediaIds = stats.mediaIds()
                model.staff = stats.staff()?.let {
                    StaffModel().also { staffModel ->
                        staffModel.id = it.id()
                        staffModel.name = it.name()?.let { n ->
                            StaffNameModel().also { name ->
                                name.full = n.full()
                            }
                        }
                        staffModel.image = it.image()?.let { i ->
                            StaffImageModel().also { image ->
                                image.large = i.large()
                                image.medium = i.medium()
                            }
                        }
                    }
                }
            }
        }
    }


    private fun getStatsStaffModel1(staff: @Nullable MutableList<UserStatsQuery.Staff2>): List<UserStaffStatisticModel> {
        return staff.map { stats ->
            UserStaffStatisticModel().also { model ->
                model.count = stats.count()
                model.meanScore = stats.meanScore()
                model.chaptersRead = stats.chaptersRead()
                model.mediaIds = stats.mediaIds()
                model.staff = stats.staff()?.let {
                    StaffModel().also { staffModel ->
                        staffModel.id = it.id()
                        staffModel.name = it.name()?.let { n ->
                            StaffNameModel().also { name ->
                                name.full = n.full()
                            }
                        }
                        staffModel.image = it.image()?.let { i ->
                            StaffImageModel().also { image ->
                                image.large = i.large()
                                image.medium = i.medium()
                            }
                        }
                    }
                }
            }
        }
    }


    private fun getStatsStudioModel(studios: @Nullable MutableList<UserStatsQuery.Studio>): List<UserStudioStatisticModel> {
        return studios.map { stats ->
            UserStudioStatisticModel().also { model ->
                model.count = stats.count()
                model.meanScore = stats.meanScore()
                model.minutesWatched = stats.minutesWatched()
                model.mediaIds = stats.mediaIds()
                model.studio = stats.studio()?.let {
                    StudioModel().also { studioModel ->
                        studioModel.id = it.id()
                        studioModel.studioName = it.name()
                    }
                }
            }
        }
    }

    private fun getStatsVoiceActorModel(voiceActors: @Nullable MutableList<UserStatsQuery.VoiceActor>): List<UserVoiceActorStatisticModel> {
        return voiceActors.map { stats ->
            UserVoiceActorStatisticModel().also { model ->
                model.count = stats.count()
                model.meanScore = stats.meanScore()
                model.minutesWatched = stats.minutesWatched()
                model.mediaIds = stats.mediaIds()
                model.voiceActor = stats.voiceActor()?.let {
                    StaffModel().also { m ->
                        m.id = it.id()
                        m.name = it.name()?.let {
                            StaffNameModel().also { n ->
                                n.full = it.full()
                            }
                        }
                        m.image = it.image()?.let {
                            StaffImageModel().also { i ->
                                i.large = it.large()
                                i.medium = it.medium()
                            }
                        }
                    }
                }
            }
        }
    }
}