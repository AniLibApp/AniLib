package com.revolgenx.anilib.media.ui.screen

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.compose.paging.GridOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.media.ui.component.MediaItemColumnCard
import com.revolgenx.anilib.media.ui.component.rememberMediaComponentState
import com.revolgenx.anilib.media.ui.viewmodel.MediaRecommendationViewModel

@Composable
fun MediaRecommendationScreen(
    viewModel: MediaRecommendationViewModel,
) {
    val pagingItems = viewModel.collectAsLazyPagingItems()
    val navigator = localNavigator()
    val mediaComponentState = rememberMediaComponentState(navigator = navigator)

    LazyPagingList(
        pagingItems = pagingItems,
        type = ListPagingListType.GRID,
        gridOptions = GridOptions(GridCells.Adaptive(120.dp)),
        onRefresh = {
            viewModel.refresh()
        }
    ) { recommendationModel ->
        val media = recommendationModel?.mediaRecommendation ?: return@LazyPagingList
        MediaItemColumnCard(media = media, mediaComponentState = mediaComponentState)
    }
}