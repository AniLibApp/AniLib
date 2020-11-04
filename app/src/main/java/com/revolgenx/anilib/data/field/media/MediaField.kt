package com.revolgenx.anilib.data.field.media

import com.revolgenx.anilib.MediaQuery
import com.revolgenx.anilib.data.field.BaseSourceField
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
    override fun toQueryOrMutation(): MediaQuery {
        return MediaQuery.builder()
            .page(page)
            .perPage(perPage)
            .apply {
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
                status?.let {
                    status(MediaStatus.values()[it])
                }
                mediaIdsIn?.let {
                    idIn(it)
                }
            }
            .build()
    }

}
