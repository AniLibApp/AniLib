package com.revolgenx.anilib.common.ui.compose.paging

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.screen.state.EmptyScreen
import com.revolgenx.anilib.common.ui.screen.state.ErrorScreen
import com.revolgenx.anilib.common.ui.screen.state.ErrorSection
import com.revolgenx.anilib.common.ui.screen.state.LoadingScreen
import com.revolgenx.anilib.common.ui.screen.state.LoadingSection
import com.revolgenx.anilib.common.util.OnClick

enum class ListPagingListType {
    COLUMN,
    ROW,
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
    onPullRefresh: Boolean = true,
    onRefresh: OnClick? = null,
    scrollState: ScrollableState? = null,
    divider: @Composable (() -> Unit)? = null,
    itemContentIndex: (@Composable Any.(index: Int, value: M?) -> Unit)? = null,
    itemContent: (@Composable Any.(value: M?) -> Unit)? = null
) {
    LazyPagingListBox(
        modifier = modifier,
        onPullRefresh = onPullRefresh,
        onRefresh = onRefresh
    ) {
        when (type) {
            ListPagingListType.COLUMN -> LazyColumnLayout(
                items = items,
                pagingItems = pagingItems,
                divider = divider,
                itemContent = itemContent,
                itemContentIndex = itemContentIndex,
                state = scrollState as? LazyListState
            )

            ListPagingListType.ROW -> LazyRowLayout(
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
                itemContent = itemContent,
                itemContentIndex = itemContentIndex,
                state = scrollState as? LazyGridState
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LazyPagingListBox(
    modifier: Modifier,
    onPullRefresh: Boolean,
    onRefresh: OnClick?,
    content: @Composable BoxScope.() -> Unit
) {
    if (onPullRefresh) {
        var isRefreshing by remember {
            mutableStateOf(false)
        }

        if (isRefreshing) {
            SideEffect {
                isRefreshing = false
            }
        }
        PullToRefreshBox(
            modifier = modifier,
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                onRefresh?.invoke()
            },
            content = content
        )
    } else {
        Box(modifier = modifier, content = content)
    }

}

@Composable
private fun <M : BaseModel> LazyColumnLayout(
    items: List<M>?,
    pagingItems: LazyPagingItems<M>?,
    divider: @Composable() (() -> Unit)? = null,
    itemContentIndex: @Composable() (LazyItemScope.(index: Int, value: M?) -> Unit)? = null,
    itemContent: @Composable() (LazyItemScope.(value: M?) -> Unit)? = null,
    state: LazyListState?
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = state ?: rememberLazyListState()
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


                divider?.let {
                    if (!pagingItems.loadState.append.endOfPaginationReached || index < pagingItems.itemCount - 1) {
                        it.invoke()
                    }
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
            lazyListResourceState(
                items = items,
                pagingItems = pagingItems,
                type = ListPagingListType.COLUMN
            )
        }
    }
}


@Composable
private fun <M : BaseModel> LazyRowLayout(
    items: List<M>?,
    pagingItems: LazyPagingItems<M>?,
    itemContentIndex: (@Composable LazyItemScope.(index: Int, value: M?) -> Unit)? = null,
    itemContent: (@Composable LazyItemScope.(value: M?) -> Unit)? = null
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
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
            lazyListResourceState(
                items = items,
                pagingItems = pagingItems,
                type = ListPagingListType.ROW
            )
        }
    }
}


@Composable
private fun <M : BaseModel> LazyGridLayout(
    items: List<M>?,
    pagingItems: LazyPagingItems<M>?,
    gridOptions: GridOptions,
    span: (LazyGridItemSpanScope.(index: Int) -> GridItemSpan)? = null,
    itemContent: @Composable() (LazyGridItemScope.(value: M?) -> Unit)? = null,
    itemContentIndex: @Composable() (LazyGridItemScope.(index: Int, value: M?) -> Unit)? = null,
    state: LazyGridState?,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(columns = gridOptions.columns, state = state ?: rememberLazyGridState()) {
            pagingItems?.let {
                itemsIndexed(
                    items = pagingItems,
                    span = span,
                    key = { i, _ -> i }
                ) { index, item ->
                    if (itemContent != null) {
                        itemContent(item)
                    }

                    if (itemContentIndex != null) {
                        itemContentIndex(index, item)
                    }
                }
                lazyGridResourceState(pagingItems)
            }

            items?.let {
                itemsIndexed(
                    items = it,
                ) { index, item ->
                    if (itemContent != null) {
                        itemContent(item)
                    }

                    if (itemContentIndex != null) {
                        itemContentIndex(index, item)
                    }
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
    type: ListPagingListType,
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
                LoadingSection(typeRow = type == ListPagingListType.ROW)
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