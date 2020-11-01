package com.revolgenx.anilib.service.user

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.UserFollowersQuery
import com.revolgenx.anilib.UserFollowingQuery
import com.revolgenx.anilib.constant.SearchTypes
import com.revolgenx.anilib.field.user.UserFavouriteField
import com.revolgenx.anilib.field.user.UserFollowerField
import com.revolgenx.anilib.field.user.UserProfileField
import com.revolgenx.anilib.field.user.UserToggleFollowField
import com.revolgenx.anilib.markwon.MarkwonImpl
import com.revolgenx.anilib.model.*
import com.revolgenx.anilib.model.character.CharacterNameModel
import com.revolgenx.anilib.model.user.*
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.network.converter.getCommonMedia
import com.revolgenx.anilib.repository.util.ERROR
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class UserServiceImpl(private val baseGraphRepository: BaseGraphRepository) : UserService {
    override var userProfileLiveData: MutableLiveData<Resource<UserProfileModel>> =
        MutableLiveData()
    override val userFollowerCountLiveData: MutableLiveData<Resource<UserFollowerCountModel>> =
        MutableLiveData()

    override fun getUserProfile(
        userProfileField: UserProfileField,
        compositeDisposable: CompositeDisposable
    ) {
        val disposable =
            baseGraphRepository.request(userProfileField.toQueryOrMutation()).map { response ->
                response.data()?.User()?.let {
                    UserProfileModel().also { model ->
                        model.baseId = it.id()
                        model.userId = it.id()
                        model.userName = it.name()
                        model.avatar = it.avatar()?.let { ava ->
                            UserAvatarImageModel().also { img ->
                                img.large = ava.large()
                                img.medium = ava.medium()
                            }
                        }
                        model.bannerImage = it.bannerImage() ?: model.avatar?.image
                        model.isBlocked = it.isBlocked
                        model.isFollower = it.isFollower
                        model.isFollowing = it.isFollowing
                        model.about = MarkwonImpl.createMarkwonCompatible(it.about() ?: "")
                        model.siteUrl = it.siteUrl()
                        it.statistics()?.let { stats ->
                            stats.anime()?.let { a ->
                                model.totalAnime = a.count()
                                model.daysWatched = a.minutesWatched().toDouble().div(60).div(24)
                                model.animeMeanScore = a.meanScore()
                                a.genres()?.forEach { g ->
                                    model.genreOverView[g.genre()!!] = g.count()
                                }
                            }
                            stats.manga()?.let { m ->
                                model.totalManga = m.count()
                                model.chaptersRead = m.chaptersRead()
                                model.mangaMeanScore = m.meanScore()
                                m.genres()?.forEach { g ->
                                    if (model.genreOverView.containsKey(g.genre())) {
                                        model.genreOverView[g.genre()!!] =
                                            model.genreOverView[g.genre()!!]!!.plus(g.count())
                                    } else {
                                        model.genreOverView[g.genre()!!] = g.count()
                                    }
                                }
                            }
                        }
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
                .subscribe({ model ->
                    model?.genreOverView?.toList()?.sortedByDescending { (_, value) -> value }
                        ?.toMap()
                        ?.toMutableMap()?.let {
                            model.genreOverView = it
                        }
                    userProfileLiveData.value = Resource.success(model)
                    model?.let {
                        getTotalFollowing(userProfileField, compositeDisposable)
                    }
                }, {
                    Timber.w(it)
                    userProfileLiveData.value = Resource.error(it.message ?: ERROR, null, it)
                })

        compositeDisposable.add(disposable)
    }

    override fun getTotalFollower(
        userProfileField: UserProfileField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<Int>) -> Unit)?
    ) {
        val disposable =
            baseGraphRepository.request(userProfileField.userTotalFollowerField.toQueryOrMutation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (userFollowerCountLiveData.value == null) {
                        userFollowerCountLiveData.value =
                            Resource.success(UserFollowerCountModel().also { mod ->
                                mod.followers = response.data()?.Page()?.pageInfo()?.total()
                            })
                    } else {
                        userFollowerCountLiveData.value =
                            userFollowerCountLiveData.value?.also { mod ->
                                mod.data?.followers = response.data()?.Page()?.pageInfo()?.total()
                            }
                    }
                }, {
                    Timber.w(it)
                })
        compositeDisposable.add(disposable)
    }

    override fun getTotalFollowing(
        userProfileField: UserProfileField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<Int>) -> Unit)?
    ) {
        val disposable =
            baseGraphRepository.request(userProfileField.userTotalFollowingField.toQueryOrMutation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (userFollowerCountLiveData.value == null) {
                        userFollowerCountLiveData.value =
                            Resource.success(UserFollowerCountModel().also { mod ->
                                mod.following = response.data()?.Page()?.pageInfo()?.total()
                            })
                    } else {
                        userFollowerCountLiveData.value =
                            userFollowerCountLiveData.value?.also { mod ->
                                mod.data?.following = response.data()?.Page()?.pageInfo()?.total()
                            }
                    }
                }, {
                    Timber.w(it)
                })
        compositeDisposable.add(disposable)
    }

    override fun getFollowersUsers(
        userField: UserFollowerField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<UserFollowersModel>>) -> Unit
    ) {
        val disposable = baseGraphRepository.request(userField.toQueryOrMutation())
            .map { response ->
                val data = response.data()
                if (data is UserFollowersQuery.Data)
                    (data as? UserFollowersQuery.Data)?.Page()?.followers()?.map {
                        UserFollowersModel().also { model ->
                            model.userId = it.id()
                            model.userName = it.name()
                            model.avatar = it.avatar()?.let {
                                UserAvatarImageModel().also { img -> img.medium = it.medium() }
                            }
                        }
                    }
                else
                    (data as? UserFollowingQuery.Data)?.Page()?.following()?.map {
                        UserFollowersModel().also { model ->
                            model.userId = it.id()
                            model.userName = it.name()
                            model.avatar = it.avatar()?.let {
                                UserAvatarImageModel().also { img -> img.medium = it.medium() }
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


    override fun getUserFavourite(
        field: UserFavouriteField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<BaseModel>>) -> Unit
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation())
            .map {
                val type = it.data()?.User()?.favourites()?.let { fav ->
                    when {
                        fav.anime() != null -> {
                            SearchTypes.ANIME
                        }
                        fav.manga() != null -> {
                            SearchTypes.MANGA
                        }
                        fav.characters() != null -> {
                            SearchTypes.CHARACTER
                        }
                        fav.staff() != null -> {
                            SearchTypes.STAFF
                        }
                        fav.studios() != null -> {
                            SearchTypes.STUDIO
                        }
                        else -> {
                            SearchTypes.UNKNOWN
                        }
                    }
                }
                val data = it.data()?.User()?.favourites()
                when (type) {
                    SearchTypes.ANIME -> {
                        data?.anime()?.nodes()
                            ?.filter { if(field.canShowAdult) true else it.fragments().commonMediaContent().isAdult == false }
                            ?.map { map ->
                                map.fragments().commonMediaContent().getCommonMedia(MediaFavouriteModel())
                            }
                    }
                    SearchTypes.MANGA -> {
                        data?.manga()?.nodes()
                            ?.filter {if(field.canShowAdult) true else it.fragments().commonMediaContent().isAdult == false }
                            ?.map { map ->
                                map.fragments().commonMediaContent().getCommonMedia(MediaFavouriteModel())
                            }
                    }
                    SearchTypes.CHARACTER -> {
                        data?.characters()?.nodes()?.map { map ->
                            map.fragments().narrowCharacterContent().let {
                                CharacterFavouriteModel().also { model ->
                                    model.characterId = it.id()
                                    model.name = it.name()?.let {
                                        CharacterNameModel().also { name ->
                                            name.full = it.full()
                                        }
                                    }
                                    model.characterImageModel = it.image()?.let {
                                        CharacterImageModel().also { img ->
                                            img.large = it.large()
                                            img.medium = it.medium()
                                        }
                                    }
                                }
                            }
                        }
                    }

                    SearchTypes.STAFF -> {
                        data?.staff()?.nodes()?.map { map ->
                            map.fragments().narrowStaffContent().let {
                                StaffFavouriteModel().also { model ->
                                    model.staffId = it.id()
                                    model.staffName = it.name()?.let {
                                        StaffNameModel().also { name ->
                                            name.full = it.full()
                                        }
                                    }
                                    model.staffImage = it.image()?.let {
                                        StaffImageModel().also { img ->
                                            img.medium = it.medium()
                                            img.large = it.large()
                                        }
                                    }
                                }
                            }
                        }
                    }

                    SearchTypes.STUDIO -> {
                        data?.studios()?.nodes()?.map { map ->
                            map.fragments().studioContent().let {
                                StudioFavouriteModel().also { model ->
                                    model.studioId = it.id()
                                    model.studioName = it.name()
                                    model.studioMedia =
                                        it.media()?.nodes()
                                            ?.filter {if(field.canShowAdult) true else it.fragments().commonMediaContent().isAdult == false }
                                            ?.map {
                                                it.fragments().commonMediaContent().getCommonMedia(MediaFavouriteModel())
                                            }
                                }
                            }
                        }?.filter { it.studioMedia.isNullOrEmpty().not() }
                    }
                    else -> {
                        emptyList()
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it ?: emptyList()))
            }, {
                Timber.w(it)
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable.add(disposable)
    }


    override fun toggleUserFollowing(
        field: UserToggleFollowField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<UserProfileModel>) -> Unit)?
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation()).map {
            it.data()?.ToggleFollow()?.let {
                UserProfileModel().also { model ->
                    model.userId = it.id()
                    model.isFollowing = it.isFollowing
                }
            }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it?.isFollowing?.let {
                    userProfileLiveData.value?.data?.isFollowing = it
                }
                callback?.invoke(Resource.success(it))
            }, {
                Timber.e(it)
                callback?.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }
}