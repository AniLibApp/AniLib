package com.revolgenx.anilib.ui.sorting

import com.revolgenx.anilib.data.model.list.AlMediaListModel

class MediaListSortingComparator(
    private val sorting: MediaListSorting,
    private val sortingType: MediaListSorting.MediaListSortingType
) : Comparator<AlMediaListModel> {

    override fun compare(o1: AlMediaListModel?, o2: AlMediaListModel?): Int {
        return sorting.compare(o1!!, o2!!, sortingType)
    }
}

fun makeMediaListSortingComparator(type: MediaListSorting.MediaListSortingType): MediaListSortingComparator {
    return MediaListSortingComparator(MediaListSorting(), type)
}