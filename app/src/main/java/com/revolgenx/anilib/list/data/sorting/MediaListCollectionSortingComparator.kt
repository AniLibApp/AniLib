package com.revolgenx.anilib.list.data.sorting

import com.revolgenx.anilib.common.data.model.toDate
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.util.getOrDefault

class MediaListCollectionSortingComparator : Comparator<MediaListModel> {
    var type: MediaListSortingType = MediaListSortingType.TITLE
    override fun compare(item1: MediaListModel, item2: MediaListModel): Int {
        val media1 = item1.media!!
        val media2 = item2.media!!
        return when (type) {
            MediaListSortingType.TITLE -> {
                media1.title?.userPreferred.getOrDefault()
                    .compareTo(media2.title?.userPreferred.getOrDefault())
            }
            MediaListSortingType.TITLE_DESC -> {
                media2.title?.userPreferred.getOrDefault()
                    .compareTo(media1.title?.userPreferred.getOrDefault())
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
                (media1.averageScore.getOrDefault()).compareTo(media2.averageScore.getOrDefault())
            }
            MediaListSortingType.AVERAGE_SCORE_DESC -> {
                (media2.averageScore.getOrDefault()).compareTo(media1.averageScore.getOrDefault())
            }
            MediaListSortingType.POPULARITY -> {
                (media1.popularity.getOrDefault()).compareTo(media2.popularity.getOrDefault())
            }
            MediaListSortingType.POPULARITY_DESC -> {
                (media2.popularity.getOrDefault()).compareTo(media1.popularity.getOrDefault())
            }
            MediaListSortingType.UPDATED_AT -> {
                (item1.updatedAt.getOrDefault()).compareTo(item2.updatedAt.getOrDefault())
            }
            MediaListSortingType.UPDATED_AT_DESC -> {
                (item2.updatedAt.getOrDefault()).compareTo(item1.updatedAt.getOrDefault())
            }
            MediaListSortingType.ADDED -> {
                (item1.createdAt.getOrDefault()).compareTo(item2.createdAt.getOrDefault())
            }
            MediaListSortingType.ADDED_DESC -> {
                (item2.createdAt.getOrDefault()).compareTo(item1.createdAt.getOrDefault())
            }
            MediaListSortingType.STARTED -> {
                item1.startedAt.toDate().compareTo(item2.startedAt.toDate())
            }
            MediaListSortingType.STARTED_DESC -> {
                item2.startedAt.toDate().compareTo(item1.startedAt.toDate())
            }
            MediaListSortingType.COMPLETED -> {
                (item1.completedAt.toDate()).compareTo(item2.completedAt.toDate())
            }
            MediaListSortingType.COMPLETED_DESC -> {
                (item2.completedAt.toDate()).compareTo(item1.completedAt.toDate())
            }
            MediaListSortingType.RELEASE -> {
                media1.startDate.toDate().compareTo(media2.startDate.toDate())
            }
            MediaListSortingType.RELEASE_DESC -> {
                media2.startDate.toDate().compareTo(media1.startDate.toDate())
            }
        }
    }

    enum class MediaListSortingType {
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
