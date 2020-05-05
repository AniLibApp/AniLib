package com.revolgenx.anilib.preference

import android.content.Context
import com.revolgenx.anilib.field.SeasonField
import com.revolgenx.anilib.field.recommendation.RecommendationFilterField
import com.revolgenx.anilib.type.MediaSeason
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.type.MediaStatus


const val SEASON_FORMAT_KEY = "SEASON_FORMAT_KEY"
const val SEASON_YEAR_KEY = "SEASON_YEAR_KEY"
const val SEASON_KEY = "SEASON_KEY"
const val SEASON_STATUS_KEY = "SEASON_STATUS_KEY"
const val SEASON_GENRES_KEY = "SEASON_GENRES_KEY"
const val SEASON_SORT_KEY = "SEASON_sort_KEY"

const val RECOMMENDATION_ON_LIST_KEY = "RECOMMENDATION_ON_LIST_KEY"
const val RECOMMENDATION_SORT_KEY = "RECOMMENDATION_SORT_KEY"

fun getSeasonField(context: Context) = SeasonField().apply {
    format = context.getInt(SEASON_FORMAT_KEY, -1).takeIf { it != -1 }
    seasonYear = context.getInt(SEASON_YEAR_KEY, 2019)//LocalDateTime.now().year)
    season = context.getInt(
        SEASON_KEY,
        MediaSeason.WINTER.ordinal
    )
    //getSeasonFromMonth(LocalDateTime.now().monthValue).ordinal -1 ), //todo remove -1
    status = context.getInt(SEASON_STATUS_KEY, -1).takeIf { it != -1 }
    sort = context.getInt(SEASON_SORT_KEY, -1).takeIf { it != -1 }
    genres = context.sharedPreference().getStringSet(SEASON_GENRES_KEY, emptySet())!!.toList()
}

fun getRecommendationField(context: Context) = RecommendationFilterField(
    onList = context.getBoolean(RECOMMENDATION_ON_LIST_KEY, false),
    sorting = context.getInt(RECOMMENDATION_SORT_KEY, 0)
)

fun setRecommendationField(context: Context, field: RecommendationFilterField) {
    context.putBoolean(RECOMMENDATION_ON_LIST_KEY, field.onList)
    context.putInt(RECOMMENDATION_SORT_KEY, field.sorting)
}