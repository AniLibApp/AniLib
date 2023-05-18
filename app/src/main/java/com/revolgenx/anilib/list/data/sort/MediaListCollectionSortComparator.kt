package com.revolgenx.anilib.list.data.sort

import com.revolgenx.anilib.common.ext.getOrDefault
import com.revolgenx.anilib.list.ui.model.MediaListModel

class MediaListCollectionSortComparator: Comparator<MediaListModel> {
    var type: MediaListSortType = MediaListSortType.TITLE
    override fun compare(item1: MediaListModel, item2: MediaListModel): Int {
        val media1 = item1.media!!
        val media2 = item2.media!!
        return when (type) {
            MediaListSortType.TITLE -> {
                media1.title?.userPreferred.getOrDefault()
                    .compareTo(media2.title?.userPreferred.getOrDefault())
            }
            MediaListSortType.TITLE_DESC -> {
                media2.title?.userPreferred.getOrDefault()
                    .compareTo(media1.title?.userPreferred.getOrDefault())
            }
            MediaListSortType.SCORE -> {
                (item1.score.getOrDefault()).compareTo(item2.score.getOrDefault())
            }
            MediaListSortType.SCORE_DESC -> {
                (item2.score.getOrDefault()).compareTo(item1.score.getOrDefault())
            }
            MediaListSortType.PROGRESS -> {
                (item1.progress.getOrDefault()).compareTo(item2.progress.getOrDefault())
            }
            MediaListSortType.PROGRESS_DESC -> {
                (item2.progress.getOrDefault()).compareTo(item1.progress.getOrDefault())
            }
            MediaListSortType.AVERAGE_SCORE -> {
                (media1.averageScore.getOrDefault()).compareTo(media2.averageScore.getOrDefault())
            }
            MediaListSortType.AVERAGE_SCORE_DESC -> {
                (media2.averageScore.getOrDefault()).compareTo(media1.averageScore.getOrDefault())
            }
            MediaListSortType.POPULARITY -> {
                (media1.popularity.getOrDefault()).compareTo(media2.popularity.getOrDefault())
            }
            MediaListSortType.POPULARITY_DESC -> {
                (media2.popularity.getOrDefault()).compareTo(media1.popularity.getOrDefault())
            }
            MediaListSortType.UPDATED_AT -> {
                (item1.updatedAt.getOrDefault()).compareTo(item2.updatedAt.getOrDefault())
            }
            MediaListSortType.UPDATED_AT_DESC -> {
                (item2.updatedAt.getOrDefault()).compareTo(item1.updatedAt.getOrDefault())
            }
            MediaListSortType.ADDED -> {
                (item1.createdAt.getOrDefault()).compareTo(item2.createdAt.getOrDefault())
            }
            MediaListSortType.ADDED_DESC -> {
                (item2.createdAt.getOrDefault()).compareTo(item1.createdAt.getOrDefault())
            }
            MediaListSortType.STARTED -> {
                item1.startedAt?.toZoneDateTime()?.compareTo(item2.startedAt?.toZoneDateTime()) ?: 0
            }
            MediaListSortType.STARTED_DESC -> {
                item2.startedAt?.toZoneDateTime()?.compareTo(item1.startedAt?.toZoneDateTime()) ?: 0
            }
            MediaListSortType.COMPLETED -> {
                item1.completedAt?.toZoneDateTime()?.compareTo(item2.completedAt?.toZoneDateTime()) ?: 0
            }
            MediaListSortType.COMPLETED_DESC -> {
                item2.completedAt?.toZoneDateTime()?.compareTo(item1.completedAt?.toZoneDateTime()) ?: 0
            }
            MediaListSortType.RELEASE -> {
                media1.startDate?.toZoneDateTime()?.compareTo(media2.startDate?.toZoneDateTime()) ?: 0
            }
            MediaListSortType.RELEASE_DESC -> {
                media2.startDate?.toZoneDateTime()?.compareTo(media1.startDate?.toZoneDateTime()) ?: 0
            }
        }
    }


}

enum class MediaListSortType {
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