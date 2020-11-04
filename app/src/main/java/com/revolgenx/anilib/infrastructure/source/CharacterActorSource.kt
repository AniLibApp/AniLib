package com.revolgenx.anilib.infrastructure.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.common.infrastruture.source.BaseRecyclerSource
import com.revolgenx.anilib.data.field.character.CharacterVoiceActorField
import com.revolgenx.anilib.data.model.VoiceActorModel
import com.revolgenx.anilib.infrastructure.service.character.CharacterService
import io.reactivex.disposables.CompositeDisposable

class CharacterActorSource(
    field: CharacterVoiceActorField,
    private val characterService: CharacterService,
    private val compositeDisposable: CompositeDisposable?
) : BaseRecyclerSource<VoiceActorModel, CharacterVoiceActorField>(field) {
    override fun areItemsTheSame(first: VoiceActorModel, second: VoiceActorModel): Boolean {
        return first.actorId == second.actorId
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        if (page.isFirstPage()) {
            characterService.getCharacterActor(field, compositeDisposable) {
               postResult(page, it)
            }
        } else {
            postResult(page, emptyList<VoiceActorModel>())
        }
    }
}
