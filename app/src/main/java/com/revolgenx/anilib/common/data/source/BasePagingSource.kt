package com.revolgenx.anilib.common.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.revolgenx.anilib.common.data.model.PageModel

abstract class BasePagingSource<M : Any>() :
    PagingSource<Int, M>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, M> {
        val page = params.key ?: 1
        val perPage = params.loadSize
        return try {
            val pageData = loadPage(page, perPage)
            val data = pageData.data
            val hasNextPage = pageData.pageInfo?.hasNextPage ?: false
            LoadResult.Page(
                data = data ?: emptyList(),
                prevKey = if (page > 1) page - 1 else null,
                nextKey = if (hasNextPage) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, M>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    protected abstract suspend fun loadPage(page: Int, perPage: Int): PageModel<M>
}