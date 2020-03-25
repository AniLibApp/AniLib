package com.revolgenx.anilib.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.field.StaffMediaCharacterField
import com.revolgenx.anilib.field.StaffField
import com.revolgenx.anilib.field.StaffMediaRoleField
import com.revolgenx.anilib.model.*
import com.revolgenx.anilib.model.character.CharacterNameModel
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.util.ERROR
import com.revolgenx.anilib.repository.util.Resource
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
        compositeDisposable: CompositeDisposable
    ): LiveData<Resource<StaffModel>> {

        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.Staff()?.let {
                    StaffModel().also { model ->
                        model.staffId = it.id()
                        model.staffName = it.name()?.let {
                            StaffNameModel().also { nModel ->
                                nModel.full = it.full()
                                nModel.native = it.native_()
                                nModel.alternative = it.alternative()
                            }
                        }
                        model.staffImage = it.image()?.let {
                            StaffImageModel().also { i ->
                                i.medium = it.medium()
                                i.large = it.large()
                            }
                        }
                        model.language = it.language()?.ordinal
                        model.favourites = it.favourites()
                        model.isFavourite = it.isFavourite
                        model.siteUrl = it.siteUrl()
                        model.description = it.description()
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                staffInfoLiveData.value = Resource.success(it)
            }, {
                Timber.w(it)
                staffInfoLiveData.value = Resource.error(it.message ?: ERROR, null, it)
            })

        compositeDisposable.add(disposable)
        return staffInfoLiveData
    }

    override fun getStaffMediaCharacter(
        field: StaffMediaCharacterField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<List<StaffMediaCharacterModel>>) -> Unit
    ) {
        val characList = mutableListOf<StaffMediaCharacterModel>()
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.Staff()?.characters()?.nodes()?.forEach { charac ->
                    charac.media()?.edges()?.forEach cont@{ mid ->
                        mid.node()?.let { media ->
                            if (media.isAdult == true) return@cont
                            StaffMediaCharacterModel().also { model ->
                                model.mediaId = media.id()
                                model.title = media.title()?.let {
                                    TitleModel().also { titleModel ->
                                        titleModel.english = it.english()
                                        titleModel.native = it.native_()
                                        titleModel.romaji = it.romaji()
                                        titleModel.userPreferred = it.userPreferred()
                                    }
                                }
                                model.coverImage = media.coverImage()?.let {
                                    CoverImageModel().also { img ->
                                        img.medium = it.medium()
                                        img.large = it.large()
                                        img.extraLarge = it.extraLarge()
                                    }
                                }
                                model.bannerImage =
                                    media.bannerImage() ?: model.coverImage?.largeImage
                                model.status = media.status()?.ordinal
                                model.format = media.format()?.ordinal
                                model.type = media.type()?.ordinal
                                model.seasonYear = media.seasonYear()
                                model.averageScore = media.averageScore()
                                model.characterId = charac.id()
                                model.characterName = charac.name()?.let {
                                    CharacterNameModel().also { c ->
                                        c.full = it.full()
                                    }
                                }
                                model.mediaRole = mid.characterRole()?.ordinal
                                model.characterImageModel = charac.image()?.let {
                                    CharacterImageModel().also { img ->
                                        img.large = it.large()
                                        img.medium = it.medium()
                                    }
                                }

                                characList.add(model)
                            }
                        }
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(characList))
            }, {
                Timber.w(it)
                resourceCallback.invoke(Resource.error(it.message ?: "ERROR", null, it))
            })

        compositeDisposable.add(disposable)

    }

    override fun getStaffMediaRole(
        field: StaffMediaRoleField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<List<StaffMediaRoleModel>>) -> Unit
    ) {

        val list = mutableListOf<StaffMediaRoleModel>()
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.Staff()?.staffMedia()?.edges()?.map map2@{ edge ->
                    if (edge.node()?.isAdult == true) return@map2
                    edge.node()?.let { media ->
                        StaffMediaRoleModel().also { model ->
                            model.mediaId = media.id()
                            model.title = media.title()?.let {
                                TitleModel().also { ti ->
                                    ti.romaji = it.romaji()
                                    ti.english = it.english()
                                    ti.native = it.native_()
                                    ti.userPreferred = it.userPreferred()
                                }
                            }
                            model.coverImage = media.coverImage()?.let {
                                CoverImageModel().also { img ->
                                    img.extraLarge = it.extraLarge()
                                    img.large = it.large()
                                    img.medium = it.medium()
                                }
                            }
                            model.bannerImage = media.bannerImage() ?: model.coverImage?.largeImage
                            model.averageScore = media.averageScore()
                            model.seasonYear = media.seasonYear()
                            model.format = media.format()?.ordinal
                            model.type = media.type()?.ordinal
                            model.staffRole = edge.staffRole()
                            list.add(model)
                        }
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(list))
            }, {
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable.add(disposable)
    }
}