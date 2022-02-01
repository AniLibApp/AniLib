package com.revolgenx.anilib.user.service

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.UserFollowersQuery
import com.revolgenx.anilib.UserFollowingQuery
import com.revolgenx.anilib.character.data.model.CharacterImageModel
import com.revolgenx.anilib.character.data.model.CharacterModel
import com.revolgenx.anilib.character.data.model.CharacterNameModel
import com.revolgenx.anilib.character.data.model.toModel
import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.friend.data.field.UserFriendField
import com.revolgenx.anilib.friend.data.model.FriendModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.media.data.model.MediaConnectionModel
import com.revolgenx.anilib.media.data.model.toModel
import com.revolgenx.anilib.staff.data.model.StaffImageModel
import com.revolgenx.anilib.staff.data.model.StaffModel
import com.revolgenx.anilib.staff.data.model.StaffNameModel
import com.revolgenx.anilib.staff.data.model.toModel
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
                response.data?.user?.let {
                    UserProfileModel().also { model ->
                        model.id = it.id
                        model.name = it.name
                        model.avatar = it.avatar?.userAvatar?.toModel()
                        model.bannerImage = it.bannerImage ?: model.avatar?.image
                        model.isBlocked = it.isBlocked ?: false
                        model.isFollower = it.isFollower ?: false
                        model.isFollowing = it.isFollowing ?: false
                        model.about = it.about ?: ""
                        model.siteUrl = it.siteUrl
                        it.statistics?.let { stats ->
                            stats.anime?.let { a ->
                                model.totalAnime = a.count
                                model.daysWatched = a.minutesWatched.toDouble().div(60).div(24)
                                model.episodesWatched = a.episodesWatched
                                model.animeMeanScore = a.meanScore
                                a.genres?.filterNotNull()?.forEach { g ->
                                    model.genreOverView[g.genre!!] = g.count
                                }
                            }
                            stats.manga?.let { m ->
                                model.totalManga = m.count
                                model.chaptersRead = m.chaptersRead
                                model.volumeRead = m.volumesRead
                                model.mangaMeanScore = m.meanScore
                                m.genres?.filterNotNull()?.forEach { g ->
                                    if (model.genreOverView.containsKey(g.genre)) {
                                        model.genreOverView[g.genre!!] =
                                            model.genreOverView[g.genre]!!.plus(g.count)
                                    } else {
                                        model.genreOverView[g.genre!!] = g.count
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
                                mod.followers = response.data?.page?.pageInfo?.total
                            })
                    } else {
                        userFollowerCountLiveData.value =
                            userFollowerCountLiveData.value?.also { mod ->
                                mod.data?.followers = response.data?.page?.pageInfo?.total
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
                                mod.following = response.data?.page?.pageInfo?.total
                            })
                    } else {
                        userFollowerCountLiveData.value =
                            userFollowerCountLiveData.value?.also { mod ->
                                mod.data?.following = response.data?.page?.pageInfo?.total
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
                val data = response.data
                if (data is UserFollowersQuery.Data)
                    (data as? UserFollowersQuery.Data)?.page?.followers?.filterNotNull()?.map {
                        UserFollowersModel().also { model ->
                            model.id = it.id
                            model.name = it.name
                            model.avatar = it.avatar?.userAvatar?.toModel()
                        }
                    }
                else
                    (data as? UserFollowingQuery.Data)?.page?.following?.filterNotNull()?.map {
                        UserFollowersModel().also { model ->
                            model.id = it.id
                            model.name = it.name
                            model.avatar = it.avatar?.userAvatar?.toModel()
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
                it.data?.user?.favourites?.let {
                    it.anime?.nodes?.filterNotNull()?.filter {
                        if (field.canShowAdult) true else it.onMedia.mediaContent.isAdult == false
                    }?.map { map ->
                        map.onMedia.mediaContent.toModel()
                    }
                    it.manga?.nodes?.filterNotNull()?.filter {
                        if (field.canShowAdult) true else it.onMedia.mediaContent.isAdult == false
                    }?.map { map ->
                        map.onMedia.mediaContent.toModel()
                    }
                    it.characters?.nodes?.filterNotNull()?.map { map ->
                        map.onCharacter.narrowCharacterContent.let {
                            CharacterModel().also { model ->
                                model.id = it.id
                                model.name = it.name?.let {
                                    CharacterNameModel(it.full)
                                }
                                model.image = it.image?.characterImage?.toModel()
                            }
                        }
                    }
                    it.staff?.nodes?.filterNotNull()?.map { map ->
                        map.onStaff.narrowStaffContent.let {
                            StaffModel().also { model ->
                                model.id = it.id
                                model.name = it.name?.let {
                                    StaffNameModel(it.full)
                                }
                                model.image = it.image?.staffImage?.toModel()
                            }
                        }
                    }

                    it.studios?.nodes?.filterNotNull()?.map { map ->
                        map.onStudio.studioContent.let {
                            StudioModel().also { model ->
                                model.id = it.id
                                model.studioName = it.name
                                model.media = it.media?.let {
                                    MediaConnectionModel().also { mediaConnection ->
                                        mediaConnection.nodes = it.nodes?.filterNotNull()?.filter {
                                            if (field.canShowAdult) true else it.mediaContent.isAdult == false
                                        }?.map {
                                            it.mediaContent.toModel()
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
            it.data?.toggleFollow?.let {
                UserProfileModel().also { model ->
                    model.id = it.id
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
                val data = response.data
                if (data is UserFollowersQuery.Data)
                    (data as? UserFollowersQuery.Data)?.page?.followers?.filterNotNull()?.map {
                        FriendModel().also { model ->
                            model.id = it.id
                            model.name = it.name
                            model.avatar = it.avatar?.userAvatar?.toModel()
                            model.isFollower = it.isFollower ?: false
                            model.isFollowing = it.isFollowing ?: false
                        }
                    }
                else
                    (data as? UserFollowingQuery.Data)?.page?.following?.filterNotNull()?.map {
                        FriendModel().also { model ->
                            model.id = it.id
                            model.name = it.name
                            model.avatar = it.avatar?.userAvatar?.toModel()
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