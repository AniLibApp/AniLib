package com.revolgenx.anilib.list.service

import com.revolgenx.anilib.app.setting.data.model.MediaListOptionModel
import com.revolgenx.anilib.app.setting.data.model.MediaListOptionTypeModel
import com.revolgenx.anilib.app.setting.data.model.getRowOrder
import com.revolgenx.anilib.common.repository.network.BaseGraphRepository
import com.revolgenx.anilib.common.repository.util.ERROR
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.list.constant.ListConstant
import com.revolgenx.anilib.list.data.field.MediaListCollectionField
import com.revolgenx.anilib.list.data.model.MediaListCollectionModel
import com.revolgenx.anilib.list.data.model.MediaListGroupModel
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.list.data.model.toModel
import com.revolgenx.anilib.media.data.model.toModel
import com.revolgenx.anilib.user.data.model.UserModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MediaListCollectionService(private val graphRepository: BaseGraphRepository) {

    fun getMediaListCollection(
        field: MediaListCollectionField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<MediaListCollectionModel>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data?.mediaListCollection?.let { collection ->
                    MediaListCollectionModel().also { collectionModel ->
                        collectionModel.user = collection.user?.let { user ->
                            UserModel().also { userModel ->
                                userModel.id = user.id
                                userModel.mediaListOptions =
                                    user.mediaListOptions?.let { option ->
                                        MediaListOptionModel().also { optionModel ->
                                            optionModel.scoreFormat = option.scoreFormat?.ordinal
                                            optionModel.rowOrder = getRowOrder(option.rowOrder)
                                            optionModel.rowOrder = getRowOrder(option.rowOrder)
                                            optionModel.animeList =
                                                option.animeList?.let { type ->
                                                    MediaListOptionTypeModel().also { typeModel ->
                                                        typeModel.customLists =
                                                            type.customLists?.filterNotNull()
                                                        typeModel.sectionOrder =
                                                            type.sectionOrder?.filterNotNull()
                                                                ?.toMutableList()
                                                                ?.also { sectionOrder ->
                                                                    typeModel.customLists?.forEach { customList ->
                                                                        if (sectionOrder.contains(
                                                                                customList
                                                                            ).not()
                                                                        ) {
                                                                            sectionOrder.add(
                                                                                customList
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                    }
                                                }
                                            optionModel.mangaList =
                                                option.mangaList?.let { type ->
                                                    MediaListOptionTypeModel().also { typeModel ->
                                                        typeModel.customLists =
                                                            type.customLists?.filterNotNull()
                                                        typeModel.sectionOrder =
                                                            type.sectionOrder?.filterNotNull()
                                                                ?.toMutableList()
                                                                ?.also { sectionOrder ->
                                                                    typeModel.customLists?.forEach { customList ->
                                                                        if (sectionOrder.contains(
                                                                                customList
                                                                            ).not()
                                                                        ) {
                                                                            sectionOrder.add(
                                                                                customList
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                    }
                                                }
                                        }
                                    }
                            }
                        }

                        val collectionList = collection.lists

                        if (collectionList != null) {
                            val allEntries = mutableMapOf<Int, MediaListModel>()
                            collectionModel.lists = collectionList.mapNotNull { g ->
                                g?.let { group ->
                                    MediaListGroupModel().also { groupModel ->
                                        groupModel.name = group.name
                                        groupModel.isCustomList = group.isCustomList == true
                                        groupModel.isCompletedList = group.isCompletedList == true
                                        groupModel.entries = group.entries?.mapNotNull {
                                            it?.takeIf { if (field.canShowAdult) true else it.media?.mediaContent?.isAdult == false }?.mediaListEntry?.toModel()
                                                ?.also { entry ->
                                                    entry.userId = collectionModel.user?.id ?: -1
                                                    entry.user = collectionModel.user
                                                    entry.media = it.media?.mediaContent?.toModel()
                                                    if ((groupModel.isCustomList.not() && entry.hiddenFromStatusLists.not()) || (groupModel.isCustomList && entry.hiddenFromStatusLists)) {
                                                        allEntries[entry.id] = entry
                                                    }
                                                }
                                        }?.toMutableList()
                                    }
                                }
                            }.toMutableList().also {
                                val allListGroupModel = MediaListGroupModel()
                                allListGroupModel.name = "All"
                                allListGroupModel.entries = allEntries.values.toMutableList()
                                it.add(allListGroupModel)
                            }
                        }
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it))
            }, {
                Timber.e(it)
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable.add(disposable)
    }
}