package com.revolgenx.anilib.home.explore.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnMediaClick
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreMediaViewModel
import com.revolgenx.anilib.media.ui.component.MediaCard
import com.revolgenx.anilib.media.ui.model.MediaModel
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

enum class ExploreMediaSectionType {
    TRENDING,
    POPULAR,
    NEWLY_ADDED,
}

@Composable
fun ExploreMediaSection(type: ExploreMediaSectionType) {
    ExploreMediaHeader(type)
    ExploreMediaContent(type)
}

@Composable
private fun ExploreMediaContent(type: ExploreMediaSectionType) {
    val viewModel = when (type) {
        ExploreMediaSectionType.TRENDING -> koinViewModel<ExploreMediaViewModel.ExploreTrendingViewModel>()
        ExploreMediaSectionType.POPULAR -> koinViewModel<ExploreMediaViewModel.ExplorePopularViewModel>()
        ExploreMediaSectionType.NEWLY_ADDED -> koinViewModel<ExploreMediaViewModel.ExploreNewlyAddedViewModel>()
    }

    val pagingItems = viewModel.collectAsLazyPagingItems()
    val navigator = localNavigator()

    Box(
        modifier = Modifier.height(ExploreMediaCardHeight)
    ) {
        LazyPagingList(
            pagingItems = pagingItems,
            type = ListPagingListType.ROW,
            onRefresh = {
                viewModel.refresh()
            }
        ) { model ->
            model ?: return@LazyPagingList
            ExploreMediaItem(mediaModel = model) { id, type ->
                navigator.mediaScreen(id, type)
            }
        }
    }
}

@Composable
private fun ExploreMediaItem(mediaModel: MediaModel, onClick: OnMediaClick) {
    MediaCard(
        media = mediaModel,
        onMediaClick = onClick,
        width = ExploreMediaCardWidth,
        height = ExploreMediaCardHeight,
    )
}

@Composable
private fun ExploreMediaHeader(type: ExploreMediaSectionType) {
    val titleRes = when (type) {
        ExploreMediaSectionType.TRENDING -> I18nR.string.trending
        ExploreMediaSectionType.POPULAR -> I18nR.string.popular
        ExploreMediaSectionType.NEWLY_ADDED -> I18nR.string.newly_added
    }

    ExploreScreenHeader(
        text = stringResource(id = titleRes),
        onFilter = {

        },
        onMore = {

        }
    )
}