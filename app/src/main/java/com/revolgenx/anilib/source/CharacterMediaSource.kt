package com.revolgenx.anilib.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.field.CharacterMediaField
import com.revolgenx.anilib.model.character.CharacterMediaModel
import com.revolgenx.anilib.repository.util.Status
import com.revolgenx.anilib.service.CharacterService
import io.reactivex.disposables.CompositeDisposable
import java.lang.Exception

class CharacterMediaSource(
    private val field: CharacterMediaField,
    private val characterService: CharacterService,
    private val compositeDisposable: CompositeDisposable
) :
    BaseRecyclerSource<CharacterMediaModel>() {

    override fun areItemsTheSame(first: CharacterMediaModel, second: CharacterMediaModel): Boolean {
        return first.mediaId == second.mediaId
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        characterService.getCharacterMediaInfo(field, compositeDisposable) {
            when (it.status) {
                Status.SUCCESS -> {
                    postResult(page, it.data ?: emptyList())
                }
                Status.ERROR -> {
                    postResult(page, Exception(it.exception))
                }
            }
        }
    }
}
