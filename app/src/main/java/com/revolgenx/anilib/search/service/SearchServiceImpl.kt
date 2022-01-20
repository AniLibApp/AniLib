package com.revolgenx.anilib.search.service

import com.apollographql.apollo.api.Query
import com.revolgenx.anilib.*
import com.revolgenx.anilib.character.data.model.CharacterImageModel
import com.revolgenx.anilib.character.data.model.CharacterModel
import com.revolgenx.anilib.character.data.model.CharacterNameModel
import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.toModel
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.media.data.model.MediaConnectionModel
import com.revolgenx.anilib.search.data.field.SearchField
import com.revolgenx.anilib.staff.data.model.StaffImageModel
import com.revolgenx.anilib.staff.data.model.StaffModel
import com.revolgenx.anilib.staff.data.model.StaffNameModel
import com.revolgenx.anilib.studio.data.model.StudioModel
import com.revolgenx.anilib.user.data.model.UserAvatarModel
import com.revolgenx.anilib.user.data.model.UserModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class SearchServiceImpl(private val baseGraphRepository: BaseGraphRepository) :
    SearchService {

    override fun search(
        field: SearchField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<BaseModel>>) -> Unit
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation() as Query<*, *, *>)
            .map {
                when (val data = it.data()) {
                    is MediaSearchQuery.Data -> {
                        data.Page()?.media()?.map {
                            it.fragments().commonMediaContent().toModel()
                        }
                    }
                    is CharacterSearchQuery.Data -> {
                        data.Page()?.characters()?.map { map ->
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
                    }

                    is StaffSearchQuery.Data -> {
                        data.Page()?.staff()?.map { map ->
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
                    }

                    is StudioSearchQuery.Data -> {
                        data.Page()?.studios()?.map { map ->
                            map.fragments().studioContent().let {
                                StudioModel().also { model ->
                                    model.id = it.fragments().studioInfo().id()
                                    model.studioName = it.fragments().studioInfo().name()
                                    model.media = it.media()?.let {
                                        MediaConnectionModel().also { connectionModel ->
                                            connectionModel.nodes = it.nodes()?.filter {
                                                if (field.canShowAdult) true else it.fragments()
                                                    .commonMediaContent().isAdult == false
                                            }
                                                ?.map {
                                                    it.fragments().commonMediaContent().toModel()
                                                }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    is UserSearchQuery.Data -> {
                        data.Page()?.users()?.map {
                            UserModel().apply {
                                id = it.id()
                                name = it.name()
                                avatar = it.avatar()?.let {
                                    UserAvatarModel().also { img ->
                                        img.large = it.large()
                                        img.medium = it.medium()
                                    }
                                }
                            }
                        }
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
}