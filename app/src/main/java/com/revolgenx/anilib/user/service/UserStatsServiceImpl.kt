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
import com.revolgenx.anilib.staff.data.model.toModel
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
                it.data?.user?.let { user ->
                    UserModel().also { userModel ->
                        userModel.id = user.id
                        userModel.name = user.name
                        userModel.mediaListOptions = user.mediaListOptions?.let { option ->
                            MediaListOptionModel().also { listOptionModel ->
                                listOptionModel.scoreFormat = option.scoreFormat?.ordinal
                            }
                        }
                        userModel.statistics = user.statistics?.let { stats ->
                            UserStatisticTypesModel().also { sTypeModel ->
                                sTypeModel.anime = stats.anime?.let { anime ->
                                    UserStatisticsModel().also { model ->
                                        model.count = anime.count
                                        model.meanScore = anime.meanScore
                                        model.standardDeviation = anime.standardDeviation
                                        model.minutesWatched = anime.minutesWatched
                                        model.episodesWatched = anime.episodesWatched

                                        model.scores = anime.scores?.filterNotNull()?.map { scr ->
                                            UserScoreStatisticModel().apply {
                                                count = scr.count
                                                meanScore = scr.meanScore
                                                minutesWatched = scr.minutesWatched
                                                score = scr.score
                                            }
                                        }
                                        model.statuses =
                                            anime.statuses?.filterNotNull()?.map { stat ->
                                                UserStatusStatisticModel().apply {
                                                    count = stat.count
                                                    meanScore = stat.meanScore
                                                    minutesWatched = stat.minutesWatched
                                                    status = stat.status?.ordinal
                                                }
                                            }
                                        model.formats = anime.formats?.filterNotNull()?.map { fmt ->
                                            UserFormatStatisticModel().apply {
                                                count = fmt.count
                                                meanScore = fmt.meanScore
                                                minutesWatched = fmt.minutesWatched
                                                format = fmt.format?.ordinal
                                            }
                                        }
                                        model.countries =
                                            anime.countries?.filterNotNull()?.map { cnt ->
                                                UserCountryStatisticModel().apply {
                                                    count = cnt.count
                                                    meanScore = cnt.meanScore
                                                    minutesWatched = cnt.minutesWatched
                                                    country = cnt.country?.toString()
                                                }
                                            }

                                        model.releaseYears =
                                            anime.releaseYears?.filterNotNull()?.map { yr ->
                                                UserReleaseYearStatisticModel().apply {
                                                    count = yr.count
                                                    meanScore = yr.meanScore
                                                    minutesWatched = yr.minutesWatched
                                                    year = yr.releaseYear
                                                }
                                            }?.sortedWith(compareBy { it.year })

                                        model.startYears =
                                            anime.startYears?.filterNotNull()?.map { yr ->
                                                UserStartYearStatisticModel().apply {
                                                    count = yr.count
                                                    meanScore = yr.meanScore
                                                    minutesWatched = yr.minutesWatched
                                                    startYear = yr.startYear
                                                }
                                            }?.sortedWith(compareBy { it.startYear })
                                    }

                                }
                                sTypeModel.manga = stats.manga?.let { manga ->
                                    UserStatisticsModel().also { model ->
                                        model.count = manga.count
                                        model.meanScore = manga.meanScore
                                        model.standardDeviation = manga.standardDeviation
                                        model.volumesRead = manga.volumesRead
                                        model.chaptersRead = manga.chaptersRead

                                        model.scores = manga.scores?.filterNotNull()?.map { scr ->
                                            UserScoreStatisticModel().apply {
                                                count = scr.count
                                                meanScore = scr.meanScore
                                                minutesWatched = scr.minutesWatched
                                                score = scr.score
                                            }
                                        }
                                        model.statuses =
                                            manga.statuses?.filterNotNull()?.map { stat ->
                                                UserStatusStatisticModel().apply {
                                                    count = stat.count
                                                    meanScore = stat.meanScore
                                                    minutesWatched = stat.minutesWatched
                                                    status = stat.status?.ordinal
                                                }
                                            }
                                        model.formats = manga.formats?.filterNotNull()?.map { fmt ->
                                            UserFormatStatisticModel().apply {
                                                count = fmt.count
                                                meanScore = fmt.meanScore
                                                minutesWatched = fmt.minutesWatched
                                                format = fmt.format?.ordinal
                                            }
                                        }
                                        model.countries =
                                            manga.countries?.filterNotNull()?.map { cnt ->
                                                UserCountryStatisticModel().apply {
                                                    count = cnt.count
                                                    meanScore = cnt.meanScore
                                                    minutesWatched = cnt.minutesWatched
                                                    country = cnt.country?.toString()
                                                }
                                            }

                                        model.releaseYears =
                                            manga.releaseYears?.filterNotNull()?.map { yr ->
                                                UserReleaseYearStatisticModel().apply {
                                                    count = yr.count
                                                    meanScore = yr.meanScore
                                                    minutesWatched = yr.minutesWatched
                                                    year = yr.releaseYear
                                                }
                                            }?.sortedWith(compareBy { it.year })

                                        model.startYears =
                                            manga.startYears?.filterNotNull()?.map { yr ->
                                                UserStartYearStatisticModel().apply {
                                                    count = yr.count
                                                    meanScore = yr.meanScore
                                                    minutesWatched = yr.minutesWatched
                                                    startYear = yr.startYear
                                                }
                                            }?.sortedWith(compareBy { it.startYear })
                                    }

                                }
                            }
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
            it.data?.user?.statistics?.let {
                if (it.anime != null) {
                    when {
                        it.anime.genres != null -> {
                            getStatsGenreModel(it.anime.genres.filterNotNull())
                        }
                        it.anime.tags != null -> {
                            getStatsTagModel(it.anime.tags.filterNotNull())
                        }
                        it.anime.studios != null -> {
                            getStatsStudioModel(it.anime.studios.filterNotNull())
                        }
                        it.anime.voiceActors != null -> {
                            getStatsVoiceActorModel(it.anime.voiceActors.filterNotNull())
                        }
                        it.anime.staff != null -> {
                            getStatsStaffModel(it.anime.staff.filterNotNull())
                        }
                        else -> {
                            null
                        }
                    }
                } else {
                    if (it.manga != null) {
                        when {
                            it.manga.genres != null -> {
                                getStatsGenreModel1(it.manga.genres.filterNotNull())
                            }
                            it.manga.tags != null -> {
                                getStatsTagModel1(it.manga.tags.filterNotNull())
                            }
                            it.manga.staff != null -> {
                                getStatsStaffModel1(it.manga.staff.filterNotNull())
                            }
                            else -> {
                                null
                            }
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

    private fun getStatsGenreModel(genres: List<UserStatsQuery.Genre>): List<UserGenreStatisticModel> {
        return genres.map { genre ->
            UserGenreStatisticModel().also { model ->
                model.genre = genre.genre
                model.count = genre.count
                model.minutesWatched = genre.minutesWatched
                model.meanScore = genre.meanScore
                model.mediaIds = genre.mediaIds.filterNotNull()
            }
        }
    }

    private fun getStatsGenreModel1(genres: List<UserStatsQuery.Genre1>): List<UserGenreStatisticModel> {
        return genres.map { genre ->
            UserGenreStatisticModel().also { model ->
                model.genre = genre.genre
                model.count = genre.count
                model.chaptersRead = genre.chaptersRead
                model.meanScore = genre.meanScore
                model.mediaIds = genre.mediaIds.filterNotNull()
            }
        }
    }

    private fun getStatsTagModel(tags: List<UserStatsQuery.Tag>): List<UserTagStatisticModel> {
        return tags.map { stats ->
            UserTagStatisticModel().also { model ->
                model.count = stats.count
                model.meanScore = stats.meanScore
                model.minutesWatched = stats.minutesWatched
                model.mediaIds = stats.mediaIds.filterNotNull()
                model.tag = stats.tag?.let {
                    MediaTagModel(
                        id = it.id,
                        name = it.name
                    )
                }
            }
        }
    }

    private fun getStatsTagModel1(tags: List<UserStatsQuery.Tag2>): List<UserTagStatisticModel> {
        return tags.map { stats ->
            UserTagStatisticModel().also { model ->
                model.count = stats.count
                model.meanScore = stats.meanScore
                model.chaptersRead = stats.chaptersRead
                model.mediaIds = stats.mediaIds.filterNotNull()
                model.tag = stats.tag?.let {
                    MediaTagModel(
                        id = it.id,
                        name = it.name
                    )
                }
            }
        }
    }


    private fun getStatsStaffModel(staff: @Nullable List<UserStatsQuery.Staff>): List<UserStaffStatisticModel> {
        return staff.map { stats ->
            UserStaffStatisticModel().also { model ->
                model.count = stats.count
                model.meanScore = stats.meanScore
                model.minutesWatched = stats.minutesWatched
                model.mediaIds = stats.mediaIds.filterNotNull()
                model.staff = stats.staff?.let {
                    StaffModel().also { staffModel ->
                        staffModel.id = it.id
                        staffModel.name = it.name?.let { n ->
                            StaffNameModel(n.full)
                        }
                        staffModel.image = it.image?.staffImage?.toModel()
                    }
                }
            }
        }
    }


    private fun getStatsStaffModel1(staff: List<UserStatsQuery.Staff2>): List<UserStaffStatisticModel> {
        return staff.map { stats ->
            UserStaffStatisticModel().also { model ->
                model.count = stats.count
                model.meanScore = stats.meanScore
                model.chaptersRead = stats.chaptersRead
                model.mediaIds = stats.mediaIds.filterNotNull()
                model.staff = stats.staff?.let {
                    StaffModel().also { staffModel ->
                        staffModel.id = it.id
                        staffModel.name = it.name?.let { n ->
                            StaffNameModel(n.full)
                        }
                        staffModel.image = it.image?.staffImage?.toModel()
                    }
                }
            }
        }
    }


    private fun getStatsStudioModel(studios: List<UserStatsQuery.Studio>): List<UserStudioStatisticModel> {
        return studios.map { stats ->
            UserStudioStatisticModel().also { model ->
                model.count = stats.count
                model.meanScore = stats.meanScore
                model.minutesWatched = stats.minutesWatched
                model.mediaIds = stats.mediaIds.filterNotNull()
                model.studio = stats.studio?.let {
                    StudioModel().also { studioModel ->
                        studioModel.id = it.id
                        studioModel.studioName = it.name
                    }
                }
            }
        }
    }

    private fun getStatsVoiceActorModel(voiceActors: List<UserStatsQuery.VoiceActor>): List<UserVoiceActorStatisticModel> {
        return voiceActors.map { stats ->
            UserVoiceActorStatisticModel().also { model ->
                model.count = stats.count
                model.meanScore = stats.meanScore
                model.minutesWatched = stats.minutesWatched
                model.mediaIds = stats.mediaIds.filterNotNull()
                model.voiceActor = stats.voiceActor?.let {
                    StaffModel().also { m ->
                        m.id = it.id
                        m.name = it.name?.let {
                            StaffNameModel(it.full)
                        }
                        m.image = it.image?.staffImage?.toModel()
                    }
                }
            }
        }
    }
}