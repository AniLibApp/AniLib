package com.revolgenx.anilib.data.field.home

import android.content.Context
import com.revolgenx.anilib.MediaQuery
import com.revolgenx.anilib.data.field.TagField
import com.revolgenx.anilib.data.field.media.MediaField
import com.revolgenx.anilib.common.preference.getSeasonField
import com.revolgenx.anilib.common.preference.storeSeasonField
import com.revolgenx.anilib.common.preference.storeSeasonGenre
import com.revolgenx.anilib.common.preference.storeSeasonTag
import com.revolgenx.anilib.type.MediaSort


class SeasonField : MediaField() {
    companion object {
        fun create(context: Context) = getSeasonField(context)
    }

    var tagTagFields = mutableMapOf<String, TagField>()
    var genreTagFields = mutableMapOf<String, TagField>()

    var showFormatHeader:Boolean = true

    fun saveSeasonField(context: Context) {
        storeSeasonField(context, this)
    }

    fun saveGenre(context: Context) {
        storeSeasonGenre(context, this)
    }

    fun saveTags(context: Context) {
        storeSeasonTag(context, this)
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

    fun updateFields(context: Context) {
        getSeasonField(context).also {
            this.season = it.season
            this.seasonYear = it.seasonYear
            this.tags = it.tags
            this.genres = it.genres
            this.format = it.format
            this.formatsIn = it.formatsIn
            this.sort = it.sort
            this.status = it.status
            this.showFormatHeader = it.showFormatHeader
        }
    }


    override fun toQueryOrMutation(): MediaQuery {
        return MediaQuery.builder()
            .builder()
            .apply {
                if (showFormatHeader) {
                    if (sort != null) {
                        sort(listOf(MediaSort.FORMAT, MediaSort.values()[sort!!]))
                    } else {
                        sort(listOf(MediaSort.FORMAT))
                    }
                } else {
                    sort?.let {
                        sort(listOf(MediaSort.values()[it]))
                    }
                }
            }
            .build()
    }


}
