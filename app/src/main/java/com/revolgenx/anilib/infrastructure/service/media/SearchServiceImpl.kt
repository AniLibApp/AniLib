package com.revolgenx.anilib.infrastructure.service.media

import com.apollographql.apollo.api.Query
import com.revolgenx.anilib.*
import com.revolgenx.anilib.data.field.search.SearchField
import com.revolgenx.anilib.data.model.*
import com.revolgenx.anilib.data.model.character.CharacterImageModel
import com.revolgenx.anilib.data.model.character.CharacterNameModel
import com.revolgenx.anilib.data.model.search.*
import com.revolgenx.anilib.data.model.staff.StaffImageModel
import com.revolgenx.anilib.data.model.staff.StaffNameModel
import com.revolgenx.anilib.data.model.user.AvatarModel
import com.revolgenx.anilib.data.model.user.UserModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.getCommonMedia
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
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
                            it.fragments().commonMediaContent().getCommonMedia(MediaSearchModel())
                        }
                    }
                    is CharacterSearchQuery.Data -> {
                        data.Page()?.characters()?.map { map ->
                            map.fragments().narrowCharacterContent().let {
                                CharacterSearchModel().also { model ->
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
                                StaffSearchModel().also { model ->
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
                                StudioSearchModel().also { model ->
                                    model.studioId = it.fragments().studioInfo().id()
                                    model.studioName = it.fragments().studioInfo().name()
                                    model.studioMedia =
                                        it.media()?.nodes()
                                            ?.filter {
                                                if (field.canShowAdult) true else it.fragments()
                                                    .commonMediaContent().isAdult == false
                                            }
                                            ?.map {
                                                it.fragments().commonMediaContent().getCommonMedia(MediaSearchModel())
                                            }
                                }
                            }
                        }?.filter { it.studioMedia.isNullOrEmpty().not() }
                    }
                    is UserSearchQuery.Data -> {
                        data.Page()?.users()?.map {
                            UserModel().apply {
                                id = it.id()
                                name = it.name()
                                avatar = it.avatar()?.let {
                                    AvatarModel().also { img->
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