package com.revolgenx.anilib.user.data.field

import com.revolgenx.anilib.UserStatsQuery
import com.revolgenx.anilib.common.data.field.BaseUserField
import com.revolgenx.anilib.media.ui.model.isAnime
import com.revolgenx.anilib.media.ui.model.isManga
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.UserStatisticsSort
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsType

data class UserStatsTypeField(val mediaType: MediaType, val statsType: UserStatsType) :
    BaseUserField<UserStatsQuery>() {
    var sort: UserStatisticsSort? = null
    override fun toQueryOrMutation(): UserStatsQuery {
        val mSort = sort?.let {
            listOf(it)
        }

        return UserStatsQuery(
            id = nn(userId), name = nn(userName), sort = nn(mSort),
            includeAnime = mediaType.isAnime,
            includeManga = mediaType.isManga,
            includeTag = statsType == UserStatsType.TAGS,
            includeGenre = statsType == UserStatsType.GENRE,
            includeStaff = statsType == UserStatsType.STAFF,
            includeStudio = statsType == UserStatsType.STUDIO,
            includeVoiceActor = statsType == UserStatsType.VOICE_ACTORS
        )
    }

}