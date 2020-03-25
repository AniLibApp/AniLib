package com.revolgenx.anilib.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.extensions.MainSource
import com.revolgenx.anilib.field.BaseField
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.repository.util.Status
import java.lang.Exception

abstract class BaseRecyclerSource<T : Any, F : Any>(protected var field: F) : MainSource<T>() {
    protected var pageNo = 1
    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        if (page.isFirstPage()) {
            setKey(page, 1)
        } else {
            pageNo = getKey<Int>(page.previous()!!)!!.plus(1)
            setKey(page, pageNo)
        }
    }

    fun <R> postResult(page: Page, resource: Resource<R>) {
        when (resource.status) {
            Status.SUCCESS -> {
                postResult(page, resource.data as? List<T> ?: emptyList())
            }
            Status.ERROR -> {
                postResult(page, Exception(resource.exception))
            }
        }
    }

}

