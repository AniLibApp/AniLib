package com.revolgenx.anilib.common.preference

import android.content.Context
import com.google.gson.Gson
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences
import com.revolgenx.anilib.airing.data.field.AiringMediaField
import com.revolgenx.anilib.constant.AiringListDisplayMode
import com.revolgenx.anilib.constant.MediaListDisplayMode
import com.revolgenx.anilib.app.setting.data.meta.DiscoverOrderType
import com.revolgenx.anilib.constant.AlActivityType
import com.revolgenx.anilib.home.data.meta.HomePageOrderType
import com.revolgenx.anilib.home.discover.data.field.NewlyAddedMediaField
import com.revolgenx.anilib.home.discover.data.field.PopularMediaField
import com.revolgenx.anilib.home.discover.data.field.TrendingMediaField
import com.revolgenx.anilib.home.season.data.field.SeasonField
import com.revolgenx.anilib.list.data.meta.MediaListCollectionFilterMeta
import com.revolgenx.anilib.social.data.field.ActivityUnionField
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

const val ANIME_MEDIA_LIST_COLLECTION_FILTER_KEY = "ANIME_MEDIA_LIST_COLLECTION_FILTER_KEY"
const val MANGA_MEDIA_LIST_COLLECTION_FILTER_KEY = "MANGA_MEDIA_LIST_COLLECTION_FILTER_KEY"


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

const val HOME_PAGE_ORDER_KEY = "HOME_PAGE_ORDER_1_KEY"
const val LIST_PAGE_ORDER_KEY = "LIST_PAGE_ORDER_1_KEY"
const val RADIO_PAGE_ORDER_KEY = "RADIO_PAGE_ORDER_1_KEY"
const val ACTIVITY_PAGE_ORDER_KEY = "ACTIVITY_PAGE_ORDER_1_KEY"

const val DISCOVER_READING_SORT_KEY = "DISCOVER_READING_SORT_KEY"
const val DISCOVER_WATCHING_SORT_KEY = "DISCOVER_WATCHING_SORT_KEY"


const val USER_ANIME_MEDIA_LIST_DISPLAY_KEY = "USER_ANIME_MEDIA_LIST_DISPLAY_KEY"
const val USER_MANGA_MEDIA_LIST_DISPLAY_KEY = "USER_MANGA_MEDIA_LIST_DISPLAY_KEY"

const val GENERAL_ANIME_MEDIA_LIST_DISPLAY_KEY = "GENERAL_ANIME_MEDIA_LIST_DISPLAY_KEY"
const val GENERAL_MANGA_MEDIA_LIST_DISPLAY_KEY = "GENERAL_MANGA_MEDIA_LIST_DISPLAY_KEY"


const val AIRING_DISPLAY_MODE_KEY = "AIRING_DISPLAY_MODE_KEY"

const val SHOW_AIRING_WEEKLY_KEY = "SHOW_AIRING_WEEKLY_KEY"


const val WIDGET_AIRING_NOT_AIRED_KEY = "WIDGET_AIRING_NOT_AIRED_KEY"
const val WIDGET_AIRING_SORT_KEY = "WIDGET_AIRING_SORT_KEY"
const val WIDGET_AIRING_PLANNING_KEY = "WIDGET_AIRING_PLANNING_KEY"
const val WIDGET_AIRING_WATCHING_KEY = "WIDGET_AIRING_WATCHING_KEY"
const val WIDGET_IS_AIRING_WEEKLY_KEY = "WIDGET_IS_AIRING_WEEKLY_KEY"
const val WIDGET_AIRING_CLICK_OPEN_LIST_EDITOR = "WIDGET_AIRING_CLICK_OPEN_MEDIA_INFO_KEY"
const val WIDGET_AIRING_SHOW_ETA_KEY = "WIDGET_AIRING_SHOW_ETA_KEY"

//activity union
const val ACTIVITY_UNION_TYPE_KEY = "ACTIVITY_UNION_TYPE_KEY"
const val ACTIVITY_IS_FOLLOWING_KEY = "ACTIVITY_IS_FOLLOWING_KEY"


fun getDiscoverAiringField(context: Context) = AiringMediaField().apply {
    notYetAired = load(DISCOVER_AIRING_NOT_AIRED_KEY, true)
    sort = load(DISCOVER_AIRING_SORT_KEY, AiringSort.TIME.ordinal)
    showFromPlanning = load(DISCOVER_AIRING_PLANNING_KEY, false)
    showFromWatching = load(DISCOVER_AIRING_WATCHING_KEY, false)
}


fun getAiringField(context: Context) = AiringMediaField().apply {
    notYetAired = load(AIRING_NOT_AIRED_KEY, true)
    sort = load(AIRING_SORT_KEY, AiringSort.TIME.ordinal)
    showFromPlanning = load(AIRING_PLANNING_KEY, false)
    showFromWatching = load(AIRING_WATCHING_KEY, false)
    isWeeklyTypeDate = showAiringWeekly(context)
}


fun getSeasonField() = SeasonField().apply {
    formatsIn = load(SEASON_FORMAT_IN_KEY, "")?.takeIf { it.isNotEmpty() }?.split(",")
        ?.map { it.toInt() }?.toMutableList()
    seasonYear = load(SEASON_YEAR_KEY, LocalDateTime.now().year)
    season = load(
        SEASON_KEY,
        getSeasonFromMonth(LocalDateTime.now().monthValue).ordinal
    ).takeIf { it > -1 }
    status = load(SEASON_STATUS_KEY, DEFAULT_STATUS).takeIf { it > -1 }
    sort = load(SEASON_SORT_KEY, DEFAULT_SORT).takeIf { it > -1 }
    tags = loadStringSet(SEASON_TAGS_KEY, emptySet())!!.toList()
    genres = loadStringSet(SEASON_GENRES_KEY, emptySet())!!.toList()
    showFormatHeader = load(SEASON_HEADER_KEY, false)
}

fun getTrendingField() = TrendingMediaField().apply {
    formatsIn =
        load(TRENDING_FORMAT_IN_KEY, "")?.takeIf { it.isNotEmpty() }?.split(",")
            ?.map { it.toInt() }?.toMutableList()
    year = load(TRENDING_YEAR_KEY, -1).takeIf { it > -1 }
    season = load(
        TRENDING_SEASON_KEY,
        -1
    ).takeIf { it > -1 }
    status = load(TRENDING_STATUS_KEY, DEFAULT_STATUS).takeIf { it > -1 }
}

fun getPopularField() = PopularMediaField().apply {
    formatsIn = load(POPULAR_FORMAT_IN_KEY, "")?.takeIf { it.isNotEmpty() }?.split(",")
        ?.map { it.toInt() }?.toMutableList()
    year = load(POPULAR_YEAR_KEY, -1).takeIf { it > -1 }
    season = load(
        POPULAR_SEASON_KEY,
        -1
    ).takeIf { it > -1 }
    status = load(POPULAR_STATUS_KEY, DEFAULT_STATUS).takeIf { it > -1 }
}


fun getNewlyAddedField() = NewlyAddedMediaField().apply {
    formatsIn =
        load(NEWLY_ADDED_FORMAT_IN_KEY, "")?.takeIf { it.isNotEmpty() }?.split(",")
            ?.map { it.toInt() }?.toMutableList()
    year = load(NEWLY_ADDED_YEAR_KEY, -1).takeIf { it > -1 }
    season = load(
        NEWLY_ADDED_SEASON_KEY,
        -1
    ).takeIf { it > -1 }
    status = load(NEWLY_ADDED_STATUS_KEY, DEFAULT_STATUS).takeIf { it > -1 }
}


fun storeDiscoverAiringField(context: Context, field: AiringMediaField) {
    with(field) {
        save(DISCOVER_AIRING_SORT_KEY, sort ?: AiringSort.TIME.ordinal)
        save(DISCOVER_AIRING_NOT_AIRED_KEY, notYetAired)
        save(DISCOVER_AIRING_PLANNING_KEY, showFromPlanning)
        save(DISCOVER_AIRING_WATCHING_KEY, showFromWatching)
    }
}

fun storeAiringField(context: Context, field: AiringMediaField) {
    with(field) {
        save(AIRING_SORT_KEY, sort ?: AiringSort.TIME.ordinal)
        save(AIRING_NOT_AIRED_KEY, notYetAired)
        save(AIRING_PLANNING_KEY, showFromPlanning)
        save(AIRING_WATCHING_KEY, showFromWatching)
    }
}

fun storeNewlyAddedField(context: Context, field: NewlyAddedMediaField) {
    with(field) {
        save(NEWLY_ADDED_FORMAT_IN_KEY, formatsIn?.joinToString(",") ?: "")
        save(NEWLY_ADDED_FORMAT_KEY, format ?: DEFAULT_FORMAT)
        save(NEWLY_ADDED_YEAR_KEY, year ?: -1)
        save(
            NEWLY_ADDED_SEASON_KEY,
            season ?: -1
        )
        save(NEWLY_ADDED_STATUS_KEY, status ?: DEFAULT_STATUS)
    }
}

fun storeSeasonField(field: SeasonField) {
    with(field) {
        save(SEASON_FORMAT_IN_KEY, formatsIn?.joinToString(",") ?: "")
        save(SEASON_FORMAT_KEY, format ?: DEFAULT_FORMAT)
        save(SEASON_YEAR_KEY, seasonYear ?: LocalDateTime.now().year)
        save(
            SEASON_KEY,
            season ?: getSeasonFromMonth(LocalDateTime.now().monthValue).ordinal
        )
        save(SEASON_STATUS_KEY, status ?: DEFAULT_STATUS)
        save(SEASON_SORT_KEY, sort ?: DEFAULT_SORT)
    }
}


fun storeTrendingField(context: Context, field: TrendingMediaField) {
    with(field) {
        save(TRENDING_FORMAT_IN_KEY, formatsIn?.joinToString(",") ?: "")
        save(TRENDING_FORMAT_KEY, format ?: DEFAULT_FORMAT)
        save(TRENDING_YEAR_KEY, year ?: -1)
        save(
            TRENDING_SEASON_KEY,
            season ?: -1
        )
        save(TRENDING_STATUS_KEY, status ?: DEFAULT_STATUS)
    }
}

fun storePopularField(context: Context, field: PopularMediaField) {
    with(field) {
        save(POPULAR_FORMAT_IN_KEY, formatsIn?.joinToString(",") ?: "")
        save(POPULAR_FORMAT_KEY, format ?: DEFAULT_FORMAT)
        save(POPULAR_YEAR_KEY, year ?: -1)
        save(
            POPULAR_SEASON_KEY,
            season ?: -1
        )
        save(POPULAR_STATUS_KEY, status ?: DEFAULT_STATUS)
    }
}


fun storeSeasonGenre(field: SeasonField?) {
    saveStringSet(SEASON_GENRES_KEY, field?.genres?.toSet() ?: emptySet())
}

fun storeSeasonTag(field: SeasonField?) {
    saveStringSet(SEASON_TAGS_KEY, field?.tags?.toSet() ?: emptySet())
}

fun getRecommendationSort() = load(RECOMMENDATION_SORT_KEY, 0)
fun getRecommendationOnList() =
    load(RECOMMENDATION_ON_LIST_KEY, false)

fun setRecommendationSort(sort: Int) =
    save(RECOMMENDATION_SORT_KEY, sort)

fun getDiscoverOrderFromType(type: DiscoverOrderType): Int {
    return when (type) {
        DiscoverOrderType.AIRING -> load(AIRING_ORDER_KEY, 0)
        DiscoverOrderType.TRENDING -> load(TRENDING_ORDER_KEY, 1)
        DiscoverOrderType.POPULAR -> load(POPULAR_ORDER_KEY, 2)
        DiscoverOrderType.NEWLY_ADDED -> load(NEWLY_ADDED_ORDER_KEY, 3)
        DiscoverOrderType.WATCHING -> load(WATCHING_ORDER_KEY, 4)
        DiscoverOrderType.READING -> load(READING_ORDER_KEY, 5)
    }
}

fun isDiscoverOrderEnabled(type: DiscoverOrderType): Boolean {
    return when (type) {
        DiscoverOrderType.AIRING -> load(AIRING_ORDER_ENABLED_KEY, true)
        DiscoverOrderType.TRENDING -> load(TRENDING_ORDER_ENABLED_KEY, true)
        DiscoverOrderType.POPULAR -> load(POPULAR_ORDER_ENABLED_KEY, true)
        DiscoverOrderType.NEWLY_ADDED -> load(NEWLY_ADDED_ORDER_ENABLED_KEY, true)
        DiscoverOrderType.WATCHING -> load(WATCHING_ORDER_ENABLED_KEY, true)
        DiscoverOrderType.READING -> load(READING_ORDER_ENABLED_KEY, true)
    }
}

fun setHomeOrderFromType(
    context: Context,
    type: DiscoverOrderType,
    order: Int,
    isEnabled: Boolean = false
) {
    when (type) {
        DiscoverOrderType.AIRING -> {
            save(AIRING_ORDER_KEY, order)
            save(AIRING_ORDER_ENABLED_KEY, isEnabled)
        }
        DiscoverOrderType.TRENDING -> {
            save(TRENDING_ORDER_KEY, order)
            save(TRENDING_ORDER_ENABLED_KEY, isEnabled)
        }
        DiscoverOrderType.POPULAR -> {
            save(POPULAR_ORDER_KEY, order)
            save(POPULAR_ORDER_ENABLED_KEY, isEnabled)
        }
        DiscoverOrderType.NEWLY_ADDED -> {
            save(NEWLY_ADDED_ORDER_KEY, order)
            save(NEWLY_ADDED_ORDER_ENABLED_KEY, isEnabled)
        }
        DiscoverOrderType.WATCHING -> {
            save(WATCHING_ORDER_KEY, order)
            save(WATCHING_ORDER_ENABLED_KEY, isEnabled)
        }
        DiscoverOrderType.READING -> {
            save(READING_ORDER_KEY, order)
            save(READING_ORDER_ENABLED_KEY, isEnabled)
        }
    }
}


fun setHomePageOrderFromType(type: HomePageOrderType, order: Int) {
    when (type) {
        HomePageOrderType.HOME -> save(HOME_PAGE_ORDER_KEY, order)
        HomePageOrderType.LIST -> save(LIST_PAGE_ORDER_KEY, order)
        HomePageOrderType.RADIO -> save(RADIO_PAGE_ORDER_KEY, order)
        HomePageOrderType.ACTIVITY -> save(ACTIVITY_PAGE_ORDER_KEY, order)
    }
}

fun getHomePageOrderFromType(type: HomePageOrderType): Int {
    return when (type) {
        HomePageOrderType.HOME -> load(HOME_PAGE_ORDER_KEY, 0)
        HomePageOrderType.LIST -> load(LIST_PAGE_ORDER_KEY, 1)
        HomePageOrderType.ACTIVITY -> load(ACTIVITY_PAGE_ORDER_KEY, 2)
        HomePageOrderType.RADIO -> load(RADIO_PAGE_ORDER_KEY, 3)
    }
}

fun setDiscoverMediaListSort(type: Int, sort: Int?) {
    when (type) {
        MediaType.ANIME.ordinal -> {
            save(DISCOVER_WATCHING_SORT_KEY, sort ?: -1)
        }
        MediaType.MANGA.ordinal -> {
            save(DISCOVER_READING_SORT_KEY, sort ?: -1)
        }
    }
}


fun getDiscoverMediaListSort(type: Int): Int? {
    return when (type) {
        MediaType.ANIME.ordinal -> {
            load(DISCOVER_WATCHING_SORT_KEY, -1).takeIf { it > -1 }
        }
        MediaType.MANGA.ordinal -> {
            load(DISCOVER_READING_SORT_KEY, -1).takeIf { it > -1 }
        }
        else -> null
    }
}


fun getUserMediaListCollectionDisplayMode(mediaType: MediaType): MediaListDisplayMode {
    return MediaListDisplayMode.values()[dynamicPreferences
        .load(
            if (mediaType == MediaType.ANIME) USER_ANIME_MEDIA_LIST_DISPLAY_KEY else USER_MANGA_MEDIA_LIST_DISPLAY_KEY,
            MediaListDisplayMode.NORMAL.ordinal
        )]
}

fun getGeneralMediaListCollectionDisplayMode(mediaType: MediaType): MediaListDisplayMode {
    return MediaListDisplayMode.values()[dynamicPreferences
        .load(
            if (mediaType == MediaType.ANIME) GENERAL_ANIME_MEDIA_LIST_DISPLAY_KEY else GENERAL_MANGA_MEDIA_LIST_DISPLAY_KEY,
            MediaListDisplayMode.NORMAL.ordinal
        )]
}

fun setUserMediaListCollectionDisplayMode(mediaType: MediaType, mode: Int) {
    dynamicPreferences
        .save(
            if (mediaType == MediaType.ANIME) USER_ANIME_MEDIA_LIST_DISPLAY_KEY else USER_MANGA_MEDIA_LIST_DISPLAY_KEY,
            mode
        )
}

fun setGeneralMediaListCollectionDisplayMode(mediaType: MediaType, mode: Int) {
    dynamicPreferences
        .save(
            if (mediaType == MediaType.ANIME) GENERAL_ANIME_MEDIA_LIST_DISPLAY_KEY else GENERAL_MANGA_MEDIA_LIST_DISPLAY_KEY,
            mode
        )
}

fun getAiringDisplayMode(): AiringListDisplayMode {
    return AiringListDisplayMode.values()[dynamicPreferences
        .load(AIRING_DISPLAY_MODE_KEY, AiringListDisplayMode.COMPACT.ordinal)]
}

fun setAiringDisplayMode(which: Int) {
    return dynamicPreferences.save(AIRING_DISPLAY_MODE_KEY, which)
}

fun storeMediaListFilterField(filter: MediaListCollectionFilterMeta) {
    val json = Gson().toJson(filter)
    dynamicPreferences.save(
        if (filter.type == 0) ANIME_MEDIA_LIST_COLLECTION_FILTER_KEY else MANGA_MEDIA_LIST_COLLECTION_FILTER_KEY,
        json
    ) // 0 = anime media type
}

fun loadMediaListCollectionFilter(type: Int): MediaListCollectionFilterMeta {
    return Gson().fromJson(
        dynamicPreferences.load(
            if (type == 0) ANIME_MEDIA_LIST_COLLECTION_FILTER_KEY else MANGA_MEDIA_LIST_COLLECTION_FILTER_KEY,
            ""
        ),
        MediaListCollectionFilterMeta::class.java
    ) ?: MediaListCollectionFilterMeta()
}


fun showAiringWeekly(context: Context, isChecked: Boolean? = null): Boolean =
    if (isChecked == null) load(SHOW_AIRING_WEEKLY_KEY, true) else {
        save(
            SHOW_AIRING_WEEKLY_KEY, isChecked
        )
        isChecked
    }


fun showSeasonHeader(context: Context, isChecked: Boolean? = null): Boolean =
    if (isChecked == null) load(SEASON_HEADER_KEY, false) else {
        save(
            SEASON_HEADER_KEY, isChecked
        )
        isChecked
    }


fun getAiringScheduleFieldForWidget(
    context: Context,
    field: AiringMediaField? = null
): AiringMediaField {
    return (field ?: AiringMediaField()).apply {
        notYetAired = load(WIDGET_AIRING_NOT_AIRED_KEY, true)
        sort = load(WIDGET_AIRING_SORT_KEY, AiringSort.TIME.ordinal)
        showFromPlanning = load(WIDGET_AIRING_PLANNING_KEY, false)
        showFromWatching = load(WIDGET_AIRING_WATCHING_KEY, false)
        isWeeklyTypeDate = load(WIDGET_IS_AIRING_WEEKLY_KEY, false)
    }
}


fun storeAiringScheduleFieldForWidget(context: Context, field: AiringMediaField) {
    with(field) {
        save(WIDGET_AIRING_SORT_KEY, sort ?: AiringSort.TIME.ordinal)
        save(WIDGET_AIRING_NOT_AIRED_KEY, notYetAired)
        save(WIDGET_AIRING_PLANNING_KEY, showFromPlanning)
        save(WIDGET_AIRING_WATCHING_KEY, showFromWatching)
        save(WIDGET_IS_AIRING_WEEKLY_KEY, isWeeklyTypeDate)
    }
}

fun getActivityUnionField(field: ActivityUnionField? = null): ActivityUnionField {
    return (field ?: ActivityUnionField()).apply {
        dynamicPreferences.let {
            type = it.load(ACTIVITY_UNION_TYPE_KEY, AlActivityType.ALL.ordinal)
                .let { AlActivityType.values()[it] }
            isFollowing = it.load(ACTIVITY_IS_FOLLOWING_KEY, false)
        }
    }
}


fun storeActivityUnionField(field: ActivityUnionField) {
    with(field) {
        dynamicPreferences.let {
            it.save(ACTIVITY_UNION_TYPE_KEY, type.ordinal)
            it.save(ACTIVITY_IS_FOLLOWING_KEY, isFollowing)
        }
    }
}