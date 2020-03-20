package com.revolgenx.anilib.source

import android.content.Context
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.extensions.MainSource
import com.revolgenx.anilib.constant.PAGE_SIZE
import com.revolgenx.anilib.field.overview.MediaCharacterField
import com.revolgenx.anilib.model.MediaCharacterModel
import com.revolgenx.anilib.repository.util.Status
import com.revolgenx.anilib.service.MediaBrowseService
import com.revolgenx.anilib.type.MediaType
import io.reactivex.disposables.CompositeDisposable

class MediaCharacterSource(
    private val pageSize: Int = PAGE_SIZE,
    private val field: MediaCharacterField,
    private val mediaBrowseService: MediaBrowseService,
    private val compositeDisposable: CompositeDisposable
) :
    MainSource<MediaCharacterModel>() {

    override fun areItemsTheSame(first: MediaCharacterModel, second: MediaCharacterModel): Boolean {
        return first.characterId == second.characterId
    }

    override fun getElementType(data: MediaCharacterModel): Int {
        return when (field.type) {
            MediaType.MANGA.ordinal -> 1
            else -> 0
        }
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        var key = 1
        if (page.isFirstPage()) {
            setKey(page, 1)
        } else {
            key = getKey<Int>(page.previous()!!)!!.plus(1)
            setKey(page, key)
        }

        field.page = key
        field.perPage = pageSize

        mediaBrowseService.getMediaCharacter(field, compositeDisposable) { res ->
            when (res.status) {
                Status.SUCCESS -> {
                    postResult(page, res.data ?: emptyList())
                }
                Status.ERROR -> {
                    postResult(page, Exception(res.message))
                }
            }
        }
    }
}
