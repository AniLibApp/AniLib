package com.revolgenx.anilib.user.ui.viewmodel

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

enum class UserStatsMediaType{
    USER_STATS_ANIME,
    USER_STATS_MANGA
}

class UserStatsViewModel : ViewModel() {
    val statsScreenType = mutableStateOf(UserStatsType.OVERVIEW)
}