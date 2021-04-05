package com.revolgenx.anilib.common.preference

import android.content.Context
import com.google.gson.Gson
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences
import com.revolgenx.anilib.constant.AiringListDisplayMode
import com.revolgenx.anilib.constant.MediaListDisplayMode
import com.revolgenx.anilib.data.field.MediaListCollectionFilterField
import com.revolgenx.anilib.data.field.home.*
import com.revolgenx.anilib.data.model.home.HomeOrderType
import com.revolgenx.anilib.data.model.home.HomePageOrderType
import com.revolgenx.anilib.type.AiringSort
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.util.getSeasonFromMonth
import java.time.LocalDateTime


const val SEASON_FORMAT_IN_KEY = "SEASON_FORMAT_IN_KEY"
const val SEASON_FORMAT_KEY = "SEASON_FORMAT_KEY"
const val SEASON_YEAR_KEY = "SEASON_YEAR_KEY"
const val SEASON_KEY = "SEASON_KEY"
const val SEASON_STATUS_KEY = "SEASON_STATUS_KEY"
const val SEASON_GENRES_KEY = "SEASON_GENRES_KEY"
const val SEASON_TAGS_KEY = "SEASON_TAGS_KEY"
const val SEASON_SORT_KEY = "SEASON_sort_KEY"
const val SEASON_HEADER_KEY = "SEASON_HEADER_KEY"

//trending

const val TRENDING_FORMAT_KEY = "TRENDING_FORMAT_KEY"
const val TRENDING_FORMAT_IN_KEY = "TRENDING_FORMAT_IN_KEY"
const val TRENDING_YEAR_KEY = "TRENDING_YEAR_KEY"
const val TRENDING_SEASON_KEY = "TRENDING_SEASON_KEY"
const val TRENDING_STATUS_KEY = "TRENDING_STATUS_KEY"

const val POPULAR_FORMAT_KEY = "POPULAR_FORMAT_KEY"
const val POPULAR_FORMAT_IN_KEY = "POPULAR_FORMAT_IN_KEY"
const val POPULAR_YEAR_KEY = "POPULAR_YEAR_KEY"
const val POPULAR_SEASON_KEY = "POPULAR_SEASON_KEY"
const val POPULAR_STATUS_KEY = "POPULAR_STATUS_KEY"

const val NEWLY_ADDED_FORMAT_KEY = "NEWLY_ADDED_FORMAT_KEY"
const val NEWLY_ADDED_FORMAT_IN_KEY = "NEWLY_ADDED_FORMAT_IN_KEY"
const val NEWLY_ADDED_YEAR_KEY = "NEWLY_ADDED_YEAR_KEY"
const val NEWLY_ADDED_SEASON_KEY = "NEWLY_ADDED_SEASON_KEY"
const val NEWLY_ADDED_STATUS_KEY = "NEWLY_ADDED_STATUS_KEY"

const val DISCOVER_AIRING_NOT_AIRED_KEY = "DISCOVER_AIRING_NOT_AIRED_KEY"
const val DISCOVER_AIRING_SORT_KEY = "DISCOVER_AIRING_SORT_KEY"
const val DISCOVER_AIRING_PLANNING_KEY = "DISCOVER_AIRING_PLANNING_KEY"
const val DISCOVER_AIRING_WATCHING_KEY = "DISCOVER_AIRING_WATCHING_KEY"

const val AIRING_NOT_AIRED_KEY = "AIRING_NOT_AIRED_KEY"
const val AIRING_SORT_KEY = "AIRING_SORT_KEY"
const val AIRING_PLANNING_KEY = "AIRING_PLANNING_KEY"
const val AIRING_WATCHING_KEY = "AIRING_WATCHING_KEY"

const val ANIME_MEDIA_LIST_FILTER_KEY = "ANIME_MEDIA_LIST_FILTER_KEY"
const val MANGA_MEDIA_LIST_FILTER_KEY = "MANGA_MEDIA_LIST_FILTER_KEY"


const val RECOMMENDATION_ON_LIST_KEY = "RECOMMENDATION_ON_LIST_KEY"
const val RECOMMENDATION_SORT_KEY = "RECOMMENDATION_SORT_KEY"

const val DEFAULT_FORMAT = -1
const val DEFAULT_STATUS = -1
const val DEFAULT_SORT = -1


const val AIRING_ORDER_KEY = "AIRING_ORDER_KEY"
const val AIRING_ORDER_ENABLED_KEY = "AIRING_ORDER_ENABLED_KEY"
const val POPULAR_ORDER_KEY = "POPULAR_ORDER_KEY"
const val POPULAR_ORDER_ENABLED_KEY = "POPULAR_ORDER_ENABLED_KEY"
const val TRENDING_ORDER_KEY = "TRENDING_ORDER_KEY"
const val TRENDING_ORDER_ENABLED_KEY = "TRENDING_ORDER_ENABLED_KEY"
const val NEWLY_ADDED_ORDER_KEY = "NEWLY_ADDED_ORDER_KEY"
const val NEWLY_ADDED_ORDER_ENABLED_KEY = "NEWLY_ADDED_ORDER_ENABLED_KEY"
const val WATCHING_ORDER_KEY = "WATCHING_ORDER_KEY"
const val WATCHING_ORDER_ENABLED_KEY = "WATCHING_ORDER_ENABLED_KEY"
const val READING_ORDER_KEY = "READING_ORDER_KEY"
const val READING_ORDER_ENABLED_KEY = "READING_ORDER_ENABLED_KEY"

const val HOME_PAGE_ORDER_KEY = "HOME_PAGE_ORDER_KEY"
const val LIST_PAGE_ORDER_KEY = "LIST_PAGE_ORDER_KEY"
const val RADIO_PAGE_ORDER_KEY = "RADIO_PAGE_ORDER_KEY"

const val DISCOVER_READING_SORT_KEY = "DISCOVER_READING_SORT_KEY"
const val DISCOVER_WATCHING_SORT_KEY = "DISCOVER_WATCHING_SORT_KEY"

const val MEDIA_LIST_GRID_PRESENTER_KEY = "MEDIA_LIST_GRID_PRESENTER_KEY"
const val AIRING_DISPLAY_MODE_KEY = "AIRING_DISPLAY_MODE_KEY"

const val SHOW_AIRING_WEEKLY_KEY = "SHOW_AIRING_WEEKLY_KEY"



const val WIDGET_AIRING_NOT_AIRED_KEY = "WIDGET_AIRING_NOT_AIRED_KEY"
const val WIDGET_AIRING_SORT_KEY = "WIDGET_AIRING_SORT_KEY"
const val WIDGET_AIRING_PLANNING_KEY = "WIDGET_AIRING_PLANNING_KEY"
const val WIDGET_AIRING_WATCHING_KEY = "WIDGET_AIRING_WATCHING_KEY"
const val WIDGET_IS_AIRING_WEEKLY_KEY = "WIDGET_IS_AIRING_WEEKLY_KEY"


fun getDiscoverAiringField(context: Context) = AiringMediaField().apply {
    notYetAired = context.getBoolean(DISCOVER_AIRING_NOT_AIRED_KEY, true)
    sort = context.getInt(DISCOVER_AIRING_SORT_KEY, AiringSort.TIME.ordinal)
    showFromPlanning = context.getBoolean(DISCOVER_AIRING_PLANNING_KEY, false)
    showFromWatching = context.getBoolean(DISCOVER_AIRING_WATCHING_KEY, false)
}


fun getAiringField(context: Context) = AiringMediaField().apply {
    notYetAired = context.getBoolean(AIRING_NOT_AIRED_KEY, true)
    sort = context.getInt(AIRING_SORT_KEY, AiringSort.TIME.ordinal)
    showFromPlanning = context.getBoolean(AIRING_PLANNING_KEY, false)
    showFromWatching = context.getBoolean(AIRING_WATCHING_KEY, false)
    isWeeklyTypeDate = showAiringWeekly(context)
}


fun getSeasonField(context: Context) = SeasonField().apply {
    formatsIn = context.getString(SEASON_FORMAT_IN_KEY, "")?.takeIf { it.isNotEmpty() }?.split(",")
        ?.map { it.toInt() }?.toMutableList()
    seasonYear = context.getInt(SEASON_YEAR_KEY, LocalDateTime.now().year)
    season = context.getInt(
        SEASON_KEY,
        getSeasonFromMonth(LocalDateTime.now().monthValue).ordinal
    ).takeIf { it > -1 }
    status = context.getInt(SEASON_STATUS_KEY, DEFAULT_STATUS).takeIf { it > -1 }
    sort = context.getInt(SEASON_SORT_KEY, DEFAULT_SORT).takeIf { it > -1 }
    tags = context.sharedPreference().getStringSet(SEASON_TAGS_KEY, emptySet())!!.toList()
    genres = context.sharedPreference().getStringSet(SEASON_GENRES_KEY, emptySet())!!.toList()
    showFormatHeader = context.getBoolean(SEASON_HEADER_KEY, false)
}

fun getTrendingField(context: Context) = TrendingMediaField().apply {
    formatsIn =
        context.getString(TRENDING_FORMAT_IN_KEY, "")?.takeIf { it.isNotEmpty() }?.split(",")
            ?.map { it.toInt() }?.toMutableList()
    seasonYear = context.getInt(TRENDING_YEAR_KEY, -1).takeIf { it > -1 }
    season = context.getInt(
        TRENDING_SEASON_KEY,
        -1
    ).takeIf { it > -1 }
    status = context.getInt(TRENDING_STATUS_KEY, DEFAULT_STATUS).takeIf { it > -1 }
}

fun getPopularField(context: Context) = PopularMediaField().apply {
    formatsIn = context.getString(POPULAR_FORMAT_IN_KEY, "")?.takeIf { it.isNotEmpty() }?.split(",")
        ?.map { it.toInt() }?.toMutableList()
    seasonYear = context.getInt(POPULAR_YEAR_KEY, -1).takeIf { it > -1 }
    season = context.getInt(
        POPULAR_SEASON_KEY,
        -1
    ).takeIf { it > -1 }
    status = context.getInt(POPULAR_STATUS_KEY, DEFAULT_STATUS).takeIf { it > -1 }
}


fun getNewlyAddedField(context: Context) = NewlyAddedMediaField().apply {
    formatsIn =
        context.getString(NEWLY_ADDED_FORMAT_IN_KEY, "")?.takeIf { it.isNotEmpty() }?.split(",")
            ?.map { it.toInt() }?.toMutableList()
    seasonYear = context.getInt(NEWLY_ADDED_YEAR_KEY, -1).takeIf { it > -1 }
    season = context.getInt(
        NEWLY_ADDED_SEASON_KEY,
        -1
    ).takeIf { it > -1 }
    status = context.getInt(NEWLY_ADDED_STATUS_KEY, DEFAULT_STATUS).takeIf { it > -1 }
}


fun storeDiscoverAiringField(context: Context, field: AiringMediaField) {
    with(field) {
        context.putInt(DISCOVER_AIRING_SORT_KEY, sort ?: AiringSort.TIME.ordinal)
        context.putBoolean(DISCOVER_AIRING_NOT_AIRED_KEY, notYetAired)
        context.putBoolean(DISCOVER_AIRING_PLANNING_KEY, showFromPlanning)
        context.putBoolean(DISCOVER_AIRING_WATCHING_KEY, showFromWatching)
    }
}

fun storeAiringField(context: Context, field: AiringMediaField) {
    with(field) {
        context.putInt(AIRING_SORT_KEY, sort ?: AiringSort.TIME.ordinal)
        context.putBoolean(AIRING_NOT_AIRED_KEY, notYetAired)
        context.putBoolean(AIRING_PLANNING_KEY, showFromPlanning)
        context.putBoolean(AIRING_WATCHING_KEY, showFromWatching)
    }
}

fun storeNewlyAddedField(context: Context, field: NewlyAddedMediaField) {
    with(field) {
        context.putString(NEWLY_ADDED_FORMAT_IN_KEY, formatsIn?.joinToString(",") ?: "")
        context.putInt(NEWLY_ADDED_FORMAT_KEY, format ?: DEFAULT_FORMAT)
        context.putInt(NEWLY_ADDED_YEAR_KEY, seasonYear ?: -1)
        context.putInt(
            NEWLY_ADDED_SEASON_KEY,
            season ?: -1
        )
        context.putInt(NEWLY_ADDED_STATUS_KEY, status ?: DEFAULT_STATUS)
    }
}

fun storeSeasonField(context: Context, field: SeasonField) {
    with(field) {
        context.putString(SEASON_FORMAT_IN_KEY, formatsIn?.joinToString(",") ?: "")
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
        context.putString(TRENDING_FORMAT_IN_KEY, formatsIn?.joinToString(",") ?: "")
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
        context.putString(POPULAR_FORMAT_IN_KEY, formatsIn?.joinToString(",") ?: "")
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

fun getRecommendationSort(context: Context) = context.getInt(RECOMMENDATION_SORT_KEY, 0)
fun getRecommendationOnList(context: Context) =
    context.getBoolean(RECOMMENDATION_ON_LIST_KEY, false)

fun setRecommendationSort(context: Context, sort: Int) =
    context.putInt(RECOMMENDATION_SORT_KEY, sort)


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

fun isHomeOrderEnabled(context: Context, type: HomeOrderType): Boolean {
    return when (type) {
        HomeOrderType.AIRING -> context.getBoolean(AIRING_ORDER_ENABLED_KEY, true)
        HomeOrderType.TRENDING -> context.getBoolean(TRENDING_ORDER_ENABLED_KEY, true)
        HomeOrderType.POPULAR -> context.getBoolean(POPULAR_ORDER_ENABLED_KEY, true)
        HomeOrderType.NEWLY_ADDED -> context.getBoolean(NEWLY_ADDED_ORDER_ENABLED_KEY, true)
        HomeOrderType.WATCHING -> context.getBoolean(WATCHING_ORDER_ENABLED_KEY, true)
        HomeOrderType.READING -> context.getBoolean(READING_ORDER_ENABLED_KEY, true)
    }
}

fun setHomeOrderFromType(
    context: Context,
    type: HomeOrderType,
    order: Int,
    isEnabled: Boolean = false
) {
    when (type) {
        HomeOrderType.AIRING -> {
            context.putInt(AIRING_ORDER_KEY, order)
            context.putBoolean(AIRING_ORDER_ENABLED_KEY, isEnabled)
        }
        HomeOrderType.TRENDING -> {
            context.putInt(TRENDING_ORDER_KEY, order)
            context.putBoolean(TRENDING_ORDER_ENABLED_KEY, isEnabled)
        }
        HomeOrderType.POPULAR -> {
            context.putInt(POPULAR_ORDER_KEY, order)
            context.putBoolean(POPULAR_ORDER_ENABLED_KEY, isEnabled)
        }
        HomeOrderType.NEWLY_ADDED -> {
            context.putInt(NEWLY_ADDED_ORDER_KEY, order)
            context.putBoolean(NEWLY_ADDED_ORDER_ENABLED_KEY, isEnabled)
        }
        HomeOrderType.WATCHING -> {
            context.putInt(WATCHING_ORDER_KEY, order)
            context.putBoolean(WATCHING_ORDER_ENABLED_KEY, isEnabled)
        }
        HomeOrderType.READING -> {
            context.putInt(READING_ORDER_KEY, order)
            context.putBoolean(READING_ORDER_ENABLED_KEY, isEnabled)
        }
    }
}


fun setHomePageOrderFromType(context: Context, type: HomePageOrderType, order: Int) {
    when (type) {
        HomePageOrderType.HOME -> context.putInt(HOME_PAGE_ORDER_KEY, order)
        HomePageOrderType.LIST -> context.putInt(LIST_PAGE_ORDER_KEY, order)
        HomePageOrderType.RADIO -> context.putInt(RADIO_PAGE_ORDER_KEY, order)
    }
}

fun getHomePageOrderFromType(context: Context, type: HomePageOrderType): Int {
    return when (type) {
        HomePageOrderType.HOME -> context.getInt(HOME_PAGE_ORDER_KEY, 0)
        HomePageOrderType.LIST -> context.getInt(LIST_PAGE_ORDER_KEY, 1)
        HomePageOrderType.RADIO -> context.getInt(RADIO_PAGE_ORDER_KEY, 2)
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


fun getMediaListGridPresenter(): MediaListDisplayMode {
    return MediaListDisplayMode.values()[DynamicPreferences.getInstance()
        .load(MEDIA_LIST_GRID_PRESENTER_KEY, MediaListDisplayMode.NORMAL.ordinal)]
}

fun getAiringDisplayMode(): AiringListDisplayMode {
    return AiringListDisplayMode.values()[DynamicPreferences.getInstance()
        .load(AIRING_DISPLAY_MODE_KEY, AiringListDisplayMode.COMPACT.ordinal)]
}

fun setMediaListGridPresenter(which: Int) {
    return DynamicPreferences.getInstance().save(MEDIA_LIST_GRID_PRESENTER_KEY, which)
}

fun setAiringDisplayMode(which: Int) {
    return DynamicPreferences.getInstance().save(AIRING_DISPLAY_MODE_KEY, which)
}


fun storeMediaListFilterField(context: Context, filter: MediaListCollectionFilterField, type: Int) {
    val json = Gson().toJson(filter)
    context.putString(
        if (type == 0) ANIME_MEDIA_LIST_FILTER_KEY else MANGA_MEDIA_LIST_FILTER_KEY,
        json
    ) // 0 = anime media type
}

fun loadMediaListFilter(context: Context, type: Int): MediaListCollectionFilterField {
    val field = Gson().fromJson(
        context.getString(
            if (type == 0) ANIME_MEDIA_LIST_FILTER_KEY else MANGA_MEDIA_LIST_FILTER_KEY,
            ""
        ),
        MediaListCollectionFilterField::class.java
    ) ?: MediaListCollectionFilterField()

    return field
}

fun showAiringWeekly(context: Context, isChecked: Boolean? = null): Boolean =
    if (isChecked == null) context.getBoolean(SHOW_AIRING_WEEKLY_KEY, true) else {
        context.putBoolean(
            SHOW_AIRING_WEEKLY_KEY, isChecked
        )
        isChecked
    }


fun showSeasonHeader(context: Context, isChecked: Boolean? = null): Boolean =
    if (isChecked == null) context.getBoolean(SEASON_HEADER_KEY, false) else {
        context.putBoolean(
            SEASON_HEADER_KEY, isChecked
        )
        isChecked
    }


fun getAiringScheduleFieldForWidget(context: Context, field:AiringMediaField? = null): AiringMediaField {
    return  (field ?: AiringMediaField()).apply {
        notYetAired = context.getBoolean(WIDGET_AIRING_NOT_AIRED_KEY, true)
        sort = context.getInt(WIDGET_AIRING_SORT_KEY, AiringSort.TIME.ordinal)
        showFromPlanning = context.getBoolean(WIDGET_AIRING_PLANNING_KEY, false)
        showFromWatching = context.getBoolean(WIDGET_AIRING_WATCHING_KEY, false)
        isWeeklyTypeDate = context.getBoolean(WIDGET_IS_AIRING_WEEKLY_KEY, false)
    }
}


fun storeAiringScheduleFieldForWidget(context: Context, field: AiringMediaField) {
    with(field) {
        context.putInt(WIDGET_AIRING_SORT_KEY, sort ?: AiringSort.TIME.ordinal)
        context.putBoolean(WIDGET_AIRING_NOT_AIRED_KEY, notYetAired)
        context.putBoolean(WIDGET_AIRING_PLANNING_KEY, showFromPlanning)
        context.putBoolean(WIDGET_AIRING_WATCHING_KEY, showFromWatching)
        context.putBoolean(WIDGET_IS_AIRING_WEEKLY_KEY, isWeeklyTypeDate)
    }
}

