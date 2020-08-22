package com.revolgenx.anilib.field.user

import com.revolgenx.anilib.UserFavouriteQuery
import com.revolgenx.anilib.constant.SearchTypes
import com.revolgenx.anilib.field.BaseSourceField

class UserFavouriteField : BaseSourceField<UserFavouriteQuery>() {

    var userId: Int? = null
    var userName: String? = null
    var favType: SearchTypes? = null
    override fun toQueryOrMutation(): UserFavouriteQuery {
        return UserFavouriteQuery.builder().apply {
            userId?.let {
                id(userId)
            }
            userName?.let {
                name(userName)
            }

            page(page)
            perPage(perPage)
            when (favType) {
                SearchTypes.ANIME -> {
                    includeAnime(true)
                    includeManga(false)
                    includeCharacter(false)
                    includeStaff(false)
                    includeStudio(false)
                }
                SearchTypes.MANGA -> {

                    includeAnime(false)
                    includeManga(true)
                    includeCharacter(false)
                    includeStaff(false)
                    includeStudio(false)
                }
                SearchTypes.CHARACTER -> {

                    includeAnime(false)
                    includeManga(false)
                    includeCharacter(true)
                    includeStaff(false)
                    includeStudio(false)
                }
                SearchTypes.STAFF -> {

                    includeAnime(false)
                    includeManga(false)
                    includeCharacter(false)
                    includeStaff(true)
                    includeStudio(false)
                }
                SearchTypes.STUDIO -> {

                    includeAnime(false)
                    includeManga(false)
                    includeCharacter(false)
                    includeStaff(false)
                    includeStudio(true)
                }
                else -> {

                }
            }
        }.build()
    }
}
