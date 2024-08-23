package com.revolgenx.anilib.airing.ui.model

import android.os.Handler
import android.os.Looper
import com.revolgenx.anilib.AiringScheduleQuery
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.toModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset


data class AiringScheduleModel(
    val id: Int = -1,
    val airingAt: Int = -1,
    val airingAtModel: AiringAtModel,
    val episode: Int = -1,
    val currentEpisode: Int? = null,
    val media: MediaModel? = null,
    val mediaId: Int = -1,
    val timeUntilAiring: Int = -1,
    val timeUntilAiringModel: TimeUntilAiringModel,
    val airingScheduleTimer: AiringScheduleTimer? = null
) : BaseModel

fun AiringScheduleQuery.AiringSchedule.toModel(): AiringScheduleModel {
    val timeUntilAiringModel = TimeUntilAiringModel(timeUntilAiring.toLong())
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
        media = media?.media?.toModel(),
        timeUntilAiring = timeUntilAiring,
        timeUntilAiringModel = timeUntilAiringModel,
        airingScheduleTimer = AiringScheduleTimer(
            airingAt,
            timeUntilAiringModel
        )
    )
}