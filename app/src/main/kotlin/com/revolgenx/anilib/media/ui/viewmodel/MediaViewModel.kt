package com.revolgenx.anilib.media.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.store.AuthDataStore
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcFire
import com.revolgenx.anilib.common.ui.icons.appicon.IcForum
import com.revolgenx.anilib.common.ui.icons.appicon.IcGroup
import com.revolgenx.anilib.common.ui.icons.appicon.IcPerson
import com.revolgenx.anilib.common.ui.icons.appicon.IcRecommendation
import com.revolgenx.anilib.common.ui.icons.appicon.IcStar
import com.revolgenx.anilib.common.ui.icons.appicon.IcStats
import com.revolgenx.anilib.common.ui.icons.appicon.IcWatch
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.media.data.field.MediaOverviewField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.ui.model.MediaModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import anilib.i18n.R as I18nR

enum class MediaScreenPageType {
    OVERVIEW,
    RECOMMENDATIONS,
    WATCH,
    CHARACTER,
    STAFF,
    REVIEW,
    STATS,
    SOCIAL,
}

private typealias MediaScreenPage = PagerScreen<MediaScreenPageType>

class MediaViewModel(
    private val mediaService: MediaService,
    private val authDataStore: AuthDataStore
) :
    ResourceViewModel<MediaModel, MediaOverviewField>() {

    override val field = MediaOverviewField()

    private val watchPage = MediaScreenPage(
        MediaScreenPageType.WATCH,
        I18nR.string.watch,
        AppIcons.IcWatch,
        mutableStateOf(false)
    )

    private val recommendationsPage = MediaScreenPage(
        MediaScreenPageType.RECOMMENDATIONS,
        I18nR.string.recommendations,
        AppIcons.IcRecommendation,
        mutableStateOf(false)
    )


    private val socialPage =
        MediaScreenPage(MediaScreenPageType.SOCIAL, I18nR.string.social,AppIcons.IcForum)

    init {
        launch {
            authDataStore.isLoggedIn.collect {
                socialPage.isVisible.value = it
            }
        }
    }

    val pages = listOf(
        MediaScreenPage(MediaScreenPageType.OVERVIEW, I18nR.string.overview,AppIcons.IcFire),
        recommendationsPage,
        watchPage,
        MediaScreenPage(MediaScreenPageType.CHARACTER, I18nR.string.character,AppIcons.IcPerson),
        MediaScreenPage(MediaScreenPageType.STAFF, I18nR.string.staff,AppIcons.IcGroup),
        MediaScreenPage(MediaScreenPageType.REVIEW, I18nR.string.review,AppIcons.IcStar),
        MediaScreenPage(MediaScreenPageType.STATS, I18nR.string.stats,AppIcons.IcStats),
        socialPage
    )

    override fun load(): Flow<MediaModel?> {
        return mediaService.getMediaOverview(field).onEach { media ->
            showHiddenPages(media)
        }
    }

    private fun showHiddenPages(media: MediaModel?) {
        if (media?.streamingEpisodes?.isNotEmpty() == true) {
            watchPage.isVisible.value = true
        }

        media?.recommendations?.pageInfo?.let {
            if (it.total.orZero() > it.perPage.orZero()) {
                recommendationsPage.isVisible.value = true
            }
        }

    }


}