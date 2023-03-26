package com.revolgenx.anilib.common.ui.compose.paging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
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
import com.revolgenx.anilib.common.ui.screen.ErrorLayout
import com.revolgenx.anilib.common.ui.screen.ErrorScreen
import com.revolgenx.anilib.common.ui.screen.LoadingLayout
import com.revolgenx.anilib.common.ui.screen.LoadingScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf


@Composable
fun <M : BaseModel> PagingLazyColumn(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<M> = flowOf(PagingData.empty<M>()).collectAsLazyPagingItems(),
    onRefresh: (() -> Unit)? = null,
    itemContent: @Composable LazyItemScope.(value: M?) -> Unit
) {
    var refreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(refreshing, onRefresh = {
        refreshing = true
        onRefresh?.invoke()
    })

    LaunchedEffect(refreshing){
        if(refreshing){
            delay(200)
            refreshing = false
        }
    }

    Box(modifier.pullRefresh(pullRefreshState)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(
                items = items,
                key = { it._id }
            ) { item ->
                itemContent(item)
            }

            // initial load
            when (val state = items.loadState.refresh) {
                is LoadState.Error -> {
                    item {
                        ErrorScreen {
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
                        item{
                            EmptyScreen()
                        }
                    }
                }
            }

            when (val state = items.loadState.append) {
                is LoadState.Error -> {
                    item {
                        ErrorLayout{
                           items.retry()
                       }
                    }
                }

                LoadState.Loading -> {
                    item {
                        LoadingLayout()
                    }
                }

                is LoadState.NotLoading -> {}
            }
        }
        PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }


}