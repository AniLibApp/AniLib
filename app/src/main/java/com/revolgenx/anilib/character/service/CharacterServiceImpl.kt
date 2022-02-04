package com.revolgenx.anilib.character.service

import com.revolgenx.anilib.character.data.field.CharacterField
import com.revolgenx.anilib.character.data.field.CharacterMediaField
import com.revolgenx.anilib.character.data.field.CharacterVoiceActorField
import com.revolgenx.anilib.character.data.model.*
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.media.data.model.toModel
import com.revolgenx.anilib.staff.data.model.toModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class CharacterServiceImpl(
    private val graphRepository: BaseGraphRepository
) : CharacterService {

    override fun getCharacterInfo(
        field: CharacterField,
        compositeDisposable: CompositeDisposable?,
        callback: (Resource<CharacterModel>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data?.character?.let {
                    CharacterModel().also { model ->
                        model.id = it.id
                        model.isFavourite = it.isFavourite
                        model.description = it.description
                        model.siteUrl = it.siteUrl
                        model.favourites = it.favourites
                        model.name = it.name?.let {
                            CharacterNameModel(
                                it.full,
                                native = it.native,
                                alternative = it.alternative?.filterNotNull()
                            )
                        }
                        model.image = it.image?.characterImage?.toModel()
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable?.add(disposable)
    }

    override fun getCharacterMediaInfo(
        field: CharacterMediaField,
        compositeDisposable: CompositeDisposable?,
        resourceCallback: ((Resource<List<MediaModel>>) -> Unit)
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data?.character?.media?.nodes?.filterNotNull()?.map {
                    it.onMedia.mediaContent.toModel()
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it))
            }, {
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable?.add(disposable)
    }

    override fun getCharacterActor(
        field: CharacterVoiceActorField,
        compositeDisposable: CompositeDisposable?,
        resourceCallback: (Resource<List<VoiceActorModel>>) -> Unit
    ) {
        val hashMap = mutableMapOf<Int, VoiceActorModel>()
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data?.character?.media?.edges?.filterNotNull()?.forEach {
                    it.voiceActors?.filterNotNull()?.forEach {
                        hashMap[it.id] = VoiceActorModel().also { model ->
                            model.actorId = it.id
                            model.name = it.name?.full
                            model.languageV2 = it.languageV2
                            model.voiceActorImageModel = it.image?.staffImage?.toModel()
                        }
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(hashMap.values.toList()))
            }, {
                Timber.e(it)
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable?.add(disposable)
    }


}