package com.revolgenx.anilib.list.ui.model

import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.MediaListCollectionQuery
import com.revolgenx.anilib.list.data.field.MediaListCollectionField
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.user.ui.model.MediaListOptionModel
import com.revolgenx.anilib.user.ui.model.UserModel
import com.revolgenx.anilib.user.ui.model.getRowOrder
import com.revolgenx.anilib.user.ui.model.toModel

data class MediaListCollectionModel(
    val lists: MutableList<MediaListGroupModel>? = null,
    val user: UserModel? = null
)

fun MediaListCollectionQuery.Data.toModel(field: MediaListCollectionField): MediaListCollectionModel {

    val userModel = mediaListCollection?.user?.run {
        UserModel(
            id = id,
            mediaListOptions = mediaListOptions?.let { option ->
                MediaListOptionModel(
                    scoreFormat = option.scoreFormat,
                    rowOrder = getRowOrder(option.rowOrder),
                    animeList = option.animeList?.mediaListCollectionTypeOptions?.toModel(),
                    mangaList = option.mangaList?.mediaListCollectionTypeOptions?.toModel()
                )
            }
        )
    }

    val sectionOrder =
        userModel?.mediaListOptions?.let { it.animeList ?: it.mangaList }?.sectionOrder

    val listModel = mediaListCollection?.lists?.let {
        val allEntries = mutableMapOf<Int, MediaListModel>()
        it.mapNotNull { g ->
            g?.let { group ->
                val isCustomList = group.isCustomList == true
                val isCompletedList = group.isCompletedList == true
                val groupName = group.name
                MediaListGroupModel(
                    name = groupName,
                    isCustomList = isCustomList,
                    isCompletedList = isCompletedList,
                    order = sectionOrder?.indexOf(groupName)?.plus(1) ?: 1,
                    entries = group.entries?.mapNotNull { entry ->
                        entry
                            ?.takeIf { if (field.canShowAdult) true else it.media?.media?.isAdult == false }
                            ?.mediaListEntry
                            ?.toModel()
                            ?.let { entryModel ->
                                entryModel.copy(
                                    userId = userModel?.id ?: -1,
                                    user = userModel,
                                    progressState = mutableStateOf(entryModel.progress),
                                    media = entry.media?.media?.toModel()?.copy(
                                        synonyms = entry.media.synonyms?.filterNotNull(),
                                        currentEpisode = entry.media.nextAiringEpisode?.episode?.minus(1)
                                    )
                                ).also {
                                    if ((isCustomList.not() && entryModel.hiddenFromStatusLists.not()) || (isCustomList && entryModel.hiddenFromStatusLists)) {
                                        allEntries[entryModel.id] = it
                                    }
                                }
                            }
                    }?.toMutableList(),

                    )
            }
        }
            .toMutableList().also {
                it.add(
                    MediaListGroupModel(
                        name = "All",
                        entries = allEntries.values.toMutableList(),
                        order = 0
                    )
                )
                it.sortBy { it.order }
            }
    }

    return MediaListCollectionModel(
        user = userModel,
        lists = listModel
    )
}