package com.revolgenx.anilib.ui.sorting

import com.revolgenx.anilib.common.data.model.toDate
import com.revolgenx.anilib.data.model.list.AlMediaListModel
import com.revolgenx.anilib.data.sorting.BaseSorting
import com.revolgenx.anilib.util.getOrDefault

class MediaListSorting() :
    BaseSorting.SortingColumnsInterface<AlMediaListModel, MediaListSorting.MediaListSortingType> {

    override fun compare(
        item1: AlMediaListModel,
        item2: AlMediaListModel,
        type: MediaListSortingType
    ): Int {
        return when (type) {
            MediaListSortingType.TITLE -> {
                item1.title?.userPreferred.getOrDefault().compareTo(item2.title?.userPreferred.getOrDefault())
            }
            MediaListSortingType.TITLE_DESC -> {
                item2.title?.userPreferred.getOrDefault().compareTo(item1.title?.userPreferred.getOrDefault())
            }
            MediaListSortingType.SCORE -> {
                (item1.score.getOrDefault()).compareTo(item2.score.getOrDefault())
            }
            MediaListSortingType.SCORE_DESC -> {
                (item2.score.getOrDefault()).compareTo(item1.score.getOrDefault())
            }
            MediaListSortingType.PROGRESS -> {
                (item1.progress.getOrDefault()).compareTo(item2.progress.getOrDefault())
            }
            MediaListSortingType.PROGRESS_DESC -> {
                (item2.progress.getOrDefault()).compareTo(item1.progress.getOrDefault())
            }
            MediaListSortingType.AVERAGE_SCORE -> {
                (item1.averageScore.getOrDefault()).compareTo(item2.averageScore.getOrDefault())
            }
            MediaListSortingType.AVERAGE_SCORE_DESC -> {
                (item2.averageScore.getOrDefault()).compareTo(item1.averageScore.getOrDefault())
            }
            MediaListSortingType.POPULARITY -> {
                (item1.popularity.getOrDefault()).compareTo(item2.popularity.getOrDefault())
            }
            MediaListSortingType.POPULARITY_DESC -> {
                (item2.popularity.getOrDefault()).compareTo(item1.popularity.getOrDefault())
            }
            MediaListSortingType.UPDATED_AT -> {
                (item1.listUpdatedDate.getOrDefault()).compareTo(item2.listUpdatedDate.getOrDefault())
            }
            MediaListSortingType.UPDATED_AT_DESC -> {
                (item2.listUpdatedDate.getOrDefault()).compareTo(item1.listUpdatedDate.getOrDefault())
            }
            MediaListSortingType.ADDED -> {
                (item1.listCreatedDate.getOrDefault()).compareTo(item2.listCreatedDate.getOrDefault())
            }
            MediaListSortingType.ADDED_DESC -> {
                (item2.listCreatedDate.getOrDefault()).compareTo(item1.listCreatedDate.getOrDefault())
            }
            MediaListSortingType.STARTED -> {
                item1.listStartDate.toDate().compareTo(item2.listStartDate.toDate())
            }
            MediaListSortingType.STARTED_DESC -> {
                item2.listStartDate.toDate().compareTo(item1.listStartDate.toDate())
            }
            MediaListSortingType.COMPLETED -> {
                (item1.listCompletedDate.toDate()).compareTo(item2.listCompletedDate.toDate())
            }
            MediaListSortingType.COMPLETED_DESC -> {
                (item2.listCompletedDate.toDate()).compareTo(item1.listCompletedDate.toDate())
            }
            MediaListSortingType.RELEASE -> {
                item1.startDate.toDate().compareTo(item2.startDate.toDate())
            }
            MediaListSortingType.RELEASE_DESC -> {
                item2.startDate.toDate().compareTo(item1.startDate.toDate())
            }
        }
    }

    enum class MediaListSortingType() : BaseSorting.SortingType {
        TITLE,
        TITLE_DESC,
        SCORE,
        SCORE_DESC,
        PROGRESS,
        PROGRESS_DESC,
        UPDATED_AT,
        UPDATED_AT_DESC,
        ADDED,
        ADDED_DESC,
        STARTED,
        STARTED_DESC,
        COMPLETED,
        COMPLETED_DESC,
        RELEASE,
        RELEASE_DESC,
        AVERAGE_SCORE,
        AVERAGE_SCORE_DESC,
        POPULARITY,
        POPULARITY_DESC
    }
}