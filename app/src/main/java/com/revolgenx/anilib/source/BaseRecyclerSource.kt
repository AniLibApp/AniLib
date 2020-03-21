package com.revolgenx.anilib.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.extensions.MainSource

abstract class BaseRecyclerSource<T:Any>:MainSource<T>() {
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
}
