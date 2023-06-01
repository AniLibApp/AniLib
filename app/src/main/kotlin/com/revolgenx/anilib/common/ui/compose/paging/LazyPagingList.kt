package com.revolgenx.anilib.common.ui.compose.paging

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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import com.revolgenx.anilib.common.ui.component.pullrefresh.PullRefreshIndicator
import com.revolgenx.anilib.common.ui.component.pullrefresh.pullRefresh
import com.revolgenx.anilib.common.ui.component.pullrefresh.rememberPullRefreshState
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.screen.state.EmptyScreen
import com.revolgenx.anilib.common.ui.screen.state.ErrorScreen
import com.revolgenx.anilib.common.ui.screen.state.ErrorSection
import com.revolgenx.anilib.common.ui.screen.state.LoadingScreen
import com.revolgenx.anilib.common.ui.screen.state.LoadingSection
import kotlinx.coroutines.delay

enum class ListPagingListType {
    COLUMN,
    GRID
}

data class GridOptions(val columns: GridCells)

@Composable
fun <M : BaseModel> LazyPagingList(
    modifier: Modifier = Modifier,
    type: ListPagingListType = ListPagingListType.COLUMN,
    items: List<M>? = null,
    pagingItems: LazyPagingItems<M>? = null,
    span: (LazyGridItemSpanScope.(index: Int) -> GridItemSpan)? = null,
    gridOptions: GridOptions? = null,
    onRefresh: (() -> Unit)? = null,
    itemContentIndex: (@Composable LazyItemScope.(index: Int, value: M?) -> Unit)? = null,
    itemContent: (@Composable Any.(value: M?) -> Unit)? = null
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
            ListPagingListType.COLUMN -> LazyColumnLayout(
                items = items,
                pagingItems = pagingItems,
                itemContent = itemContent,
                itemContentIndex = itemContentIndex
            )

            ListPagingListType.GRID -> LazyGridLayout(
                items = items,
                pagingItems = pagingItems,
                gridOptions = gridOptions!!,
                span = span,
                itemContent = itemContent!!
            )
        }
        PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
private fun <M : BaseModel> LazyColumnLayout(
    items: List<M>?,
    pagingItems: LazyPagingItems<M>?,
    itemContentIndex: (@Composable LazyItemScope.(index: Int, value: M?) -> Unit)? = null,
    itemContent: (@Composable LazyItemScope.(value: M?) -> Unit)? = null
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        pagingItems?.let {
            itemsIndexed(
                items = pagingItems,
                key = { i, _ -> i }
            ) { index, item ->
                if (itemContent != null) {
                    itemContent(item)
                }

                if (itemContentIndex != null) {
                    itemContentIndex(index, item)
                }
            }
        }

        items?.let {
            itemsIndexed(
                items = items,
                key = { i, _ -> i }
            ) { index, item ->
                if (itemContent != null) {
                    itemContent(item)
                }

                if (itemContentIndex != null) {
                    itemContentIndex(index, item)
                }
            }
        }

        pagingItems?.let {
            lazyListResourceState(items = items, pagingItems = pagingItems)
        }
    }
}

@Composable
private fun <M : BaseModel> LazyGridLayout(
    items: List<M>?,
    pagingItems: LazyPagingItems<M>?,
    gridOptions: GridOptions,
    span: (LazyGridItemSpanScope.(index: Int) -> GridItemSpan)? = null,
    itemContent: @Composable() (LazyGridItemScope.(value: M?) -> Unit)
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(columns = gridOptions.columns) {
            pagingItems?.let {
                items(items = pagingItems, span = span) { item ->
                    itemContent(item)
                }
                lazyGridResourceState(pagingItems)
            }

            items?.let {
                items(items = items) { item ->
                    itemContent(item)
                }
            }
        }

        if (items?.isEmpty() == true) {
            EmptyScreen()
        }

        // initial load
        when (val state = pagingItems?.loadState?.refresh) {
            is LoadState.Error -> {
                ErrorScreen(state.error.message) {
                    pagingItems.retry()
                }
            }

            LoadState.Loading -> {
                LoadingScreen()
            }

            is LoadState.NotLoading -> {
                if (pagingItems.itemCount == 0) {
                    EmptyScreen()
                }
            }

            else -> {}
        }
    }
}

private fun <M : BaseModel> LazyListScope.lazyListResourceState(
    items: List<M>?,
    pagingItems: LazyPagingItems<M>?,
) {

    if (items?.isEmpty() == true) {
        item {
            EmptyScreen()
        }
    }

    // initial load
    when (val state = pagingItems?.loadState?.refresh) {
        is LoadState.Error -> {
            item {
                ErrorScreen(state.error.message) {
                    pagingItems.retry()
                }
            }
        }

        LoadState.Loading -> {
            item {
                LoadingScreen()
            }
        }

        is LoadState.NotLoading -> {
            if (pagingItems.itemCount == 0) {
                item {
                    EmptyScreen()
                }
            }
        }

        else -> {}
    }

    when (val state = pagingItems?.loadState?.append) {
        is LoadState.Error -> {
            item {
                ErrorSection(error = state.error.message) {
                    pagingItems.retry()
                }
            }
        }

        LoadState.Loading -> {
            item {
                LoadingSection()
            }
        }

        is LoadState.NotLoading -> {}
        else -> {}
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