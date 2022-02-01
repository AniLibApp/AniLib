package com.revolgenx.anilib.staff.service

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.character.data.model.*
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.media.data.model.MediaConnectionModel
import com.revolgenx.anilib.media.data.model.MediaEdgeModel
import com.revolgenx.anilib.media.data.model.toModel
import com.revolgenx.anilib.staff.data.field.StaffField
import com.revolgenx.anilib.staff.data.field.StaffMediaCharacterField
import com.revolgenx.anilib.staff.data.field.StaffMediaRoleField
import com.revolgenx.anilib.staff.data.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class StaffServiceImpl(
    private val graphRepository: BaseGraphRepository
) : StaffService {
    override val staffInfoLiveData: MutableLiveData<Resource<StaffModel>> by lazy {
        MutableLiveData<Resource<StaffModel>>()
    }

    override fun getStaffInfo(
        field: StaffField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<StaffModel>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data?.staff?.let {
                    StaffModel().also { model ->
                        model.id = it.id
                        model.name = it.name?.let {
                            StaffNameModel(it.full, it.native, it.alternative?.filterNotNull())
                        }
                        model.image = it.image?.staffImage?.toModel()
                        model.languageV2 = it.languageV2
                        model.favourites = it.favourites
                        model.isFavourite = it.isFavourite
                        model.siteUrl = it.siteUrl
                        model.description = it.description
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                Timber.e(it)
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }

    override fun getStaffMediaCharacter(
        field: StaffMediaCharacterField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<CharacterConnectionModel>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data?.staff?.characters?.let {
                    CharacterConnectionModel().also { characterConnectionModel ->
                        characterConnectionModel.nodes =
                            it.nodes?.filterNotNull()?.map { character ->
                                CharacterModel().also { characterModel ->
                                    characterModel.id = character.id
                                    characterModel.name = CharacterNameModel(character.name?.full)
                                    characterModel.image =
                                        character.image?.characterImage?.toModel()

                                    characterModel.media = character.media?.let { media ->
                                        MediaConnectionModel().also { mediaConnectionModel ->
                                            mediaConnectionModel.edges =
                                                media.edges?.filterNotNull()?.map { edge ->
                                                    MediaEdgeModel().also { edgeModel ->
                                                        edgeModel.characterRole =
                                                            edge.characterRole?.ordinal
                                                        edgeModel.node =
                                                            edge.node?.onMedia?.mediaContent?.toModel()
                                                    }
                                                }
                                        }
                                    }

                                }
                            }
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it))
            },
                {
                    Timber.w(it)
                    resourceCallback.invoke(Resource.error(it.message ?: "ERROR", null, it))
                })
        compositeDisposable.add(disposable)
    }

    override fun getStaffMediaRole(
        field: StaffMediaRoleField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<MediaConnectionModel>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {

                it.data?.staff?.staffMedia?.let { staffMedia ->
                    MediaConnectionModel().also { mediaConnectionModel ->
                        mediaConnectionModel.edges =
                            staffMedia.edges?.filterNotNull()?.map { edge ->
                                MediaEdgeModel().also { staffEdgeModel ->
                                    staffEdgeModel.staffRole = edge.staffRole
                                    staffEdgeModel.node =
                                        edge.node?.onMedia?.mediaContent?.toModel()
                                }
                            }
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it))
            }, {
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable.add(disposable)
    }
}