package com.revolgenx.anilib.home.explore.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreMediaViewModel
import com.revolgenx.anilib.media.ui.component.CoverMediaCard
import com.revolgenx.anilib.media.ui.component.MediaComponentState
import com.revolgenx.anilib.media.ui.component.rememberMediaComponentState
import com.revolgenx.anilib.media.ui.model.MediaModel
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

@Composable
fun ExploreMediaSection(viewModel: ExploreMediaViewModel) {
    ExploreMediaHeader(viewModel)
    ExploreMediaContent(viewModel)
}

@Composable
private fun ExploreMediaContent(viewModel: ExploreMediaViewModel) {
    val pagingItems = viewModel.collectAsLazyPagingItems()
    val navigator = localNavigator()
    val mediaComponentState = rememberMediaComponentState(navigator = navigator)


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
            ExploreMediaItem(media = model, mediaComponentState = mediaComponentState)
        }
    }
}

@Composable
private fun ExploreMediaItem(
    media: MediaModel,
    mediaComponentState: MediaComponentState
) {
    CoverMediaCard(
        media = media,
        mediaComponentState = mediaComponentState,
        width = ExploreMediaCardWidth,
        height = ExploreMediaCardHeight,
    )
}

@Composable
private fun ExploreMediaHeader(viewModel: ExploreMediaViewModel) {
    val titleRes = when (viewModel) {
        is ExploreMediaViewModel.ExploreTrendingViewModel -> I18nR.string.trending
        is ExploreMediaViewModel.ExplorePopularViewModel -> I18nR.string.popular
        is ExploreMediaViewModel.ExploreNewlyAddedViewModel -> I18nR.string.newly_added
    }
    ExploreScreenHeader(
        text = stringResource(id = titleRes),
        onFilter = {

        },
        onMore = {

        }
    )
}