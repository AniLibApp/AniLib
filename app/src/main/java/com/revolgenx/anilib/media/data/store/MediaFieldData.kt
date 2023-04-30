package com.revolgenx.anilib.media.data.store

import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.type.MediaFormat
import com.revolgenx.anilib.type.MediaSeason
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.type.MediaStatus
import kotlinx.serialization.Serializable

@Serializable
data class MediaFieldData(
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
    fun toField() = MediaField(
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

    fun compare(other: MediaField): Boolean {
        if (seasonYear != other.seasonYear) return false
        if (year != other.year) return false
        if (season != other.season) return false
        if (status != other.status) return false
        if (formatsIn != other.formatsIn) return false
        if (genreIn != other.genreIn) return false
        if (tagIn != other.tagIn) return false
        if (genreNotIn != other.genreNotIn) return false
        if (tagNotIn != other.tagNotIn) return false
        if (sort != other.sort) return false
        if (idIn != other.idIn) return false
        if (isAdult != other.isAdult) return false
        return true
    }

    fun toFieldIfDifferent(field: MediaField): MediaField {
        return if (compare(field)) {
            field
        } else {
            toField()
        }
    }

}