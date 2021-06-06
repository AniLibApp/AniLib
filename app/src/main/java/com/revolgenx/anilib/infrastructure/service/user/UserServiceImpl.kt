package com.revolgenx.anilib.infrastructure.service.user

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.UserFollowersQuery
import com.revolgenx.anilib.UserFollowingQuery
import com.revolgenx.anilib.constant.SearchTypes
import com.revolgenx.anilib.data.field.friend.UserFriendField
import com.revolgenx.anilib.data.field.user.UserFavouriteField
import com.revolgenx.anilib.data.field.user.UserFollowerField
import com.revolgenx.anilib.data.field.user.UserProfileField
import com.revolgenx.anilib.data.field.user.UserToggleFollowField
import com.revolgenx.anilib.markwon.MarkwonImpl
import com.revolgenx.anilib.data.model.*
import com.revolgenx.anilib.data.model.character.CharacterImageModel
import com.revolgenx.anilib.data.model.character.CharacterNameModel
import com.revolgenx.anilib.data.model.staff.StaffImageModel
import com.revolgenx.anilib.data.model.staff.StaffNameModel
import com.revolgenx.anilib.data.model.user.*
import com.revolgenx.anilib.data.model.user.container.CharacterFavouriteModel
import com.revolgenx.anilib.data.model.user.container.MediaFavouriteModel
import com.revolgenx.anilib.data.model.user.container.StaffFavouriteModel
import com.revolgenx.anilib.data.model.user.container.StudioFavouriteModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.getCommonMedia
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
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
                        model.id = it.id()
                        model.name = it.name()
                        model.avatar = it.avatar()?.let { ava ->
                            AvatarModel().also { img ->
                                img.large = ava.large()
                                img.medium = ava.medium()
                            }
                        }
                        model.bannerImage = it.bannerImage() ?: model.avatar?.image
                        model.isBlocked = it.isBlocked ?: false
                        model.isFollower = it.isFollower ?: false
                        model.isFollowing = it.isFollowing ?: false
                        model.about = MarkwonImpl.createMarkwonCompatible(it.about() ?: "")
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
                            model.id = it.id()
                            model.name = it.name()
                            model.avatar = it.avatar()?.let {
                                AvatarModel().also { img -> img.medium = it.medium() }
                            }
                        }
                    }
                else
                    (data as? UserFollowingQuery.Data)?.Page()?.following()?.map {
                        UserFollowersModel().also { model ->
                            model.id = it.id()
                            model.name = it.name()
                            model.avatar = it.avatar()?.let {
                                AvatarModel().also { img -> img.medium = it.medium() }
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
                            ?.filter {
                                if (field.canShowAdult) true else it.fragments()
                                    .commonMediaContent().isAdult == false
                            }
                            ?.map { map ->
                                map.fragments().commonMediaContent().getCommonMedia(
                                    MediaFavouriteModel()
                                )
                            }
                    }
                    SearchTypes.MANGA -> {
                        data?.manga()?.nodes()
                            ?.filter {
                                if (field.canShowAdult) true else it.fragments()
                                    .commonMediaContent().isAdult == false
                            }
                            ?.map { map ->
                                map.fragments().commonMediaContent().getCommonMedia(
                                    MediaFavouriteModel()
                                )
                            }
                    }
                    SearchTypes.CHARACTER -> {
                        data?.characters()?.nodes()?.map { map ->
                            map.fragments().narrowCharacterContent().let {
                                CharacterFavouriteModel().also { model ->
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
                    }

                    SearchTypes.STAFF -> {
                        data?.staff()?.nodes()?.map { map ->
                            map.fragments().narrowStaffContent().let {
                                StaffFavouriteModel().also { model ->
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
                    }

                    SearchTypes.STUDIO -> {
                        data?.studios()?.nodes()?.map { map ->
                            map.fragments().studioContent().let {
                                StudioFavouriteModel().also { model ->
                                    model.studioId = it.fragments().studioInfo().id()
                                    model.studioName = it.fragments().studioInfo().name()
                                    model.studioMedia =
                                        it.media()?.nodes()
                                            ?.filter {
                                                if (field.canShowAdult) true else it.fragments()
                                                    .commonMediaContent().isAdult == false
                                            }
                                            ?.map {
                                                it.fragments().commonMediaContent().getCommonMedia(
                                                    MediaFavouriteModel()
                                                )
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
                    model.id = it.id()
                    model.isFollowing = it.isFollowing ?:false
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
        callback: ((Resource<List<UserModel>>) -> Unit)
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation())
            .map { response ->
                val data = response.data()
                if (data is UserFollowersQuery.Data)
                    (data as? UserFollowersQuery.Data)?.Page()?.followers()?.map {
                        UserModel().also { model ->
                            model.id = it.id()
                            model.name = it.name()
                            model.avatar = it.avatar()?.let {
                                AvatarModel().also { img -> img.medium = it.medium() }
                            }
                            model.isFollower = it.isFollower ?: false
                            model.isFollowing = it.isFollowing ?: false
                        }
                    }
                else
                    (data as? UserFollowingQuery.Data)?.Page()?.following()?.map {
                        UserModel().also { model ->
                            model.id = it.id()
                            model.name = it.name()
                            model.avatar = it.avatar()?.let {
                                AvatarModel().also { img -> img.medium = it.medium() }
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