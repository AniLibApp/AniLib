package com.revolgenx.anilib.common.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingItems
import com.revolgenx.anilib.common.ui.compose.paging.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class PagingViewModel<M : Any, SM : Any> : ViewModel() {
    protected val pageSize = 20
    protected abstract val pagingSource: BasePagingSource<SM>
    var lazyPagingItems: LazyPagingItems<M>? = null

    private val pager: Flow<PagingData<M>>
        get() = Pager(
            PagingConfig(
                pageSize = pageSize,
                prefetchDistance = 4,
                initialLoadSize = pageSize + 10
            )
        ) {
            pagingSource
        }.flow
            .map {
                mapToUiModel(it)
            }
            .cachedIn(viewModelScope)

    var pagingDataFlow by mutableStateOf(pager)

    abstract fun mapToUiModel(pagingData: PagingData<SM>): PagingData<M>
    fun refresh() {
        pagingDataFlow = pager
    }
}

@Composable
fun <M : Any, SM : Any> PagingViewModel<M, SM>.collectAsLazyPagingItems(): LazyPagingItems<M> {
    lazyPagingItems = pagingDataFlow.collectAsLazyPagingItems(lazyPagingItems)
    return lazyPagingItems!!
}