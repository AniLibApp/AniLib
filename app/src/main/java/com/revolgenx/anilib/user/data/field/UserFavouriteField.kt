package com.revolgenx.anilib.user.data.field

import com.revolgenx.anilib.UserFavouriteQuery
import com.revolgenx.anilib.search.data.field.SearchTypes
import com.revolgenx.anilib.common.data.field.BaseSourceField

class UserFavouriteField : BaseSourceField<UserFavouriteQuery>() {

    var userId: Int? = null
    var userName: String? = null
    var favType: SearchTypes? = null
    override fun toQueryOrMutation(): UserFavouriteQuery {

        var includeAnime: Boolean = false
        var includeManga: Boolean = false
        var includeCharacter: Boolean = false
        var includeStaff: Boolean = false
        var includeStudio: Boolean = false

        when (favType) {
            SearchTypes.ANIME -> {
                includeAnime = true
                includeManga = false
                includeCharacter = false
                includeStaff = false
                includeStudio = false
            }
            SearchTypes.MANGA -> {

                includeAnime = false
                includeManga = true
                includeCharacter = false
                includeStaff = false
                includeStudio = false
            }
            SearchTypes.CHARACTER -> {

                includeAnime = false
                includeManga = false
                includeCharacter = true
                includeStaff = false
                includeStudio = false
            }
            SearchTypes.STAFF -> {

                includeAnime = false
                includeManga = false
                includeCharacter = false
                includeStaff = true
                includeStudio = false
            }
            SearchTypes.STUDIO -> {

                includeAnime = false
                includeManga = false
                includeCharacter = false
                includeStaff = false
                includeStudio = true
            }
            else -> {}
        }
        return UserFavouriteQuery(
            id = nn(userId),
            name = nn(userName),
            page = nn(page),
            perPage = nn(perPage),
            includeAnime = includeAnime,
            includeManga = includeManga,
            includeCharacter = includeCharacter,
            includeStaff = includeStaff,
            includeStudio = includeStudio
        )
    }
}
