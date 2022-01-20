package com.revolgenx.anilib.character.service

import com.revolgenx.anilib.character.data.model.VoiceActorImageModel
import com.revolgenx.anilib.character.data.model.VoiceActorModel
import com.revolgenx.anilib.character.data.field.CharacterField
import com.revolgenx.anilib.character.data.field.CharacterMediaField
import com.revolgenx.anilib.character.data.field.CharacterVoiceActorField
import com.revolgenx.anilib.character.data.model.CharacterImageModel
import com.revolgenx.anilib.character.data.model.CharacterMediaModel
import com.revolgenx.anilib.character.data.model.CharacterModel
import com.revolgenx.anilib.character.data.model.CharacterNameModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.getCommonMedia
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
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
                it.data()?.Character()?.let {
                    CharacterModel().also { model ->
                        model.id = it.id()
                        model.isFavourite = it.isFavourite
                        model.description = it.description()
                        model.siteUrl = it.siteUrl()
                        model.favourites = it.favourites()
                        model.name = it.name()?.let {
                            CharacterNameModel().also { name ->
                                name.full = it.full()
                                name.native = it.native_()
                                name.alternative = it.alternative()
                            }
                        }
                        model.image = it.image()?.let { image ->
                            CharacterImageModel().also { imgModel ->
                                imgModel.large = image.large()
                                imgModel.medium = image.medium()
                            }
                        }
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
        resourceCallback: ((Resource<List<CharacterMediaModel>>) -> Unit)
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.Character()?.media()?.nodes()?.map {
                    it.fragments().commonMediaContent().getCommonMedia(CharacterMediaModel())
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
                it.data()?.Character()?.media()?.edges()?.forEach {
                    it.voiceActors()?.forEach {
                        hashMap[it.id()] = VoiceActorModel().also { model ->
                            model.actorId = it.id()
                            model.name = it.name()?.full()
                            model.language = it.language()?.ordinal
                            model.languageV2 = it.languageV2()
                            model.voiceActorImageModel = it.image()?.let {
                                VoiceActorImageModel().apply {
                                    large = it.large()
                                    medium = it.medium()
                                }
                            }
                        }
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(hashMap.values.toList()))
            }, {
                Timber.w(it)
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable?.add(disposable)
    }




}