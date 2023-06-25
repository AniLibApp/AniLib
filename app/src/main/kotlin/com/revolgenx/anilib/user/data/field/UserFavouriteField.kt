package com.revolgenx.anilib.user.data.field

import com.revolgenx.anilib.R
import com.revolgenx.anilib.UserFavouriteQuery
import com.revolgenx.anilib.common.data.field.BaseSourceUserField


enum class UserFavouriteType{
    ANIME, MANGA, CHARACTER, STAFF, STUDIO
}

fun UserFavouriteType.toStringRes() = when(this){
    UserFavouriteType.ANIME -> R.string.anime
    UserFavouriteType.MANGA -> R.string.manga
    UserFavouriteType.CHARACTER -> R.string.character
    UserFavouriteType.STAFF -> R.string.staff
    UserFavouriteType.STUDIO -> R.string.studio
}

data class UserFavouriteField(val type: UserFavouriteType): BaseSourceUserField<UserFavouriteQuery>() {
    override fun toQueryOrMutation(): UserFavouriteQuery {
        return UserFavouriteQuery(
            id = nn(userId),
            name = nn(userName),
            page = nn(page),
            perPage = nn(perPage),
            includeAnime = type == UserFavouriteType.ANIME,
            includeManga = type == UserFavouriteType.MANGA,
            includeCharacter = type == UserFavouriteType.CHARACTER,
            includeStaff = type == UserFavouriteType.STAFF,
            includeStudio = type == UserFavouriteType.STUDIO
        )
    }
}