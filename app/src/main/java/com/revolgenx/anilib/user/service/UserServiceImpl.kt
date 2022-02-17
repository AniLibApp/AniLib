package com.revolgenx.anilib.user.service

import com.revolgenx.anilib.UserFollowersQuery
import com.revolgenx.anilib.UserFollowingQuery
import com.revolgenx.anilib.character.data.model.CharacterModel
import com.revolgenx.anilib.character.data.model.CharacterNameModel
import com.revolgenx.anilib.character.data.model.toModel
import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.friend.data.field.UserFriendField
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.media.data.model.MediaConnectionModel
import com.revolgenx.anilib.media.data.model.toModel
import com.revolgenx.anilib.staff.data.model.StaffModel
import com.revolgenx.anilib.staff.data.model.StaffNameModel
import com.revolgenx.anilib.staff.data.model.toModel
import com.revolgenx.anilib.studio.data.model.StudioModel
import com.revolgenx.anilib.user.data.field.*
import com.revolgenx.anilib.user.data.model.*
import com.revolgenx.anilib.user.data.model.stats.UserGenreStatisticModel
import com.revolgenx.anilib.user.data.model.stats.UserStatisticsModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserServiceImpl(private val baseGraphRepository: BaseGraphRepository) : UserService {
    override fun getUserOverView(
        field: UserOverViewField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<UserModel>) -> Unit
    ) {
        val disposable =
            baseGraphRepository.request(field.toQueryOrMutation()).map { response ->
                response.data?.let {
                    it.user?.let { user ->
                        UserModel().also { model ->
                            model.id = user.id
                            model.name = user.name
                            model.avatar = user.avatar?.userAvatar?.toModel()
                            model.bannerImage = user.bannerImage ?: model.avatar?.image
                            model.isBlocked = user.isBlocked ?: false
                            model.isFollower = user.isFollower ?: false
                            model.isFollowing = user.isFollowing ?: false
                            model.about = user.about ?: ""
                            model.siteUrl = user.siteUrl
                            model.statistics = user.statistics?.let { stats ->
                                UserStatisticTypesModel().also {
                                    it.anime = stats.anime?.let { anime ->
                                        UserStatisticsModel().also { sModel ->
                                            sModel.count = anime.count
                                            sModel.daysWatched =
                                                anime.minutesWatched.toDouble().div(60).div(24)
                                            sModel.episodesWatched = anime.episodesWatched
                                            sModel.meanScore = anime.meanScore
                                            sModel.genres = anime.genres?.mapNotNull { genre ->
                                                genre?.let { g ->
                                                    UserGenreStatisticModel().also { gModel ->
                                                        gModel.genre = g.genre
                                                        gModel.count = g.count
                                                    }
                                                }

                                            }
                                        }
                                    }

                                    it.manga = stats.manga?.let { manga ->
                                        UserStatisticsModel().also { sModel ->
                                            sModel.count = manga.count
                                            sModel.volumesRead =
                                                manga.volumesRead
                                            sModel.chaptersRead = manga.chaptersRead
                                            sModel.meanScore = manga.meanScore
                                            sModel.genres = manga.genres?.mapNotNull { genre ->
                                                genre?.let { g ->
                                                    UserGenreStatisticModel().also { gModel ->
                                                        gModel.genre = g.genre
                                                        gModel.count = g.count
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }?.also { userModel ->
                        if (field.includeFollow) {
                            userModel.following = it.followingPage?.pageInfo?.total ?: 0
                            userModel.followers = it.followerPage?.pageInfo?.total ?: 0
                        } else {
                            runBlocking {
                                val fResponse = suspendCoroutine<Resource<Pair<Int, Int>>> { cont ->
                                    getUserFollowingFollowerCount(UserFollowingFollowerCountField().also { countField ->
                                        countField.userId = userModel.id
                                    }, compositeDisposable) {
                                        cont.resume(it)
                                    }
                                }
                                userModel.following = fResponse.data?.first ?: 0
                                userModel.followers = fResponse.data?.second ?: 0
                            }
                        }
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callback.invoke(Resource.success(it))
                }, {
                    Timber.w(it)
                    callback.invoke(Resource.error(it.message, null, it))
                })

        compositeDisposable.add(disposable)
    }

    override fun getUserFollowingFollowerCount(
        field: UserFollowingFollowerCountField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<Pair<Int, Int>>) -> Unit
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation()).map {
            it.data?.let {
                (it.followingPage?.pageInfo?.total ?: 0) to
                        (it.followerPage?.pageInfo?.total ?: 0)
            }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                Timber.w(it)
                callback.invoke(Resource.error(it.message, null, it))
            })

        compositeDisposable.add(disposable)
    }

    override fun getFollowersUsers(
        field: UserFollowerField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<UserModel>>) -> Unit
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation())
            .map { response ->
                val data = response.data
                if (data is UserFollowersQuery.Data)
                    (data as? UserFollowersQuery.Data)?.page?.followers?.filterNotNull()?.map {
                        UserModel().also { model ->
                            model.id = it.id
                            model.name = it.name
                            model.avatar = it.avatar?.userAvatar?.toModel()
                        }
                    }
                else
                    (data as? UserFollowingQuery.Data)?.page?.following?.filterNotNull()?.map {
                        UserModel().also { model ->
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
                it.data?.user?.favourites?.let { fav ->
                    fav.anime?.nodes?.mapNotNull { node ->
                        node?.takeIf {
                            if (field.canShowAdult) true else it.onMedia.mediaContent.isAdult == false
                        }?.onMedia?.mediaContent?.toModel()

                    } ?: fav.manga?.nodes?.mapNotNull { node ->
                        node?.takeIf {
                            if (field.canShowAdult) true else it.onMedia.mediaContent.isAdult == false
                        }?.onMedia?.mediaContent?.toModel()

                    } ?: fav.characters?.nodes?.mapNotNull { node ->
                        node?.onCharacter?.narrowCharacterContent?.let {
                            CharacterModel().also { model ->
                                model.id = it.id
                                model.name = it.name?.let {
                                    CharacterNameModel(it.full)
                                }
                                model.image = it.image?.characterImage?.toModel()
                            }
                        }
                    } ?: fav.staff?.nodes?.mapNotNull { map ->
                        map?.onStaff?.narrowStaffContent?.let {
                            StaffModel().also { model ->
                                model.id = it.id
                                model.name = it.name?.let {
                                    StaffNameModel(it.full)
                                }
                                model.image = it.image?.staffImage?.toModel()
                            }
                        }
                    } ?: fav.studios?.nodes?.mapNotNull { map ->
                        map?.onStudio?.studioContent?.let {
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
                Timber.e(it)
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
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
                val data = response.data
                if (data is UserFollowersQuery.Data)
                    (data as? UserFollowersQuery.Data)?.page?.followers?.filterNotNull()?.map {
                        UserModel().also { model ->
                            model.id = it.id
                            model.name = it.name
                            model.avatar = it.avatar?.userAvatar?.toModel()
                            model.isFollower = it.isFollower ?: false
                            model.isFollowing = it.isFollowing ?: false
                        }
                    }
                else
                    (data as? UserFollowingQuery.Data)?.page?.following?.filterNotNull()?.map {
                        UserModel().also { model ->
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