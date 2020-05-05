package com.revolgenx.anilib.field.media

import com.revolgenx.anilib.MediaQuery
import com.revolgenx.anilib.field.BaseSourceField
import com.revolgenx.anilib.type.MediaFormat
import com.revolgenx.anilib.type.MediaSeason
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.type.MediaStatus

open class MediaField : BaseSourceField<MediaQuery>() {
    var genres: List<String>? = null
    var format: Int? = null
    var sort: Int? = null
    var seasonYear: Int? = null
    var season: Int? = null
    var status:Int? = null
    override fun toQueryOrMutation(): MediaQuery {
        return MediaQuery.builder()
            .page(page)
            .perPage(perPage)
            .apply {
                if (genres?.isNullOrEmpty() == false) {
                    genre_in(genres)
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
            }
            .build()
    }

}
