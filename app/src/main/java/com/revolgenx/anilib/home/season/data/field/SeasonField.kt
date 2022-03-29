package com.revolgenx.anilib.home.season.data.field

import com.revolgenx.anilib.MediaQuery
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.type.MediaFormat
import com.revolgenx.anilib.type.MediaSeason
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.type.MediaStatus


class SeasonField : MediaField() {
    companion object {
        fun create() = getSeasonField()
    }

    var showFormatHeader:Boolean = true

    fun saveSeasonField() {
        storeSeasonField(this)
    }

    fun saveGenre() {
        storeSeasonGenre(this)
    }

    fun saveTags() {
        storeSeasonTag(this)
    }

    fun nextSeason() {
        if (season == null) return
        season = season!! + 1
        if (season!! > 3) {
            seasonYear = seasonYear!! + 1
            season = 0
        }
        saveSeasonField()
    }

    fun previousSeason() {
        if (season == null) return
        season = season!! - 1
        if (season!! < 0) {
            seasonYear = seasonYear!! - 1
            season = 3
        }
        saveSeasonField()
    }

    fun updateFields() {
        getSeasonField().also {
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
        val genreExcludedList = mutableListOf<String>()
        val tagExcludedList = mutableListOf<String>()

        genreNotIn?.let {
            genreExcludedList.addAll(it)
        }

        tagNotIn?.let {
            tagExcludedList.addAll(it)
        }

        if (includeExcludedGenre) {
            genreExcludedList.addAll(getExcludedGenre())
        }

        if (includeExcludedTags) {
            tagExcludedList.addAll(getExcludedTags())
        }

        val mediaSeason = season?.let {
            MediaSeason.values()[it]
        }

        val mediaFormatsIn = formatsIn?.let {
            val formats = MediaFormat.values()
            it.map { formats[it] }
        }

        val mediaFormat = format?.let {
            MediaFormat.values()[it]
        }
        val mediaStatus = status?.let {
            MediaStatus.values()[it]
        }

        val mediaSort = if (showFormatHeader) {
            if (sort != null) {
                listOf(MediaSort.FORMAT, MediaSort.values()[sort!!])
            } else {
                listOf(MediaSort.FORMAT)
            }
        } else {
            sort?.let {
                listOf(MediaSort.values()[it])
            }
        }

        return MediaQuery(
            page = nn(page),
            perPage = nn(perPage),
            season = nn(mediaSeason),
            seasonYear = nn(seasonYear),
            sort = nn(mediaSort),
            format_in = nn(mediaFormatsIn),
            tag_in = nn(tags),
            idIn = nn(mediaIdsIn),
            isAdult = nn(canShowAdult.takeIf { it.not() }),
            format = nn(mediaFormat),
            status =  nn(mediaStatus),
            includeStaff = includeStaff,
            includeStudio = includeStudio,
            genre_in = nn(genres),
            genre_not_in = nn(genreExcludedList),
            tag_not_in = nn(tagExcludedList)
        )
    }


}
