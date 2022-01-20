package com.revolgenx.anilib.user.service

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.UserFollowersQuery
import com.revolgenx.anilib.UserFollowingQuery
import com.revolgenx.anilib.character.data.model.CharacterImageModel
import com.revolgenx.anilib.character.data.model.CharacterModel
import com.revolgenx.anilib.character.data.model.CharacterNameModel
import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.friend.data.field.UserFriendField
import com.revolgenx.anilib.friend.data.model.FriendModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.toModel
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.media.data.model.MediaConnectionModel
import com.revolgenx.anilib.staff.data.model.StaffImageModel
import com.revolgenx.anilib.staff.data.model.StaffModel
import com.revolgenx.anilib.staff.data.model.StaffNameModel
import com.revolgenx.anilib.studio.data.model.StudioModel
import com.revolgenx.anilib.user.data.field.UserFavouriteField
import com.revolgenx.anilib.user.data.field.UserFollowerField
import com.revolgenx.anilib.user.data.field.UserProfileField
import com.revolgenx.anilib.user.data.field.UserToggleFollowField
import com.revolgenx.anilib.user.data.model.*
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
                        model.id = it.id()
                        model.name = it.name()
                        model.avatar = it.avatar()?.let { ava ->
                            UserAvatarModel().also { img ->
                                img.large = ava.large()
                                img.medium = ava.medium()
                            }
                        }
                        model.bannerImage = it.bannerImage() ?: model.avatar?.image
                        model.isBlocked = it.isBlocked ?: false
                        model.isFollower = it.isFollower ?: false
                        model.isFollowing = it.isFollowing ?: false
                        model.about = it.about() ?: ""
                        model.siteUrl = it.siteUrl()
                        it.statistics()?.let { stats ->
                            stats.anime()?.let { a ->
                                model.totalAnime = a.count()
                                model.daysWatched = a.minutesWatched().toDouble().div(60).div(24)
                                model.episodesWatched = a.episodesWatched()
                                model.animeMeanScore = a.meanScore()
                                a.genres()?.forEach { g ->
                                    model.genreOverView[g.genre()!!] = g.count()
                                }
                            }
                            stats.manga()?.let { m ->
                                model.totalManga = m.count()
                                model.chaptersRead = m.chaptersRead()
                                model.volumeRead = m.volumesRead()
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
                    Timber.e(it)
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
                            model.id = it.id()
                            model.name = it.name()
                            model.avatar = it.avatar()?.let {
                                UserAvatarModel().also { img -> img.medium = it.medium() }
                            }
                        }
                    }
                else
                    (data as? UserFollowingQuery.Data)?.Page()?.following()?.map {
                        UserFollowersModel().also { model ->
                            model.id = it.id()
                            model.name = it.name()
                            model.avatar = it.avatar()?.let {
                                UserAvatarModel().also { img -> img.medium = it.medium() }
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
                it.data()?.User()?.favourites()?.let {
                    it.anime()?.nodes()?.filter {
                        if (field.canShowAdult) true else it.fragments()
                            .commonMediaContent().isAdult == false
                    }?.map { map ->
                        map.fragments().commonMediaContent().toModel()
                    }
                    it.manga()?.nodes()?.filter {
                        if (field.canShowAdult) true else it.fragments()
                            .commonMediaContent().isAdult == false
                    }?.map { map ->
                        map.fragments().commonMediaContent().toModel()
                    }
                    it.characters()?.nodes()?.map { map ->
                        map.fragments().narrowCharacterContent().let {
                            CharacterModel().also { model ->
                                model.id = it.id()
                                model.name = it.name()?.let {
                                    CharacterNameModel().also { name ->
                                        name.full = it.full()
                                    }
                                }
                                model.image = it.image()?.let {
                                    CharacterImageModel().also { img ->
                                        img.large = it.large()
                                        img.medium = it.medium()
                                    }
                                }
                            }
                        }
                    }

                    it.staff()?.nodes()?.map { map ->
                        map.fragments().narrowStaffContent().let {
                            StaffModel().also { model ->
                                model.id = it.id()
                                model.name = it.name()?.let {
                                    StaffNameModel().also { name ->
                                        name.full = it.full()
                                    }
                                }
                                model.image = it.image()?.let {
                                    StaffImageModel().also { img ->
                                        img.medium = it.medium()
                                        img.large = it.large()
                                    }
                                }
                            }
                        }
                    }

                    it.studios()?.nodes()?.map { map ->
                        map.fragments().studioContent().let {
                            StudioModel().also { model ->
                                model.id = it.fragments().studioInfo().id()
                                model.studioName =
                                    it.fragments().studioInfo().name()
                                model.media = it.media()?.let {
                                        MediaConnectionModel().also { mediaConnection ->
                                            mediaConnection.nodes = it.nodes()?.filter {
                                                if (field.canShowAdult) true else it.fragments()
                                                    .commonMediaContent().isAdult == false
                                            }?.map {
                                                it.fragments().commonMediaContent()
                                                    .toModel()
                                            }
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


    override fun toggleUserFollowing(
        field: UserToggleFollowField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<UserProfileModel>) -> Unit)?
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation()).map {
            it.data()?.ToggleFollow()?.let {
                UserProfileModel().also { model ->
                    model.id = it.id()
                    model.isFollowing = it.isFollowing ?: false
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


    override fun getUserFriend(
        field: UserFriendField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<List<FriendModel>>) -> Unit)
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation())
            .map { response ->
                val data = response.data()
                if (data is UserFollowersQuery.Data)
                    (data as? UserFollowersQuery.Data)?.Page()?.followers()?.map {
                        FriendModel().also { model ->
                            model.id = it.id()
                            model.name = it.name()
                            model.avatar = it.avatar()?.let {
                                UserAvatarModel().also { img -> img.medium = it.medium() }
                            }
                            model.isFollower = it.isFollower ?: false
                            model.isFollowing = it.isFollowing ?: false
                        }
                    }
                else
                    (data as? UserFollowingQuery.Data)?.Page()?.following()?.map {
                        FriendModel().also { model ->
                            model.id = it.id()
                            model.name = it.name()
                            model.avatar = it.avatar()?.let {
                                UserAvatarModel().also { img -> img.medium = it.medium() }
                            }

                            model.isFollower = it.isFollower ?: false
                            model.isFollowing = it.isFollowing ?: false
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
}