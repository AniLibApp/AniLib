package com.revolgenx.anilib.media.data.field

import com.revolgenx.anilib.MediaQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.type.MediaFormat
import com.revolgenx.anilib.type.MediaSeason
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.type.MediaStatus

open class MediaField : BaseSourceField<MediaQuery>() {
    var genres: List<String>? = null
    var tags: List<String>? = null
    var format: Int? = null
    var sort: Int? = null
    var seasonYear: Int? = null
    var season: Int? = null
    var status: Int? = null
    var mediaIdsIn: List<Int>? = null

    var formatsIn: MutableList<Int>? = null
    open var includeStaff = false
    open var includeStudio = false

    override fun toQueryOrMutation(): MediaQuery {

        val mediaSort = sort?.let {
            listOf(MediaSort.values()[it])
        }

        val mediaSeason = season?.let {
            MediaSeason.values()[it]
        }

        val mediaFormatsIn = formatsIn?.let {
            val formats = MediaFormat.values()
            it.map { formats[it] }
        }

        val mediaFormat = format?.let {
            MediaFormat.values()[it]
        }
        val mediaStatus = status?.let {
            MediaStatus.values()[it]
        }

        return MediaQuery(
            page = nn(page),
            perPage = nn(perPage),
            season = nn(mediaSeason),
            seasonYear = nn(seasonYear),
            sort = nn(mediaSort),
            format_in = nn(mediaFormatsIn),
            tag_in = nn(tags),
            idIn = nn(mediaIdsIn),
            isAdult = nn(canShowAdult),
            format = nn(mediaFormat),
            status =  nn(mediaStatus),
            includeStaff = nn(includeStaff),
            includeStudio = nn(includeStudio),
            genre_in = nn(genres)
        )
    }

}
