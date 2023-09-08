package com.revolgenx.anilib.media.ui.screen

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ui.compose.paging.GridOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.media.ui.component.MediaCard
import com.revolgenx.anilib.media.ui.viewmodel.MediaRecommendationViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MediaRecommendationScreen(
    viewModel: MediaRecommendationViewModel,
) {
    val pagingItems = viewModel.collectAsLazyPagingItems()
    val navigator = localNavigator()
    LazyPagingList(
        pagingItems = pagingItems,
        type = ListPagingListType.GRID,
        gridOptions = GridOptions(GridCells.Adaptive(120.dp)),
        onRefresh = {
            viewModel.refresh()
        }
    ) { recommendationModel ->
        val media = recommendationModel?.mediaRecommendation ?: return@LazyPagingList
        MediaCard(media = media, onMediaClick = { id, type ->
            navigator.mediaScreen(id, type)
        })
    }
}