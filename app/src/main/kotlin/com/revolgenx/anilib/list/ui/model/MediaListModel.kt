package com.revolgenx.anilib.list.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringArrayResource
import com.revolgenx.anilib.MediaListEntryQuery
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.tuples.MutablePair
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.model.FuzzyDateModel
import com.revolgenx.anilib.common.ui.model.toModel
import com.revolgenx.anilib.entry.ui.model.AdvancedScoreModel
import com.revolgenx.anilib.fragment.MediaListEntry
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.isAnime
import com.revolgenx.anilib.media.ui.model.isManga
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.user.ui.model.UserModel

data class MediaListModel(
    val id: Int = -1,
    val userId: Int = -1,
    val mediaId: Int = -1,
    val status: MediaListStatus? = null,
    val score: Double? = null,
    val progress: Int? = null,
    val progressVolumes: Int? = null,
    val repeat: Int? = null,
    val priority: Int? = null,
    val private: Boolean? = false,
    val notes: String? = null,
    val hiddenFromStatusLists: Boolean = false,
    val startedAt: FuzzyDateModel? = null,
    val completedAt: FuzzyDateModel? = null,
    val updatedAt: Int? = null,
    val createdAt: Int? = null,

    val customLists: List<MutablePair<String, Boolean>>? = null,
    val advancedScores: List<AdvancedScoreModel>? = null,

    var media: MediaModel? = null,
    val user: UserModel? = null,
) : BaseModel

fun MediaListEntry.toModel() = MediaListModel(
    id = id,
    mediaId = mediaId,
    status = status,
    score = score,
    progress = progress,
    progressVolumes = progressVolumes,
    repeat = repeat,
    priority = priority,
    private = private == true,
    hiddenFromStatusLists = hiddenFromStatusLists == true,
    customLists = (customLists as? Map<String, Boolean>)?.map { MutablePair(it.key, it.value) },
    notes = notes,
    updatedAt = updatedAt,
    createdAt = createdAt,
    startedAt = startedAt?.fuzzyDate?.toModel(),
    completedAt = completedAt?.fuzzyDate?.toModel()
)

fun MediaListEntryQuery.MediaListEntry.toModel(): MediaListModel {
    return mediaListEntry.toModel().copy(
        advancedScores = (advancedScores as? Map<String, *>)?.map {
            AdvancedScoreModel(
                it.key,
                mutableStateOf(it.value?.let { if (it is Int) it.toDouble() else it as Double }
                    ?: 0.0)
            )
        }
    )
}


fun MediaListStatus.toColorRes() = when (this) {
    MediaListStatus.CURRENT -> R.color.current
    MediaListStatus.PLANNING -> R.color.planning
    MediaListStatus.COMPLETED -> R.color.completed
    MediaListStatus.DROPPED -> R.color.dropped
    MediaListStatus.PAUSED -> R.color.paused
    MediaListStatus.REPEATING -> R.color.repeating
    MediaListStatus.UNKNOWN__ -> R.color.dropped
}

fun MediaListStatus?.toStringRes(mediaType: MediaType): Int {
    val isManga = mediaType.isManga
    return when (this) {
        MediaListStatus.CURRENT -> if (isManga) R.string.watching else R.string.reading
        MediaListStatus.PLANNING -> if (isManga) R.string.plan_to_read else R.string.plan_to_watch
        MediaListStatus.COMPLETED -> R.string.completed
        MediaListStatus.DROPPED -> R.string.dropped
        MediaListStatus.PAUSED -> R.string.paused
        MediaListStatus.REPEATING -> if (isManga) R.string.rereading else R.string.rewatching
        else -> R.string.unknown
    }
}

fun MediaListStatus.toAlMediaListStatus() = when (this) {
    MediaListStatus.CURRENT -> 0
    MediaListStatus.PLANNING -> 1
    MediaListStatus.COMPLETED -> 2
    MediaListStatus.DROPPED -> 5
    MediaListStatus.PAUSED -> 4
    MediaListStatus.REPEATING -> 3
    MediaListStatus.UNKNOWN__ -> -1
}

fun getStatusFromAlMediaListStatus(status: Int) = when (status) {
    0 -> MediaListStatus.CURRENT
    1 -> MediaListStatus.PLANNING
    2 -> MediaListStatus.COMPLETED
    3 -> MediaListStatus.REPEATING
    4 -> MediaListStatus.PAUSED
    5 -> MediaListStatus.DROPPED
    else -> MediaListStatus.UNKNOWN__
}

@Composable
fun getAlMediaListStatusForType(type: MediaType?) =
    stringArrayResource(id = if (type.isAnime) R.array.anime_media_list_status else R.array.manga_media_list_status)
