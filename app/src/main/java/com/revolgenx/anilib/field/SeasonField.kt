package com.revolgenx.anilib.field

import android.content.Context
import com.revolgenx.anilib.SeasonListQuery
import com.revolgenx.anilib.field.media.MediaField
import com.revolgenx.anilib.preference.getSeasonField
import com.revolgenx.anilib.preference.storeSeasonField
import com.revolgenx.anilib.type.MediaFormat
import com.revolgenx.anilib.type.MediaSeason
import com.revolgenx.anilib.type.MediaStatus


class SeasonField : MediaField() {
    companion object {
        fun create(context: Context) = getSeasonField(context)
    }

    fun saveSeasonField(context: Context) {
        storeSeasonField(context, this)
    }

    fun nextSeason(context: Context) {
        if (season == null) return
        season = season!! + 1
        if (season!! > 3) {
            seasonYear = seasonYear!! + 1
            season = 0
        }
        saveSeasonField(context)
    }

    fun previousSeason(context: Context) {
        if (season == null) return
        season = season!! - 1
        if (season!! < 0) {
            seasonYear = seasonYear!! - 1
            season = 3
        }
        saveSeasonField(context)
    }

}