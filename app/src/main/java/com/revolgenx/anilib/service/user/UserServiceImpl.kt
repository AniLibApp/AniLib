package com.revolgenx.anilib.service.user

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.UserFollowersQuery
import com.revolgenx.anilib.UserFollowingQuery
import com.revolgenx.anilib.field.user.UserField
import com.revolgenx.anilib.field.user.UserFollowerField
import com.revolgenx.anilib.model.UserAvatarImageModel
import com.revolgenx.anilib.model.user.UserFollowersModel
import com.revolgenx.anilib.model.user.UserFollowerCountModel
import com.revolgenx.anilib.model.user.UserProfileModel
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.util.ERROR
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class UserServiceImpl(private val repository: BaseGraphRepository) : UserService {
    override var userProfileLiveData: MutableLiveData<Resource<UserProfileModel>> =
        MutableLiveData()
    override val userFollowerCountLiveData: MutableLiveData<Resource<UserFollowerCountModel>> =
        MutableLiveData()

    override fun getUserProfile(userField: UserField, compositeDisposable: CompositeDisposable) {
        val disposable = repository.request(userField.toQueryOrMutation()).map { response ->
            response.data()?.User()?.let {
                UserProfileModel().also { model ->
                    model.baseId = it.id()
                    model.userId = it.id()
                    model.name = it.name()
                    model.avatar = it.avatar()?.let { ava ->
                        UserAvatarImageModel().also { img ->
                            img.large = ava.large()
                            img.medium = ava.medium()
                        }
                    }
                    model.bannerImage = it.bannerImage()
                    model.isBlocked = it.isBlocked
                    model.isFollower = it.isFollower
                    model.isFollowing = it.isFollowing
                    model.about = it.about()
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
                model?.genreOverView?.toList()?.sortedByDescending { (_, value) -> value }?.toMap()
                    ?.toMutableMap()?.let {
                        model.genreOverView = it
                    }
                userProfileLiveData.value = Resource.success(model)
                model?.let {
                    getTotalFollowing(userField, compositeDisposable)
                }
            }, {
                Timber.w(it)
                userProfileLiveData.value = Resource.error(it.message ?: ERROR, null, it)
            })

        compositeDisposable.add(disposable)
    }

    override fun getTotalFollower(
        userField: UserField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<Int>) -> Unit)?
    ) {
        val disposable = repository.request(userField.userTotalFollowerField.toQueryOrMutation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (userFollowerCountLiveData.value == null) {
                    userFollowerCountLiveData.value =
                        Resource.success(UserFollowerCountModel().also { mod ->
                            mod.followers = response.data()?.Page()?.pageInfo()?.total()
                        })
                } else {
                    userFollowerCountLiveData.value = userFollowerCountLiveData.value?.also { mod ->
                        mod.data?.followers = response.data()?.Page()?.pageInfo()?.total()
                    }
                }
            }, {
                Timber.w(it)
            })
        compositeDisposable.add(disposable)
    }

    override fun getTotalFollowing(
        userField: UserField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<Int>) -> Unit)?
    ) {
        val disposable = repository.request(userField.userTotalFollowingField.toQueryOrMutation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (userFollowerCountLiveData.value == null) {
                    userFollowerCountLiveData.value =
                        Resource.success(UserFollowerCountModel().also { mod ->
                            mod.following = response.data()?.Page()?.pageInfo()?.total()
                        })
                } else {
                    userFollowerCountLiveData.value = userFollowerCountLiveData.value?.also { mod ->
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
        val disposable = repository.request(userField.toQueryOrMutation())
            .map { response ->
                val data = response.data()
                if (data is UserFollowersQuery.Data)
                    (data as? UserFollowersQuery.Data)?.Page()?.followers()?.map {
                        UserFollowersModel().also { model ->
                            model.userId = it.id()
                            model.name = it.name()
                            model.avatar = it.avatar()?.let {
                                UserAvatarImageModel().also { img -> img.medium = it.medium() }
                            }
                        }
                    }
                else
                    (data as? UserFollowingQuery.Data)?.Page()?.following()?.map {
                        UserFollowersModel().also { model ->
                            model.userId = it.id()
                            model.name = it.name()
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


}