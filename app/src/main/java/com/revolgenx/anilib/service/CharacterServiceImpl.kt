package com.revolgenx.anilib.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.field.CharacterField
import com.revolgenx.anilib.field.CharacterMediaField
import com.revolgenx.anilib.field.CharacterVoiceActorField
import com.revolgenx.anilib.model.*
import com.revolgenx.anilib.model.character.CharacterMediaModel
import com.revolgenx.anilib.model.character.CharacterModel
import com.revolgenx.anilib.model.character.CharacterNameModel
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.util.ERROR
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class CharacterServiceImpl(
    private val graphRepository: BaseGraphRepository
) : CharacterService {
    override val characterInfoLiveData by lazy {
        MutableLiveData<Resource<CharacterModel>>()
    }

    override fun getCharacterInfo(
        field: CharacterField,
        compositeDisposable: CompositeDisposable?
    ): LiveData<Resource<CharacterModel>> {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.Character()?.let {
                    CharacterModel().also { model ->
                        model.characterId = it.id()
                        model.isFavourite = it.isFavourite
                        model.descrition = it.description()
                        model.siteUrl = it.siteUrl()
                        model.favourites = it.favourites()
                        model.name = it.name()?.let {
                            CharacterNameModel().also { name ->
                                name.full = it.full()
                                name.native = it.native_()
                                name.alternative = it.alternative()
                            }
                        }
                        model.characterImageModel = it.image()?.let { image ->
                            CharacterImageModel().also { imgModel ->
                                imgModel.large = image.large()
                                imgModel.medium = image.medium()
                            }
                        }
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                characterInfoLiveData.value = Resource.success(it)
            }, {
                characterInfoLiveData.value = Resource.error(it.message ?: ERROR, null, it)
            })

        compositeDisposable?.add(disposable)
        return characterInfoLiveData
    }

    override fun getCharacterMediaInfo(
        field: CharacterMediaField,
        compositeDisposable: CompositeDisposable?,
        resourceCallback: ((Resource<List<CharacterMediaModel>>) -> Unit)
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.Character()?.media()?.nodes()?.map {
                    CharacterMediaModel().also { model ->
                        model.mediaId = it.id()
                        model.title = it.title()?.let {
                            TitleModel().also { title ->
                                title.romaji = it.romaji()
                                title.english = it.english()
                                title.native = it.native_()
                                title.userPreferred = it.userPreferred()
                            }
                        }
                        model.season = it.season()?.ordinal
                        model.seasonYear = it.seasonYear()
                        model.status = it.status()?.ordinal
                        model.format = it.format()?.ordinal
                        model.type = it.type()?.ordinal
                        model.averageScore = it.averageScore()
                        model.coverImage = it.coverImage()?.let {
                            CoverImageModel().also { img ->
                                img.large = it.large()
                                img.extraLarge = it.extraLarge()
                            }
                        }
                        model.bannerImage = it.bannerImage() ?: model.coverImage?.largeImage
                    }
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