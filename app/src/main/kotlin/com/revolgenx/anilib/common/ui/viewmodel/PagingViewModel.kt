package com.revolgenx.anilib.common.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.flow.emptyFlow

abstract class PagingViewModel<M : Any, F : BaseField<*>, S : BasePagingSource<M, *>>(initialize: Boolean = true) :
    BaseViewModel<F>() {
    protected val pageSize = 20
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

    internal var pagingDataFlow by mutableStateOf(
        if (initialize) pager else emptyFlow()
    )

    fun refresh() {
        pagingDataFlow = pager
    }

}

@Composable
fun <M : Any, F : BaseField<*>, S : BasePagingSource<M, *>> PagingViewModel<M, F, S>.collectAsLazyPagingItems(): LazyPagingItems<M> =
    pagingDataFlow.collectAsLazyPagingItems()
