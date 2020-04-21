package com.revolgenx.anilib.service

import com.apollographql.apollo.api.Query
import com.revolgenx.anilib.CharacterSearchQuery
import com.revolgenx.anilib.MediaSearchQuery
import com.revolgenx.anilib.StaffSearchQuery
import com.revolgenx.anilib.StudioSearchQuery
import com.revolgenx.anilib.field.browse.BrowseField
import com.revolgenx.anilib.model.*
import com.revolgenx.anilib.model.character.CharacterNameModel
import com.revolgenx.anilib.model.search.CharacterSearchModel
import com.revolgenx.anilib.model.search.MediaSearchModel
import com.revolgenx.anilib.model.search.StaffSearchModel
import com.revolgenx.anilib.model.search.StudioSearchModel
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.util.ERROR
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class BrowseServiceImpl(private val baseGraphRepository: BaseGraphRepository) :
    BrowseService {

    override fun search(
        field: BrowseField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<BaseModel>>) -> Unit
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation() as Query<*, *, *>)
            .map {
                when (val data = it.data()) {
                    is MediaSearchQuery.Data -> {
                        data.Page()?.media()?.map {
                            MediaSearchModel().also { model ->
                                model.mediaId = it.id()
                                model.title = it.title()?.let {
                                    TitleModel().also { ti ->
                                        ti.romaji = it.romaji()
                                        ti.english = it.english()
                                        ti.native = it.native_()
                                        ti.userPreferred = it.userPreferred()
                                    }
                                }
                                model.coverImage = it.coverImage()?.let {
                                    CoverImageModel().also { img ->
                                        img.extraLarge = it.extraLarge()
                                        img.medium = it.medium()
                                        img.large = it.large()
                                    }
                                }
                                model.bannerImage = it.bannerImage() ?: model.coverImage?.largeImage
                                model.type = it.type()?.ordinal
                                model.format = it.format()?.ordinal
                                model.status = it.status()?.ordinal
                                model.seasonYear = it.seasonYear()
                                model.season = it.season()?.ordinal
                                model.averageScore = it.averageScore()
                            }
                        }
                    }
                    is CharacterSearchQuery.Data -> {
                        data.Page()?.characters()?.map { map ->
                            map.fragments().narrowCharacterContent().let {
                                CharacterSearchModel().also { model ->
                                    model.characterId = it.id()
                                    model.name = it.name()?.let {
                                        CharacterNameModel().also { name ->
                                            name.full = it.full()
                                        }
                                    }
                                    model.characterImageModel = it.image()?.let {
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
                                    model.staffId = it.id()
                                    model.staffName = it.name()?.let {
                                        StaffNameModel().also { name ->
                                            name.full = it.full()
                                        }
                                    }
                                    model.staffImage = it.image()?.let {
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
                                    model.studioId = it.id()
                                    model.studioName = it.name()
                                    model.studioMedia =
                                        it.media()?.nodes()
                                            ?.filter { it.fragments().commonMediaContent().isAdult == false }
                                            ?.map {
                                                it.fragments().commonMediaContent().let {
                                                    MediaSearchModel().also { model ->
                                                        model.mediaId = it.id()
                                                        model.averageScore = it.averageScore()
                                                        model.title = it.title()?.fragments()?.mediaTitle()?.let {
                                                            TitleModel().also { ti ->
                                                                ti.userPreferred =
                                                                    it.userPreferred()
                                                                ti.romaji = it.romaji()
                                                                ti.english = it.english()
                                                                ti.native = it.native_()
                                                            }
                                                        }
                                                        model.coverImage = it.coverImage()?.fragments()?.mediaCoverImage()?.let {
                                                            CoverImageModel().also { img ->
                                                                img.large = it.large()
                                                                img.medium = it.medium()
                                                                img.extraLarge = it.extraLarge()
                                                            }
                                                        }
                                                        model.bannerImage =
                                                            it.bannerImage()
                                                                ?: model.coverImage?.largeImage
                                                        model.type = it.type()?.ordinal
                                                        model.format = it.format()?.ordinal
                                                        model.status = it.status()?.ordinal
                                                        model.seasonYear = it.seasonYear()
                                                    }
                                                }
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
}