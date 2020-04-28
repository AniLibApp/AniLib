package com.revolgenx.anilib.field.stats

import com.revolgenx.anilib.UserStatsQuery
import com.revolgenx.anilib.field.BaseSourceField
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.UserStatisticsSort

class UserStatsField : BaseSourceField<UserStatsQuery>() {
    var type: Int? = null
    var statsType: UserStatsType? = null
    var userName: String? = null
    var userId: Int? = null
    var userStatsSort: Int? = null
    override fun toQueryOrMutation(): UserStatsQuery {
        return UserStatsQuery.builder()
            .apply {
                userId?.let {
                    id(it)
                }
                userName?.let {
                    name(it)
                }

                userStatsSort?.let {
                    sort(listOf(UserStatisticsSort.values()[it]))
                }

                when (type) {
                    MediaType.ANIME.ordinal -> {
                        includeAnime(true)
                        when (statsType) {
                            UserStatsType.GENRE -> {
                                includeGenre(true)
                            }
                            UserStatsType.TAG -> {
                                includeTag(true)
                            }
                            UserStatsType.STAFF -> {
                                includeStaff(true)
                            }
                            UserStatsType.STUDIO -> {
                                includeStudio(true)
                            }
                            UserStatsType.VOICE_ACTOR -> {
                                includeVoiceActor(true)
                            }
                            null -> {
                            }
                        }
                    }

                    MediaType.MANGA.ordinal -> {
                        includeManga(true)
                        when (statsType?.ordinal) {
                            UserStatsType.GENRE.ordinal -> {
                                includeGenre(true)
                            }
                            UserStatsType.TAG.ordinal -> {
                                includeTag(true)
                            }
                            UserStatsType.STAFF.ordinal -> {
                                includeStaff(true)
                            }
                        }
                    }
                }

            }
            .build()
    }

    enum class UserStatsType {
        GENRE, TAG, STAFF, STUDIO, VOICE_ACTOR
    }
}
