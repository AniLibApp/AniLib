package com.revolgenx.anilib.preference

import android.content.Context
import com.revolgenx.anilib.field.home.SeasonField
import com.revolgenx.anilib.field.home.TrendingMediaField
import com.revolgenx.anilib.field.recommendation.RecommendationFilterField
import com.revolgenx.anilib.util.getSeasonFromMonth
import org.threeten.bp.LocalDateTime


const val SEASON_FORMAT_KEY = "SEASON_FORMAT_KEY"
const val SEASON_YEAR_KEY = "SEASON_YEAR_KEY"
const val SEASON_KEY = "SEASON_KEY"
const val SEASON_STATUS_KEY = "SEASON_STATUS_KEY"
const val SEASON_GENRES_KEY = "SEASON_GENRES_KEY"
const val SEASON_TAGS_KEY = "SEASON_TAGS_KEY"
const val SEASON_SORT_KEY = "SEASON_sort_KEY"

//trending

const val TRENDING_FORMAT_KEY = "SEASON_FORMAT_KEY"
const val TRENDING_YEAR_KEY = "SEASON_YEAR_KEY"
const val TRENDING_SEASON_KEY = "SEASON_KEY"
const val TRENDING_STATUS_KEY = "SEASON_STATUS_KEY"

const val RECOMMENDATION_ON_LIST_KEY = "RECOMMENDATION_ON_LIST_KEY"
const val RECOMMENDATION_SORT_KEY = "RECOMMENDATION_SORT_KEY"

const val DEFAULT_FORMAT = -1
const val DEFAULT_STATUS = -1
const val DEFAULT_SORT = -1

fun getSeasonField(context: Context) = SeasonField().apply {
    format = context.getInt(SEASON_FORMAT_KEY, DEFAULT_FORMAT).takeIf { it > -1 }
    seasonYear = context.getInt(SEASON_YEAR_KEY, LocalDateTime.now().year)
    season = context.getInt(
        SEASON_KEY,
        getSeasonFromMonth(LocalDateTime.now().monthValue).ordinal
    )
    status = context.getInt(SEASON_STATUS_KEY, DEFAULT_STATUS).takeIf { it > -1 }
    sort = context.getInt(SEASON_SORT_KEY, DEFAULT_SORT).takeIf { it > -1 }
    tags = context.sharedPreference().getStringSet(SEASON_TAGS_KEY, emptySet())!!.toList()
    genres = context.sharedPreference().getStringSet(SEASON_GENRES_KEY, emptySet())!!.toList()
}

fun getTrendingField(context: Context) = TrendingMediaField().apply {
    format = context.getInt(TRENDING_FORMAT_KEY, DEFAULT_FORMAT).takeIf { it > -1 }
    seasonYear = context.getInt(TRENDING_YEAR_KEY, -1).takeIf { it > -1 }
    season = context.getInt(
        TRENDING_SEASON_KEY,
        -1
    ).takeIf { it > -1 }
    status = context.getInt(TRENDING_STATUS_KEY, DEFAULT_STATUS).takeIf { it > -1 }
}


fun storeSeasonField(context: Context, field: SeasonField) {
    with(field) {
        context.putInt(SEASON_FORMAT_KEY, format ?: DEFAULT_FORMAT)
        context.putInt(SEASON_YEAR_KEY, seasonYear ?: LocalDateTime.now().year)
        context.putInt(
            SEASON_KEY,
            season ?: getSeasonFromMonth(LocalDateTime.now().monthValue).ordinal
        )
        context.putInt(SEASON_STATUS_KEY, status ?: DEFAULT_STATUS)
        context.putInt(SEASON_SORT_KEY, sort ?: DEFAULT_SORT)
    }
}


fun storeTrendingField(context: Context, field: TrendingMediaField) {
    with(field) {
        context.putInt(TRENDING_FORMAT_KEY, format ?: DEFAULT_FORMAT)
        context.putInt(TRENDING_YEAR_KEY, seasonYear ?: -1)
        context.putInt(
            TRENDING_SEASON_KEY,
            season ?: -1
        )
        context.putInt(TRENDING_STATUS_KEY, status ?: DEFAULT_STATUS)
    }
}

fun storeSeasonGenre(context: Context, field: SeasonField) {
    context.sharedPreference().edit()
        .putStringSet(SEASON_GENRES_KEY, field.genres?.toSet() ?: emptySet()).apply()
}

fun storeSeasonTag(context: Context, field: SeasonField) {
    context.sharedPreference().edit()
        .putStringSet(SEASON_TAGS_KEY, field.tags?.toSet() ?: emptySet()).apply()
}

fun getRecommendationField(context: Context) = RecommendationFilterField(
    onList = context.getBoolean(RECOMMENDATION_ON_LIST_KEY, false),
    sorting = context.getInt(RECOMMENDATION_SORT_KEY, 0)
)

fun setRecommendationField(context: Context, field: RecommendationFilterField) {
    context.putBoolean(RECOMMENDATION_ON_LIST_KEY, field.onList)
    context.putInt(RECOMMENDATION_SORT_KEY, field.sorting)
}