package com.revolgenx.anilib.studio.data.field

import com.revolgenx.anilib.StudioMediaQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.type.MediaSort


enum class StudioMediaSort {
    POPULARITY_DESC,
    SCORE_DESC,
    FAVOURITES_DESC,
    START_DATE_DESC,
    START_DATE,
    TITLE_ROMAJI,
    TITLE_ENGLISH,
    TITLE_NATIVE,
}

data class StudioField(
    var studioId: Int = -1,
    var onList: Boolean = false,
    var sort: StudioMediaSort = StudioMediaSort.START_DATE_DESC
) : BaseSourceField<StudioMediaQuery>() {

    override fun toQueryOrMutation(): StudioMediaQuery {
        return StudioMediaQuery(
            page = nn(page),
            perPage = nn(perPage),
            studioId = nn(studioId),
            onList = nnBool(onList),
            sort = nn(listOf(MediaSort.safeValueOf(sort.name)))
        )
    }
}