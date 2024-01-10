package com.revolgenx.anilib.media.data.store

import com.revolgenx.anilib.media.data.constant.mediaTagCollection
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.media.ui.model.MediaExternalLinkModel
import com.revolgenx.anilib.media.ui.model.MediaTagModel
import com.revolgenx.anilib.type.MediaFormat
import com.revolgenx.anilib.type.MediaSeason
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.type.MediaStatus
import kotlinx.serialization.Serializable

@Serializable
data class MediaFilterData(
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
) {
    fun toMediaField(): MediaField {
        return MediaField(
            seasonYear = seasonYear,
            year = year,
            season = season,
            status = status,
            formatsIn = formatsIn,
            genreIn = genreIn,
            tagIn = tagIn,
            genreNotIn = genreNotIn,
            tagNotIn = tagNotIn,
            sort = sort,
            idIn = idIn,
            isAdult = isAdult,
        )
    }

}

@Serializable
data class MediaTagCollectionData(
    val tags: List<MediaTagModel> = mediaTagCollection
)

@Serializable
data class ExternalLinkSourceCollectionData(val links: List<MediaExternalLinkModel>)