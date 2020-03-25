package com.revolgenx.anilib.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.field.CharacterVoiceActorField
import com.revolgenx.anilib.model.VoiceActorModel
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.repository.util.Status
import com.revolgenx.anilib.service.CharacterService
import io.reactivex.disposables.CompositeDisposable
import java.lang.Exception

class CharacterActorSource(
    field: CharacterVoiceActorField,
    private val characterService: CharacterService,
    private val compositeDisposable: CompositeDisposable?
) : BaseRecyclerSource<VoiceActorModel,CharacterVoiceActorField>(field) {
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
