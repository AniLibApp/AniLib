package com.revolgenx.anilib.home.explore.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcFire
import com.revolgenx.anilib.common.ui.icons.appicon.IcNew
import com.revolgenx.anilib.common.ui.icons.appicon.IcStar
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreMediaViewModel
import com.revolgenx.anilib.home.explore.component.ExploreMediaCard
import com.revolgenx.anilib.home.explore.component.ExploreMediaCardHeight
import com.revolgenx.anilib.media.ui.component.MediaComponentState
import com.revolgenx.anilib.media.ui.component.rememberMediaComponentState
import com.revolgenx.anilib.media.ui.model.MediaModel
import anilib.i18n.R as I18nR

@Composable
fun ExploreMediaSection(viewModel: ExploreMediaViewModel, onFilter: OnClick, onMore: OnClick) {
    ExploreMediaHeader(viewModel, onFilter, onMore)
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
            onPullRefresh = false
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
    ExploreMediaCard(
        media = media,
        mediaComponentState = mediaComponentState,
    )
}

@Composable
private fun ExploreMediaHeader(viewModel: ExploreMediaViewModel, onFilter: OnClick, onMore: OnClick) {
    val headerPair = when (viewModel) {
        is ExploreMediaViewModel.ExploreTrendingViewModel -> I18nR.string.trending to AppIcons.IcFire
        is ExploreMediaViewModel.ExplorePopularViewModel -> I18nR.string.popular to AppIcons.IcStar
        is ExploreMediaViewModel.ExploreNewlyAddedViewModel -> I18nR.string.newly_added to AppIcons.IcNew
    }
    ExploreScreenHeader(
        text = stringResource(id = headerPair.first),
        icon = headerPair.second,
        onFilter = onFilter,
        onMore = onMore
    )
}