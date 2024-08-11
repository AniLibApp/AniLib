package com.revolgenx.anilib.list.data.filter

import com.revolgenx.anilib.list.data.sort.MediaListSortType
import com.revolgenx.anilib.type.MediaFormat
import com.revolgenx.anilib.type.MediaStatus
import kotlinx.serialization.Serializable

@Serializable
data class MediaListCollectionFilter(
    val formatsIn: List<MediaFormat>? = null,
    val status: MediaStatus? = null,
    val genre: String? = null,
    val sort: MediaListSortType? = null,
    val year: Int? = null,
    val groupName: String = "All",
    val isHentai: Boolean? = null
)