package com.revolgenx.anilib.preference

import android.content.Context
import com.revolgenx.anilib.field.SeasonField
import com.revolgenx.anilib.type.MediaFormat
import com.revolgenx.anilib.type.MediaSeason
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.type.MediaStatus
import com.revolgenx.anilib.util.getSeasonFromMonth
import org.threeten.bp.LocalDateTime


const val SEASON_FORMAT_KEY = "SEASON_FORMAT_KEY"
const val SEASON_YEAR_KEY = "SEASON_YEAR_KEY"
const val SEASON_KEY = "SEASON_KEY"
const val SEASON_STATUS_KEY = "SEASON_STATUS_KEY"
const val SEASON_GENRES_KEY = "SEASON_GENRES_KEY"
const val SEASON_ORDER_KEY = "SEASON_ORDER_KEY"
const val SEASON_SORT_KEY = "SEASON_sort_KEY"

fun getSeasonField(context: Context) = SeasonField(
    format = context.getInt(SEASON_FORMAT_KEY, MediaFormat.`$UNKNOWN`.ordinal),
    year = context.getInt(SEASON_YEAR_KEY, 2019),//LocalDateTime.now().year),
    season = context.getInt(
        SEASON_KEY,
        MediaSeason.WINTER.ordinal
    ),//getSeasonFromMonth(LocalDateTime.now().monthValue).ordinal -1 ), //todo remove -1
    status = context.getInt(SEASON_STATUS_KEY, MediaStatus.`$UNKNOWN`.ordinal),
    sort = context.getInt(SEASON_SORT_KEY, MediaSort.ID_DESC.ordinal),
    genres = context.sharedPreference().getStringSet(SEASON_GENRES_KEY, emptySet())!!.toList(),
    order = context.getInt(SEASON_ORDER_KEY, SeasonField.Companion.SeasonOrder.DESC.ordinal)
)