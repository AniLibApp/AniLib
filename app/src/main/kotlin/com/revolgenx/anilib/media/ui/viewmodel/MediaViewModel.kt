package com.revolgenx.anilib.media.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import com.revolgenx.anilib.media.ui.model.MediaModel

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

class MediaViewModel : ViewModel() {
    var media = mutableStateOf(MediaModel())
    val pages = listOf(
        MediaScreenPage(MediaScreenPageType.OVERVIEW, R.string.overview, R.drawable.ic_fire),
        MediaScreenPage(MediaScreenPageType.WATCH, R.string.watch, R.drawable.ic_watch),
        MediaScreenPage(MediaScreenPageType.CHARACTER, R.string.character, R.drawable.ic_person),
        MediaScreenPage(MediaScreenPageType.STAFF, R.string.staff, R.drawable.ic_staff),
        MediaScreenPage(MediaScreenPageType.REVIEW, R.string.review, R.drawable.ic_star),
        MediaScreenPage(MediaScreenPageType.STATS, R.string.stats, R.drawable.ic_stats),
        MediaScreenPage(MediaScreenPageType.SOCIAL, R.string.social, R.drawable.ic_forum)
    )
}