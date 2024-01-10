package com.revolgenx.anilib.media.data.field

import com.revolgenx.anilib.MediaPageQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.media.data.store.MediaFilterData
import com.revolgenx.anilib.type.MediaFormat
import com.revolgenx.anilib.type.MediaSeason
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.type.MediaStatus

data class MediaField(
    var seasonYear: Int? = null,
    var year: Int? = null,
    var season: MediaSeason? = null,
    var status: MediaStatus? = null,
    var formatsIn: List<MediaFormat>? = null,
    var genreIn: List<String>? = null,
    var tagIn: List<String>? = null,
    var genreNotIn: List<String>? = null,
    var tagNotIn: List<String>? = null,
    var sort: MediaSort? = null,
    var idIn: List<Int>? = null,
    var isAdult: Boolean? = null
) : BaseSourceField<MediaPageQuery>() {

    override fun toQueryOrMutation(): MediaPageQuery {
        return MediaPageQuery(
            page = nn(page),
            perPage = nn(perPage),
            season = nn(season),
            seasonYear = nn(seasonYear),
            year = nnString(year?.let { "$it%" }),
            sort = nn(sort?.let { listOf(it) }),
            format_in = nn(formatsIn),
            idIn = nn(idIn),
            isAdult = nn(isAdult ?: canShowAdult.takeIf { it.not() }),
            status = nn(status),
            genre_in = nn(genreIn),
            tag_in = nn(tagIn)
        )
    }

    fun toMediaFilter() = MediaFilterData(
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


