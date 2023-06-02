package com.revolgenx.anilib.media.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.media.data.field.MediaOverviewField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.ui.model.MediaModel
import kotlinx.coroutines.flow.Flow

enum class MediaScreenPageType {
    OVERVIEW,
    WATCH,
    CHARACTER,
    STAFF,
    REVIEW,
    STATS,
    SOCIAL,
}

private typealias MediaScreenPage = PagerScreen<MediaScreenPageType>

class MediaViewModel (private val mediaService: MediaService) :
    ResourceViewModel<MediaModel, MediaOverviewField>() {

    override val field = MediaOverviewField()

    private val watchPage = MediaScreenPage(
        MediaScreenPageType.WATCH,
        R.string.watch,
        R.drawable.ic_watch,
        mutableStateOf(false)
    )

    val pages = listOf(
        MediaScreenPage(MediaScreenPageType.OVERVIEW, R.string.overview, R.drawable.ic_fire),
        watchPage,
        MediaScreenPage(MediaScreenPageType.CHARACTER, R.string.character, R.drawable.ic_person),
        MediaScreenPage(MediaScreenPageType.STAFF, R.string.staff, R.drawable.ic_staff),
        MediaScreenPage(MediaScreenPageType.REVIEW, R.string.review, R.drawable.ic_star),
        MediaScreenPage(MediaScreenPageType.STATS, R.string.stats, R.drawable.ic_stats),
        MediaScreenPage(MediaScreenPageType.SOCIAL, R.string.social, R.drawable.ic_forum)
    )

    override fun loadData(): Flow<MediaModel?> {
        return mediaService.getMediaOverview(field)
    }

    fun showWatchPage() {
        watchPage.isVisible.value = true
    }

}