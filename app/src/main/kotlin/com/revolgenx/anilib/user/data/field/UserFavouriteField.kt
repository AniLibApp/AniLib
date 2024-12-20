package com.revolgenx.anilib.user.data.field

import androidx.annotation.StringRes
import com.revolgenx.anilib.UserFavouriteQuery
import com.revolgenx.anilib.common.data.field.BaseSourceUserField
import anilib.i18n.R as I18nR


enum class UserFavouriteType(@StringRes val res: Int){
    FAVOURITE_ANIME(I18nR.string.anime),
    FAVOURITE_MANGA(I18nR.string.manga),
    CHARACTER(I18nR.string.character),
    STAFF(I18nR.string.staff),
    STUDIO(I18nR.string.studio)
}

data class UserFavouriteField(val type: UserFavouriteType): BaseSourceUserField<UserFavouriteQuery>() {
    override fun toQueryOrMutation(): UserFavouriteQuery {
        return UserFavouriteQuery(
            id = nn(userId),
            name = nnString(userName),
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