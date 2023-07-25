package com.revolgenx.anilib.user.data.field

import androidx.annotation.StringRes
import com.revolgenx.anilib.R
import com.revolgenx.anilib.UserFavouriteQuery
import com.revolgenx.anilib.common.data.field.BaseSourceUserField


enum class UserFavouriteType(@StringRes val res: Int){
    FAVOURITE_ANIME(R.string.anime),
    FAVOURITE_MANGA(R.string.manga),
    CHARACTER(R.string.character),
    STAFF(R.string.staff),
    STUDIO(R.string.studio)
}

data class UserFavouriteField(val type: UserFavouriteType): BaseSourceUserField<UserFavouriteQuery>() {
    override fun toQueryOrMutation(): UserFavouriteQuery {
        return UserFavouriteQuery(
            id = nn(userId),
            name = nn(userName),
            page = nn(page),
            perPage = nn(perPage),
            includeAnime = type == UserFavouriteType.FAVOURITE_ANIME,
            includeManga = type == UserFavouriteType.FAVOURITE_MANGA,
            includeCharacter = type == UserFavouriteType.CHARACTER,
            includeStaff = type == UserFavouriteType.STAFF,
            includeStudio = type == UserFavouriteType.STUDIO
        )
    }
}