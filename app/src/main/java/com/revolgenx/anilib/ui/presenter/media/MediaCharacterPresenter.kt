package com.revolgenx.anilib.ui.presenter.media

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.infrastructure.event.BrowseCharacterEvent
import com.revolgenx.anilib.infrastructure.event.BrowseStaffEvent
import com.revolgenx.anilib.data.meta.CharacterMeta
import com.revolgenx.anilib.data.meta.StaffMeta
import com.revolgenx.anilib.data.model.MediaCharacterModel
import kotlinx.android.synthetic.main.anime_character_presenter_layout.view.*
import kotlinx.android.synthetic.main.manga_character_presenter_layout.view.*

class MediaCharacterPresenter(context: Context) : Presenter<MediaCharacterModel>(context) {

    override val elementTypes: Collection<Int>
        get() = listOf(0, 1)

    private val characterRoles by lazy {
        context.resources.getStringArray(R.array.character_role)
    }
    private val staffLanguages by lazy {
        context.resources.getStringArray(R.array.staff_language)
    }

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        val v =
            LayoutInflater.from(parent.context).inflate(
                when (elementType) {
                    1 -> R.layout.manga_character_presenter_layout
                    else ->
                        R.layout.anime_character_presenter_layout
                },
                parent,
                false
            )

        return Holder(v);
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaCharacterModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        when (holder.elementType) {
            0 -> {
                holder.itemView.apply {
                    characterImageView.setImageURI(item.characterImageModel?.image)
                    characterNameTv.text = item.name
                    characterRoleTv.text = item.role?.let { characterRoles[it] }
                    characterLayoutContainer.setOnClickListener {
                        BrowseCharacterEvent(
                            CharacterMeta(
                                item.characterId ?: -1,
                                item.characterImageModel?.image
                            ),
                            characterImageView
                        ).postEvent
                    }

                    item.voiceActor?.let { actorModel ->
                        voiceActorLayoutContainer
                        voiceActorImageView.setImageURI(actorModel.voiceActorImageModel?.image)
                        voiceActorNameTv.text = actorModel.name
                        voiceActorLanguage.text =
                            actorModel.language?.let { staffLanguages[it] }

                        voiceActorLayoutContainer.setOnClickListener {
                            BrowseStaffEvent(
                                StaffMeta(
                                    actorModel.actorId ?: -1,
                                    actorModel.voiceActorImageModel?.image
                                )
                            ).postEvent
                        }
                    }

                }
            }
            1 -> {
                holder.itemView.apply {
                    mangaCharacterImageView.setImageURI(item.characterImageModel?.image)
                    mangaCharacterNameTv.text = item.name
                    mangaCharacterRoleTv.text = item.role?.let { characterRoles[it] }
                    mangaCharacterContainer.setOnClickListener {
                        BrowseCharacterEvent(
                            CharacterMeta(
                                item.characterId ?: -1,
                                item.characterImageModel?.image
                            ),
                            mangaCharacterImageView
                        ).postEvent
                    }
                }
            }
        }
    }
}
