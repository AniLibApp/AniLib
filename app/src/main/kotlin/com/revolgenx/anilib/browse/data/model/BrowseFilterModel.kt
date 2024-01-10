package com.revolgenx.anilib.browse.data.model

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
data class BrowseFilterModel(
    val search: String? = null,
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
    val isHentai: Boolean? = false,
    val streamingOn: List<Int>? = null,
    val readableOn: List<Int>? = null,
    val countryOfOrigin: Int? = null,
    val source: MediaSource? = null,
    val doujins: Boolean? = null,
    val format: MediaFormat? = null,
    val genreIn: List<String>? = null,
    val genreNotIn: List<String>? = null,
    val tagsIn: List<String>? = null,
    val tagsNotIn: List<String>? = null,
    val sort: MediaSort? = null,
    val minimumTagRank: Int? = null
) {
    fun toBrowseField(): BrowseField {
        return BrowseField(
            search = search,
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
            streamingOn = streamingOn?.toMutableList(),
            readableOn = readableOn?.toMutableList(),
            countryOfOrigin = countryOfOrigin,
            source = source,
            doujins = doujins,
            format = format,
            genreIn = genreIn?.toMutableList(),
            genreNotIn = genreNotIn?.toMutableList(),
            tagsIn = tagsIn?.toMutableList(),
            tagsNotIn = tagsNotIn?.toMutableList(),
            sort = sort,
            minimumTagRank = minimumTagRank,
        )
    }
}