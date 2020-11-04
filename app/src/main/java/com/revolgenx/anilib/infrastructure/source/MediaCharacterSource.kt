package com.revolgenx.anilib.infrastructure.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.common.infrastruture.source.BaseRecyclerSource
import com.revolgenx.anilib.data.field.media.MediaCharacterField
import com.revolgenx.anilib.data.model.MediaCharacterModel
import com.revolgenx.anilib.infrastructure.service.media.MediaBrowseService
import com.revolgenx.anilib.type.MediaType
import io.reactivex.disposables.CompositeDisposable

class MediaCharacterSource(
    field: MediaCharacterField,
    private val mediaBrowseService: MediaBrowseService,
    private val compositeDisposable: CompositeDisposable
) :
    BaseRecyclerSource<MediaCharacterModel, MediaCharacterField>(field) {

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
        field.page = pageNo
        mediaBrowseService.getMediaCharacter(field, compositeDisposable) { res ->
            postResult(page, res)
        }
    }
}
