package com.revolgenx.anilib.common.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.extensions.MainSource
import com.revolgenx.anilib.common.repository.util.Resource

abstract class BaseRecyclerSource<M : Any, F>(protected var field: F) : MainSource<M>() {
    protected var pageNo = 1

    protected open fun savePageKey() = true

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        if (savePageKey()) {
            if (page.isFirstPage()) {
                setKey(page, 1)
            } else {
                pageNo = getKey<Int>(page.previous()!!)!!.plus(1)
                setKey(page, pageNo)
            }
        }

    }

    @Suppress("UNCHECKED_CAST")
    protected open fun <R> postResult(page: Page, resource: Resource<R>) {
        when (resource) {
            is Resource.Success -> {
                postResult(page, resource.data as? List<M> ?: emptyList())
            }
            is Resource.Error -> {
                postResult(page, Exception(resource.exception))
            }
            else->{}

        }
    }

}

