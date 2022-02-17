package com.revolgenx.anilib.common

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.extensions.MainSource

class ListSource<T : Any>(
    val list: MutableList<T>,
    loadingIndicatorsEnabled: Boolean = true,
    errorIndicatorEnabled: Boolean = true,
    emptyIndicatorEnabled: Boolean = true,
    private val elementType: Int = 0
) : MainSource<T>(
    loadingIndicatorsEnabled = loadingIndicatorsEnabled,
    errorIndicatorEnabled = errorIndicatorEnabled,
    emptyIndicatorEnabled = emptyIndicatorEnabled
) {

    override fun areItemsTheSame(first: T, second: T): Boolean {
        return first == second
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        if (page.isFirstPage()) {
            postResult(page, list)
        }
    }

    override fun getElementType(data: T): Int = elementType
}