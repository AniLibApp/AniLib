package com.revolgenx.anilib.user.data.field

import com.revolgenx.anilib.UserStatsQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.UserStatisticsSort

class UserStatsField : BaseSourceField<UserStatsQuery>() {
    var type: Int? = null
    var userStatsType: UserStatsType? = null
    var userName: String? = null
    var userId: Int? = null
    var userStatsSort: Int? = null
    override fun toQueryOrMutation(): UserStatsQuery {
        val mSort = userStatsSort?.let {
            listOf(UserStatisticsSort.values()[it])
        }

        var includeAnime = false
        var includeManga = false
        var includeGenre = false
        var includeTag = false
        var includeStaff = false
        var includeStudio = false
        var includeVoiceActor = false


        when (type) {
            MediaType.ANIME.ordinal -> {
                includeAnime = true
                when (userStatsType) {
                    UserStatsType.GENRE -> {
                        includeGenre = true
                    }
                    UserStatsType.TAG -> {
                        includeTag = true
                    }
                    UserStatsType.STAFF -> {
                        includeStaff = true
                    }
                    UserStatsType.STUDIO -> {
                        includeStudio = true
                    }
                    UserStatsType.VOICE_ACTOR -> {
                        includeVoiceActor = true
                    }
                    null -> {
                    }
                }
            }

            MediaType.MANGA.ordinal -> {
                includeManga = true
                when (userStatsType?.ordinal) {
                    UserStatsType.GENRE.ordinal -> {
                        includeGenre = true
                    }
                    UserStatsType.TAG.ordinal -> {
                        includeTag = true
                    }
                    UserStatsType.STAFF.ordinal -> {
                        includeStaff = true
                    }
                }
            }
        }
        return UserStatsQuery(
            id = nn(userId), name = nn(userName), sort = nn(mSort),
            includeAnime = nn(includeAnime),
            includeManga = nn(includeManga),
            includeTag = nn(includeTag),
            includeGenre = nn(includeGenre),
            includeStaff = nn(includeStaff),
            includeStudio = nn(includeStudio),
            includeVoiceActor = nn(includeVoiceActor)
        )
    }
    enum class UserStatsType {
        GENRE, TAG, STAFF, STUDIO, VOICE_ACTOR
    }
}
