package com.revolgenx.anilib.browse.data.store

import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.browse.data.field.BrowseField
import com.revolgenx.anilib.browse.data.field.BrowseTypes
import com.revolgenx.anilib.browse.ui.model.FuzzyDateIntModel
import com.revolgenx.anilib.type.MediaFormat
import com.revolgenx.anilib.type.MediaSeason
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.type.MediaSource
import com.revolgenx.anilib.type.MediaStatus
import kotlinx.serialization.Serializable

@Serializable
data class BrowseFilterData(
    val browseType: BrowseTypes = BrowseTypes.ANIME,
    val season: MediaSeason? = null,
    val yearGreater: FuzzyDateIntModel? = null,
    val yearLesser: FuzzyDateIntModel? = null,
    val year: Int? = null,
    val status: MediaStatus? = null,
    val episodesGreater: Int? = null,
    val episodesLesser: Int? = null,
    val chaptersGreater: Int? = null,
    val chaptersLesser: Int? = null,
    val volumesGreater: Int? = null,
    val volumesLesser: Int? = null,
    val durationGreater: Int? = null,
    val durationLesser: Int? = null,
    val isHentai: Boolean? = null,
    val streamingOn: List<Int>? = null,
    val readableOn: List<Int>? = null,
    val countryOfOrigin: Int? = null,
    val source: MediaSource? = null,
    val doujins: Boolean? = null,
    val format: MediaFormat? = null,
    var formatsIn: List<MediaFormat>? = null,
    val genreIn: List<String>? = null,
    val genreNotIn: List<String>? = null,
    val tagsIn: List<String>? = null,
    val tagsNotIn: List<String>? = null,
    val sort: MediaSort? = null,
    val minimumTagRank: Int? = null,
    val onList: Boolean? = null,
) {
    fun toBrowseField(): BrowseField {
        return BrowseField(
            browseType = mutableStateOf(browseType),
            season = season,
            yearGreater = yearGreater,
            yearLesser = yearLesser,
            year = year,
            status = status,
            episodesGreater = episodesGreater,
            episodesLesser = episodesLesser,
            chaptersGreater = chaptersGreater,
            chaptersLesser = chaptersLesser,
            volumesGreater = volumesGreater,
            volumesLesser = volumesLesser,
            durationGreater = durationGreater,
            durationLesser = durationLesser,
            isHentai = isHentai,
            streamingOn = streamingOn,
            readableOn = readableOn,
            countryOfOrigin = countryOfOrigin,
            source = source,
            doujins = doujins,
            format = format,
            formatsIn = formatsIn,
            genreIn = genreIn,
            genreNotIn = genreNotIn,
            tagsIn = tagsIn,
            tagsNotIn = tagsNotIn,
            sort = sort,
            minimumTagRank = minimumTagRank,
            onList = onList
        )
    }
}