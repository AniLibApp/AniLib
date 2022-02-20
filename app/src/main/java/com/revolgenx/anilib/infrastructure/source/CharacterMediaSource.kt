package com.revolgenx.anilib.infrastructure.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.common.source.BaseRecyclerSource
import com.revolgenx.anilib.character.data.field.CharacterMediaField
import com.revolgenx.anilib.character.service.CharacterService
import com.revolgenx.anilib.media.data.model.MediaModel
import io.reactivex.disposables.CompositeDisposable

class CharacterMediaSource(
    field: CharacterMediaField,
    private val characterService: CharacterService,
    private val compositeDisposable: CompositeDisposable
) :
    BaseRecyclerSource<MediaModel, CharacterMediaField>(field) {

    override fun areItemsTheSame(first: MediaModel, second: MediaModel): Boolean {
        return first.id == second.id
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        characterService.getCharacterMediaInfo(field, compositeDisposable) {
            postResult(page, it)
        }
    }
}
