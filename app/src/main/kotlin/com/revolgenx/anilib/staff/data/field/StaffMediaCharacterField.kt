package com.revolgenx.anilib.staff.data.field

import com.revolgenx.anilib.StaffCharacterMediaQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.type.MediaSort

enum class StaffMediaCharacterSort {
    POPULARITY_DESC,
    SCORE_DESC,
    FAVOURITES_DESC,
    START_DATE_DESC,
    START_DATE,
    TITLE_ROMAJI,
    CHARACTER_SORT
}

data class StaffMediaCharacterField(
    var staffId: Int? = null,
    var onList: Boolean = false,
    var sort: StaffMediaCharacterSort = StaffMediaCharacterSort.START_DATE_DESC,
    var sortCharacter: Boolean = false
) : BaseSourceField<StaffCharacterMediaQuery>() {

    override var perPage: Int = 30
    override fun toQueryOrMutation(): StaffCharacterMediaQuery {
        return StaffCharacterMediaQuery(
            staffId = nn(staffId),
            page = nn(page),
            perPage = nn(perPage),
            onList = nnBool(onList),
            sort = nn(sort.takeIf { it != StaffMediaCharacterSort.CHARACTER_SORT }?.let { listOf(MediaSort.safeValueOf(it.name)) }),
            sortCharacter = sortCharacter
        )
    }
}