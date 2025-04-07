package com.revolgenx.anilib.user.ui.viewmodel.userStats

import androidx.annotation.StringRes
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import anilib.i18n.R as I18nR

enum class UserStatsType(@StringRes val res: Int) {
    OVERVIEW(I18nR.string.overview),
    GENRE(I18nR.string.genre),
    TAGS(I18nR.string.tags),
    VOICE_ACTORS(I18nR.string.voice_actors),
    STUDIO(I18nR.string.studio),
    STAFF(I18nR.string.staff)
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