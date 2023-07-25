package com.revolgenx.anilib.user.ui.viewmodel.userStats

import androidx.annotation.StringRes
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.R

enum class UserStatsType(@StringRes val res: Int) {
    OVERVIEW(R.string.overview),
    GENRE(R.string.genre),
    TAGS(R.string.tags),
    VOICE_ACTORS(R.string.voice_actors),
    STUDIO(R.string.studio),
    STAFF(R.string.staff)
}

enum class UserStatsTypeMediaType{
    USER_STATS_ANIME,
    USER_STATS_MANGA,
    USER_STATS_OVERVIEW_ANIME,
    USER_STATS_OVERVIEW_MANGA,
    USER_STATS_GENRE_ANIME,
    USER_STATS_GENRE_MANGA,
    USER_STATS_TAGS_ANIME,
    USER_STATS_TAGS_MANGA,
    USER_STATS_STAFF_ANIME,
    USER_STATS_STAFF_MANGA,
}

class UserStatsViewModel : ViewModel() {
    val statsScreenType = mutableStateOf(UserStatsType.OVERVIEW)
}