package com.revolgenx.anilib.media.data.model

import com.revolgenx.anilib.common.data.filter.BaseFilter
import com.revolgenx.anilib.media.ui.model.seasonFromMonth
import com.revolgenx.anilib.type.MediaFormat
import com.revolgenx.anilib.type.MediaSeason
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.type.MediaStatus
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class MediaFilterModel(
    val seasonYear: Int? = null,
    val year: Int? = null,
    val season: MediaSeason? = null,
    val status: MediaStatus? = null,
    val formatsIn: List<MediaFormat>? = null,
    val genreIn: List<String>? = null,
    val tagIn: List<String>? = null,
    val genreNotIn: List<String>? = null,
    val tagNotIn: List<String>? = null,
    val sort: MediaSort? = null,
    val idIn: List<Int>? = null,
    val isAdult: Boolean? = null
) : BaseFilter{
    companion object{
        fun default() = MediaFilterModel(
            seasonYear = LocalDateTime.now().year,
            season = seasonFromMonth(LocalDateTime.now().monthValue)
        )
    }
}