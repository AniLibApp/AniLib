package com.revolgenx.anilib.ui.sorting

import com.revolgenx.anilib.model.list.MediaListModel

class MediaListSortingComparator(
    private val sorting: MediaListSorting,
    private val sortingType: MediaListSorting.MediaListSortingType
) : Comparator<MediaListModel> {

    override fun compare(o1: MediaListModel?, o2: MediaListModel?): Int {
        return sorting.compare(o1!!, o2!!, sortingType)
    }
}

fun makeMediaListSortingComparator(type: MediaListSorting.MediaListSortingType): MediaListSortingComparator {
    return MediaListSortingComparator(MediaListSorting(), type)
}