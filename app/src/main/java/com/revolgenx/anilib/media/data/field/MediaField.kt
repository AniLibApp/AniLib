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
        return MediaQuery.builder()
            .builder()
            .build()
    }

    protected open fun MediaQuery.Builder.builder(): MediaQuery.Builder {
        if (page != null) {
            page(page!!)
            perPage(perPage)
        }
        if (genres?.isNullOrEmpty() == false) {
            genre_in(genres)
        }
        if (tags?.isNullOrEmpty() == false) {
            tag_in(tags)
        }
        sort?.let {
            sort(listOf(MediaSort.values()[it]))
        }
        seasonYear?.let {
            seasonYear(it)
        }
        season?.let {
            season(MediaSeason.values()[it])
        }
        format?.let {
            format(MediaFormat.values()[it])
        }
        formatsIn?.let {
            val formats = MediaFormat.values()
            format_in(it.map { formats[it] })
        }
        status?.let {
            status(MediaStatus.values()[it])
        }
        mediaIdsIn?.let {
            idIn(it)
        }
        if (!canShowAdult) {
            isAdult(false)
        }

        includeStaff(includeStaff)
        includeStudio(includeStudio)
        return this
    }

}
