package com.revolgenx.anilib.common.ui.compose.paging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.revolgenx.anilib.common.ui.component.pullrefresh.PullRefreshIndicator
import com.revolgenx.anilib.common.ui.component.pullrefresh.pullRefresh
import com.revolgenx.anilib.common.ui.component.pullrefresh.rememberPullRefreshState
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.screen.EmptyScreen
import com.revolgenx.anilib.common.ui.screen.ErrorScreen
import com.revolgenx.anilib.common.ui.screen.ErrorSection
import com.revolgenx.anilib.common.ui.screen.LoadingScreen
import com.revolgenx.anilib.common.ui.screen.LoadingSection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf

enum class ListPagingListType {
    COLUMN,
    GRID
}

data class GridOptions(val columns: GridCells)

@Composable
fun <M : BaseModel> LazyPagingList(
    modifier: Modifier = Modifier,
    type: ListPagingListType = ListPagingListType.COLUMN,
    items: LazyPagingItems<M> = flowOf(PagingData.empty<M>()).collectAsLazyPagingItems(),
    span: (LazyGridItemSpanScope.(index: Int) -> GridItemSpan)? = null,
    gridOptions: GridOptions? = null,
    onRefresh: (() -> Unit)? = null,
    itemContent: @Composable Any.(value: M?) -> Unit
) {
    var refreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(refreshing, onRefresh = {
        refreshing = true
        onRefresh?.invoke()
    })

    LaunchedEffect(refreshing) {
        if (refreshing) {
            delay(200)
            refreshing = false
        }
    }

    Box(modifier.pullRefresh(pullRefreshState)) {
        when (type) {
            ListPagingListType.COLUMN -> LazyColumnLayout(items, itemContent)
            ListPagingListType.GRID -> LazyGridLayout(
                items,
                gridOptions!!,
                span = span,
                itemContent = itemContent
            )
        }
        PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
private fun <M : BaseModel> LazyColumnLayout(
    items: LazyPagingItems<M>,
    itemContent: @Composable LazyItemScope.(value: M?) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(
            items = items,
            key = { it.key }
        ) { item ->
            itemContent(item)
        }
        lazyListResourceState(items = items)
    }
}

@Composable
private fun <M : BaseModel> LazyGridLayout(
    items: LazyPagingItems<M>,
    gridOptions: GridOptions,
    span: (LazyGridItemSpanScope.(index: Int) -> GridItemSpan)? = null,
    itemContent: @Composable() (LazyGridItemScope.(value: M?) -> Unit)
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(columns = gridOptions.columns) {
            items(items = items, span = span) { item ->
                itemContent(item)
            }
            lazyGridResourceState(items)
        }

        // initial load
        when (val state = items.loadState.refresh) {
            is LoadState.Error -> {
                ErrorScreen(state.error.message) {
                    items.retry()
                }
            }

            LoadState.Loading -> {
                LoadingScreen()
            }

            is LoadState.NotLoading -> {
                if (items.itemCount == 0) {
                    EmptyScreen()
                }
            }
        }
    }
}

private fun <M : BaseModel> LazyListScope.lazyListResourceState(
    items: LazyPagingItems<M>,
) {
    // initial load
    when (val state = items.loadState.refresh) {
        is LoadState.Error -> {
            item {
                ErrorScreen(state.error.message) {
                    items.retry()
                }
            }
        }

        LoadState.Loading -> {
            item {
                LoadingScreen()
            }
        }

        is LoadState.NotLoading -> {
            if (items.itemCount == 0) {
                item {
                    EmptyScreen()
                }
            }
        }
    }

    when (val state = items.loadState.append) {
        is LoadState.Error -> {
            item {
                ErrorSection(error = state.error.message) {
                    items.retry()
                }
            }
        }

        LoadState.Loading -> {
            item {
                LoadingSection()
            }
        }

        is LoadState.NotLoading -> {}
    }
}


private fun <M : BaseModel> LazyGridScope.lazyGridResourceState(
    items: LazyPagingItems<M>,
) {

    when (val state = items.loadState.append) {
        is LoadState.Error -> {
            item(
                span = {
                    GridItemSpan(maxLineSpan)
                }
            ) {
                ErrorSection(error = state.error.message) {
                    items.retry()
                }
            }
        }

        LoadState.Loading -> {
            item(
                span = {
                    GridItemSpan(maxLineSpan)
                }
            ) {
                LoadingSection()
            }
        }

        is LoadState.NotLoading -> {}
    }
}