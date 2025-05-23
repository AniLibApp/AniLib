package com.revolgenx.anilib.common.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.common.data.source.BasePagingSource
import kotlinx.coroutines.flow.Flow

abstract class PagingViewModel<M : Any, F : BaseField<*>?, S : BasePagingSource<M, *>> :
    BaseViewModel<F>() {
    protected val pageSize = 30
    protected abstract val pagingSource: S

    private val pager: Flow<PagingData<M>>
        get() = Pager(
            PagingConfig(
                pageSize = pageSize,
                prefetchDistance = 4,
                initialLoadSize = pageSize
            )
        ) {
            pagingSource
        }.flow
            .cachedIn(viewModelScope)

    internal var pagingDataFlow by mutableStateOf(pager)

    fun refresh() {
        pagingDataFlow = pager

        Pager(
            PagingConfig(
                pageSize = pageSize,
                prefetchDistance = 4,
                initialLoadSize = pageSize
            )
        ) {
            pagingSource
        }
    }

}

@Composable
fun <M : Any, F : BaseField<*>?, S : BasePagingSource<M, *>> PagingViewModel<M, F, S>.collectAsLazyPagingItems(): LazyPagingItems<M> =
    pagingDataFlow.collectAsLazyPagingItems()
