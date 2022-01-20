package com.revolgenx.anilib.common.infrastruture.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.extensions.MainSource
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.repository.util.Status

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
        when (resource.status) {
            Status.SUCCESS -> {
                postResult(page, resource.data as? List<M> ?: emptyList())
            }
            Status.ERROR -> {
                postResult(page, Exception(resource.exception))
            }
            else->{}

        }
    }

}

