package com.revolgenx.anilib.widget.data.store

import com.revolgenx.anilib.airing.ui.model.AiringAtModel
import com.revolgenx.anilib.airing.ui.model.AiringScheduleModel
import com.revolgenx.anilib.airing.ui.model.TimeUntilAiringModel
import com.revolgenx.anilib.media.ui.model.MediaCoverImageModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import com.revolgenx.anilib.type.MediaType
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Serializable
data class AiringScheduleWidgetData(
    val data: List<AiringScheduleModelData>?
)

@Serializable
data class AiringScheduleModelData(
    val id: Int,
    val airingAt: Int,
    val episode: Int,
    val currentEpisode: Int? = null,
    val media: AiringScheduleMediaModelData? = null,
    val mediaId: Int,
    val timeUntilAiring: Int,
)

@Serializable
data class AiringScheduleMediaModelData(
    val id: Int,
    val title: MediaTitleModel?,
    val coverImageModel: MediaCoverImageModel?,
    val type: MediaType?,
    val isAdult: Boolean
)

fun AiringScheduleModel.toData(): AiringScheduleModelData {
    return AiringScheduleModelData(
        id = id,
        airingAt = airingAt,
        episode = episode,
        currentEpisode = currentEpisode,
        mediaId = mediaId,
        timeUntilAiring = timeUntilAiring,
        media = AiringScheduleMediaModelData(
            id = media!!.id,
            title = media.title,
            coverImageModel = media.coverImage,
            type = media.type,
            isAdult = media.isAdult
        )
    )
}

fun AiringScheduleModelData.toModel(): AiringScheduleModel {
    return AiringScheduleModel(
        id = id,
        airingAt = airingAt,
        airingAtModel = AiringAtModel(
            LocalDateTime.ofInstant(
                Instant.ofEpochSecond(
                    airingAt.toLong()
                ), ZoneOffset.systemDefault()
            )
        ),
        episode = episode,
        currentEpisode = (episode - 1).takeIf { it > 0 },
        mediaId = mediaId,
        media = media?.let {
            MediaModel(
                id = id,
                title = it.title,
                coverImage = it.coverImageModel,
                isAdult = it.isAdult,
                type = it.type
            )
        },
        timeUntilAiring = timeUntilAiring,
        timeUntilAiringModel = TimeUntilAiringModel(
            timeUntilAired = timeUntilAiring.toLong(),
            airingAt = airingAt.toLong()
        ),
    )
}