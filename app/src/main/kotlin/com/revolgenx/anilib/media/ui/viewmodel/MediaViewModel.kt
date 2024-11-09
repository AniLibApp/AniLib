package com.revolgenx.anilib.media.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ext.get
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
import com.revolgenx.anilib.entry.data.field.SaveMediaListEntryField
import com.revolgenx.anilib.entry.data.service.MediaListEntryService
import com.revolgenx.anilib.list.data.store.MediaListEntryEventStore
import com.revolgenx.anilib.list.data.store.MediaListEntryEventType
import com.revolgenx.anilib.media.data.field.MediaOverviewField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
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
    private val mediaListEntryService: MediaListEntryService,
    private val appPreferencesDataStore: AppPreferencesDataStore,
    private val mediaListEntryEventStore: MediaListEntryEventStore
) :
    ResourceViewModel<MediaModel, MediaOverviewField>() {

    init {
        launch {
            mediaListEntryEventStore.mediaListUpdate.collect {
                val media = getData() ?: return@collect

                when (it.first) {
                    MediaListEntryEventType.DELETED -> {
                        media.mediaListEntry!!.status.value = null
                    }
                    else -> {
                        if (media.id == it.second.mediaId) {
                            media.mediaListEntry!!.status.value = it.second.status.value
                        }
                    }
                }
            }
        }
    }


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

    private val isLoggedIn = appPreferencesDataStore.isLoggedIn.get()
    private val socialPage =
        MediaScreenPage(MediaScreenPageType.SOCIAL, I18nR.string.social, AppIcons.IcForum, isVisible = mutableStateOf(isLoggedIn))

    val pages = listOf(
        MediaScreenPage(MediaScreenPageType.OVERVIEW, I18nR.string.overview, AppIcons.IcFire),
        recommendationsPage,
        watchPage,
        MediaScreenPage(MediaScreenPageType.CHARACTER, I18nR.string.character, AppIcons.IcPerson),
        MediaScreenPage(MediaScreenPageType.STAFF, I18nR.string.staff, AppIcons.IcGroup),
        MediaScreenPage(MediaScreenPageType.REVIEW, I18nR.string.review, AppIcons.IcStar),
        MediaScreenPage(MediaScreenPageType.STATS, I18nR.string.stats, AppIcons.IcStats),
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

    fun toggleFavourite(type: MediaType) {
        val mediaId = field.mediaId
        if (mediaId == -1) return
        val isFavourite = getData()?.isFavourite ?: return
        isFavourite.value = !isFavourite.value

        launch {
            val toggled = mediaService.toggleFavourite(mediaId = mediaId, type = type).single()
            if (!toggled) {
                isFavourite.value = !isFavourite.value
                showOperationFailedMsg()
            }
        }
    }


    fun updateEntryStatus(status: MediaListStatus) {
        val mediaId = field.mediaId
        if (mediaId == -1) return

        val saveEntryField = SaveMediaListEntryField().also {
            it.mediaId = mediaId
            it.status = status
        }
        mediaListEntryService.saveMediaListEntry(saveEntryField)
            .onEach {
                it?.let {
                    mediaListEntryEventStore.update(it)
                }
            }
            .catch {
                showOperationFailedMsg()
            }
            .launchIn(viewModelScope)
    }

    private fun showOperationFailedMsg() {
        errorMsg = anilib.i18n.R.string.operation_failed
    }

}