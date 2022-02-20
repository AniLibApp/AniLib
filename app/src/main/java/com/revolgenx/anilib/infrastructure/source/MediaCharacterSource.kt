package com.revolgenx.anilib.infrastructure.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.character.data.model.CharacterEdgeModel
import com.revolgenx.anilib.common.source.BaseRecyclerSource
import com.revolgenx.anilib.media.data.field.MediaCharacterField
import com.revolgenx.anilib.media.service.MediaInfoService
import com.revolgenx.anilib.type.MediaType
import io.reactivex.disposables.CompositeDisposable

class MediaCharacterSource(
    field: MediaCharacterField,
    private val mediaBrowseService: MediaInfoService,
    private val compositeDisposable: CompositeDisposable
) :
    BaseRecyclerSource<CharacterEdgeModel, MediaCharacterField>(field) {

    override fun areItemsTheSame(first: CharacterEdgeModel, second: CharacterEdgeModel): Boolean {
        return first.node?.id == second.node?.id
    }

    override fun getElementType(data: CharacterEdgeModel): Int {
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
