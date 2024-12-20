package com.revolgenx.anilib.entry.ui.model

import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.MediaListEntryQuery
import com.revolgenx.anilib.SaveMediaListEntryMutation
import com.revolgenx.anilib.list.ui.model.MediaListModel
import com.revolgenx.anilib.list.ui.model.toModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.user.ui.model.MediaListOptionModel
import com.revolgenx.anilib.user.ui.model.MediaListOptionTypeModel
import com.revolgenx.anilib.user.ui.model.UserModel

data class UserMediaModel(
    val user: UserModel?,
    val media: MediaModel?
)

fun SaveMediaListEntryMutation.Data.toModel(): MediaListModel? {
    return this.saveMediaListEntry?.let { entry ->
        entry.mediaListEntry.toModel().also {
            it.media = entry.media?.media?.toModel()
        }
    }
}

fun MediaListEntryQuery.Data.toModel(): UserMediaModel {
    return UserMediaModel(
        user = user?.let {
            UserModel(
                id = user.id,
                mediaListOptions = user.mediaListOptions?.let { option ->
                    MediaListOptionModel(scoreFormat = option.scoreFormat,
                        animeList = option.animeList?.let { optionType ->
                            MediaListOptionTypeModel(
                                advancedScoring = optionType.advancedScoring?.filterNotNull(),
                                advancedScoringEnabled = optionType.advancedScoringEnabled == true,
                                customLists = optionType.customLists?.filterNotNull()
                            )
                        },
                        mangaList = option.mangaList?.let { optionType ->
                            MediaListOptionTypeModel(
                                advancedScoring = optionType.advancedScoring?.filterNotNull(),
                                advancedScoringEnabled = optionType.advancedScoringEnabled == true,
                                customLists = optionType.customLists?.filterNotNull()
                            )
                        })
                })
        },
        media = media?.let {
            MediaModel(
                id = media.id,
                title = media.title?.mediaTitle?.toModel(),
                type = media.type,
                status = media.status,
                episodes = media.episodes,
                chapters = media.chapters,
                volumes = media.volumes,
                isFavourite = mutableStateOf(media.isFavourite),
                mediaListEntry = media.mediaListEntry?.toModel()
            )
        })
}