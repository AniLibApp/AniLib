package com.revolgenx.anilib.list.data.sort

import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.list.ui.model.MediaListModel
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import java.time.ZonedDateTime

class MediaListCollectionSortComparator(
    @MediaTitleModel.Companion.MediaTitleType var titleType: Int
): Comparator<MediaListModel> {
    var type: MediaListSortType = MediaListSortType.TITLE
    override fun compare(item1: MediaListModel, item2: MediaListModel): Int {
        val media1 = item1.media!!
        val media2 = item2.media!!
        return when (type) {
            MediaListSortType.TITLE -> {
                media1.title?.title(titleType).orEmpty()
                    .compareTo(media2.title?.title(titleType).orEmpty())
            }
            MediaListSortType.TITLE_DESC -> {
                media2.title?.title(titleType).orEmpty()
                    .compareTo(media1.title?.title(titleType).orEmpty())
            }
            MediaListSortType.SCORE -> {
                item1.score.orZero().compareTo(item2.score.orZero())
            }
            MediaListSortType.SCORE_DESC -> {
                item2.score.orZero().compareTo(item1.score.orZero())
            }
            MediaListSortType.PROGRESS -> {
                item1.progress.orZero().compareTo(item2.progress.orZero())
            }
            MediaListSortType.PROGRESS_DESC -> {
                item2.progress.orZero().compareTo(item1.progress.orZero())
            }
            MediaListSortType.AVERAGE_SCORE -> {
                media1.averageScore.orZero().compareTo(media2.averageScore.orZero())
            }
            MediaListSortType.AVERAGE_SCORE_DESC -> {
                media2.averageScore.orZero().compareTo(media1.averageScore.orZero())
            }
            MediaListSortType.POPULARITY -> {
                media1.popularity.orZero().compareTo(media2.popularity.orZero())
            }
            MediaListSortType.POPULARITY_DESC -> {
                media2.popularity.orZero().compareTo(media1.popularity.orZero())
            }
            MediaListSortType.UPDATED_AT -> {
                item1.updatedAt.orZero().compareTo(item2.updatedAt.orZero())
            }
            MediaListSortType.UPDATED_AT_DESC -> {
                item2.updatedAt.orZero().compareTo(item1.updatedAt.orZero())
            }
            MediaListSortType.ADDED -> {
                item1.createdAt.orZero().compareTo(item2.createdAt.orZero())
            }
            MediaListSortType.ADDED_DESC -> {
                item2.createdAt.orZero().compareTo(item1.createdAt.orZero())
            }
            MediaListSortType.STARTED -> {
                compareDate(item1.startedAt?.toZoneDateTime(), item2.startedAt?.toZoneDateTime())
            }
            MediaListSortType.STARTED_DESC -> {
                compareDate(item2.startedAt?.toZoneDateTime(), item1.startedAt?.toZoneDateTime())
            }
            MediaListSortType.COMPLETED -> {
                compareDate(item1.completedAt?.toZoneDateTime(), item2.completedAt?.toZoneDateTime())
            }
            MediaListSortType.COMPLETED_DESC -> {
                compareDate(item2.completedAt?.toZoneDateTime(), item1.completedAt?.toZoneDateTime())
            }
            MediaListSortType.RELEASE -> {
                compareDate(media1.startDate?.toZoneDateTime(), media2.startDate?.toZoneDateTime())
            }
            MediaListSortType.RELEASE_DESC -> {
                compareDate(media2.startDate?.toZoneDateTime(), media1.startDate?.toZoneDateTime())
            }
        }
    }

    private fun compareDate(dateTime1: ZonedDateTime?, dateTime2: ZonedDateTime?): Int{
        return when {
            dateTime1 == null && dateTime2 == null -> 0
            dateTime1 == null -> -1
            dateTime2 == null -> 1
            else -> dateTime1.compareTo(dateTime2)
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