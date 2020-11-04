package com.revolgenx.anilib.common.preference

import android.content.Context
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences
import com.revolgenx.anilib.data.field.home.NewlyAddedMediaField
import com.revolgenx.anilib.data.field.home.PopularMediaField
import com.revolgenx.anilib.data.field.home.SeasonField
import com.revolgenx.anilib.data.field.home.TrendingMediaField
import com.revolgenx.anilib.data.field.recommendation.RecommendationFilterField
import com.revolgenx.anilib.data.model.home.HomeOrderType
import com.revolgenx.anilib.type.MediaType
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

const val TRENDING_FORMAT_KEY = "TRENDING_FORMAT_KEY"
const val TRENDING_YEAR_KEY = "TRENDING_YEAR_KEY"
const val TRENDING_SEASON_KEY = "TRENDING_SEASON_KEY"
const val TRENDING_STATUS_KEY = "TRENDING_STATUS_KEY"

const val POPULAR_FORMAT_KEY = "POPULAR_FORMAT_KEY"
const val POPULAR_YEAR_KEY = "POPULAR_YEAR_KEY"
const val POPULAR_SEASON_KEY = "POPULAR_SEASON_KEY"
const val POPULAR_STATUS_KEY = "POPULAR_STATUS_KEY"

const val NEWLY_ADDED_FORMAT_KEY = "NEWLY_ADDED_FORMAT_KEY"
const val NEWLY_ADDED_YEAR_KEY = "NEWLY_ADDED_YEAR_KEY"
const val NEWLY_ADDED_SEASON_KEY = "NEWLY_ADDED_SEASON_KEY"
const val NEWLY_ADDED_STATUS_KEY = "NEWLY_ADDED_STATUS_KEY"


const val RECOMMENDATION_ON_LIST_KEY = "RECOMMENDATION_ON_LIST_KEY"
const val RECOMMENDATION_SORT_KEY = "RECOMMENDATION_SORT_KEY"

const val DEFAULT_FORMAT = -1
const val DEFAULT_STATUS = -1
const val DEFAULT_SORT = -1


const val AIRING_ORDER_KEY = "AIRING_ORDER_KEY"
const val POPULAR_ORDER_KEY = "POPULAR_ORDER_KEY"
const val TRENDING_ORDER_KEY = "TRENDING_ORDER_KEY"
const val NEWLY_ADDED_ORDER_KEY = "NEWLY_ADDED_ORDER_KEY"
const val WATCHING_ORDER_KEY = "WATCHING_ORDER_KEY"
const val READING_ORDER_KEY = "READING_ORDER_KEY"

const val DISCOVER_READING_SORT_KEY = "DISCOVER_READING_SORT_KEY"
const val DISCOVER_WATCHING_SORT_KEY = "DISCOVER_WATCHING_SORT_KEY"

const val MEDIA_LIST_GRID_PRESENTER_KEY = "MEDIA_LIST_GRID_PRESENTER_KEY"


fun getSeasonField(context: Context) = SeasonField().apply {
    format = context.getInt(SEASON_FORMAT_KEY, DEFAULT_FORMAT).takeIf { it > -1 }
    seasonYear = context.getInt(SEASON_YEAR_KEY, LocalDateTime.now().year)
    season = context.getInt(
        SEASON_KEY,
        getSeasonFromMonth(LocalDateTime.now().monthValue).ordinal
    ).takeIf { it > -1 }
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

fun getPopularField(context: Context) = PopularMediaField().apply {
    format = context.getInt(POPULAR_FORMAT_KEY, DEFAULT_FORMAT).takeIf { it > -1 }
    seasonYear = context.getInt(POPULAR_YEAR_KEY, -1).takeIf { it > -1 }
    season = context.getInt(
        POPULAR_SEASON_KEY,
        -1
    ).takeIf { it > -1 }
    status = context.getInt(POPULAR_STATUS_KEY, DEFAULT_STATUS).takeIf { it > -1 }
}


fun getNewlyAddedField(context: Context) = NewlyAddedMediaField().apply {
    format = context.getInt(NEWLY_ADDED_FORMAT_KEY, DEFAULT_FORMAT).takeIf { it > -1 }
    seasonYear = context.getInt(NEWLY_ADDED_YEAR_KEY, -1).takeIf { it > -1 }
    season = context.getInt(
        NEWLY_ADDED_SEASON_KEY,
        -1
    ).takeIf { it > -1 }
    status = context.getInt(NEWLY_ADDED_STATUS_KEY, DEFAULT_STATUS).takeIf { it > -1 }
}


fun storeNewlyAddedField(context: Context, field: NewlyAddedMediaField) {
    with(field) {
        context.putInt(NEWLY_ADDED_FORMAT_KEY, format ?: DEFAULT_FORMAT)
        context.putInt(NEWLY_ADDED_YEAR_KEY, seasonYear ?: LocalDateTime.now().year)
        context.putInt(
            NEWLY_ADDED_SEASON_KEY,
            season ?: getSeasonFromMonth(LocalDateTime.now().monthValue).ordinal
        )
        context.putInt(NEWLY_ADDED_STATUS_KEY, status ?: DEFAULT_STATUS)
    }
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

fun storePopularField(context: Context, field: PopularMediaField) {
    with(field) {
        context.putInt(POPULAR_FORMAT_KEY, format ?: DEFAULT_FORMAT)
        context.putInt(POPULAR_YEAR_KEY, seasonYear ?: -1)
        context.putInt(
            POPULAR_SEASON_KEY,
            season ?: -1
        )
        context.putInt(POPULAR_STATUS_KEY, status ?: DEFAULT_STATUS)
    }
}


fun storeSeasonGenre(context: Context, field: SeasonField?) {
    context.sharedPreference().edit()
        .putStringSet(SEASON_GENRES_KEY, field?.genres?.toSet() ?: emptySet()).apply()
}

fun storeSeasonTag(context: Context, field: SeasonField?) {
    context.sharedPreference().edit()
        .putStringSet(SEASON_TAGS_KEY, field?.tags?.toSet() ?: emptySet()).apply()
}

fun getRecommendationField(context: Context) = RecommendationFilterField(
    onList = context.getBoolean(RECOMMENDATION_ON_LIST_KEY, false),
    sorting = context.getInt(RECOMMENDATION_SORT_KEY, 0)
)

fun setRecommendationField(context: Context, field: RecommendationFilterField) {
    context.putBoolean(RECOMMENDATION_ON_LIST_KEY, field.onList)
    context.putInt(RECOMMENDATION_SORT_KEY, field.sorting)
}

fun getHomeOrderFromType(context: Context, type: HomeOrderType): Int {
    return when (type) {
        HomeOrderType.AIRING -> context.getInt(AIRING_ORDER_KEY, 0)
        HomeOrderType.TRENDING -> context.getInt(TRENDING_ORDER_KEY, 1)
        HomeOrderType.POPULAR -> context.getInt(POPULAR_ORDER_KEY, 2)
        HomeOrderType.NEWLY_ADDED -> context.getInt(NEWLY_ADDED_ORDER_KEY, 3)
        HomeOrderType.WATCHING -> context.getInt(WATCHING_ORDER_KEY, 4)
        HomeOrderType.READING -> context.getInt(READING_ORDER_KEY, 5)
    }
}

fun setHomeOrderFromType(context: Context, type: HomeOrderType, order: Int) {
    when (type) {
        HomeOrderType.AIRING -> context.putInt(AIRING_ORDER_KEY, order)
        HomeOrderType.TRENDING -> context.putInt(TRENDING_ORDER_KEY, order)
        HomeOrderType.POPULAR -> context.putInt(POPULAR_ORDER_KEY, order)
        HomeOrderType.NEWLY_ADDED -> context.putInt(NEWLY_ADDED_ORDER_KEY, order)
        HomeOrderType.WATCHING -> context.putInt(WATCHING_ORDER_KEY, order)
        HomeOrderType.READING -> context.putInt(READING_ORDER_KEY, order)
    }
}


fun setDiscoverMediaListSort(context: Context, type: Int, sort: Int?) {
    when (type) {
        MediaType.ANIME.ordinal -> {
            context.putInt(DISCOVER_WATCHING_SORT_KEY, sort ?: -1)
        }
        MediaType.MANGA.ordinal -> {
            context.putInt(DISCOVER_READING_SORT_KEY, sort ?: -1)
        }
    }
}


fun getDiscoverMediaListSort(context: Context, type: Int): Int? {
    return when (type) {
        MediaType.ANIME.ordinal -> {
            context.getInt(DISCOVER_WATCHING_SORT_KEY, -1).takeIf { it > -1 }
        }
        MediaType.MANGA.ordinal -> {
            context.getInt(DISCOVER_READING_SORT_KEY, -1).takeIf { it > -1 }
        }
        else -> null
    }
}


fun getMediaListGridPresenter(): Int {
    return DynamicPreferences.getInstance()
        .load(MEDIA_LIST_GRID_PRESENTER_KEY, 0) //0 for single gird
}

fun setMediaListGridPresenter(which:Int) {
    return DynamicPreferences.getInstance().save(MEDIA_LIST_GRID_PRESENTER_KEY, which)
}


