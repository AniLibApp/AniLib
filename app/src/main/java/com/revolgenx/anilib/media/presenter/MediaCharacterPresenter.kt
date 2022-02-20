package com.revolgenx.anilib.media.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.character.data.model.CharacterEdgeModel
import com.revolgenx.anilib.media.data.model.MediaEdgeModel
import com.revolgenx.anilib.databinding.AnimeCharacterPresenterLayoutBinding
import com.revolgenx.anilib.databinding.MangaCharacterPresenterLayoutBinding
import com.revolgenx.anilib.common.event.OpenCharacterEvent
import com.revolgenx.anilib.common.event.OpenStaffEvent
import com.revolgenx.anilib.common.presenter.BasePresenter

class MediaCharacterPresenter(context: Context) :
    BasePresenter<ViewBinding, CharacterEdgeModel>(context) {

    override val elementTypes: Collection<Int>
        get() = listOf(0, 1)

    private val characterRoles by lazy {
        context.resources.getStringArray(R.array.character_role)
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): ViewBinding {
        return when (elementType) {
            1 -> {
                MangaCharacterPresenterLayoutBinding.inflate(inflater, parent, false)
            }
            else -> {
                AnimeCharacterPresenterLayoutBinding.inflate(inflater, parent, false)
            }
        }
    }

    override fun onBind(page: Page, holder: Holder, element: Element<CharacterEdgeModel>) {
        super.onBind(page, holder, element)
        val edge = element.data ?: return
        val item = edge.node ?: return
        val binding = holder.getBinding() ?: return
        when (holder.elementType) {
            0 -> {
                (binding as AnimeCharacterPresenterLayoutBinding).apply {
                    characterImageView.setImageURI(item.image?.image)
                    characterNameTv.text = item.name?.full
                    characterRoleTv.text = edge.role?.let { characterRoles[it] }
                    characterLayoutContainer.setOnClickListener {
                        OpenCharacterEvent(
                            item.id
                        ).postEvent
                    }

                    edge.voiceActors?.firstOrNull()?.let { actorModel ->
                        voiceActorLayoutContainer
                        voiceActorImageView.setImageURI(actorModel.image?.image)
                        voiceActorNameTv.text = actorModel.name?.full
                        actorModel.languageV2?.let {
                            voiceActorLanguage.text = it
                        }
                        voiceActorLayoutContainer.setOnClickListener {
                            OpenStaffEvent(
                                actorModel.id,
                            ).postEvent
                        }
                    }

                }
            }
            1 -> {
                (binding as MangaCharacterPresenterLayoutBinding).apply {
                    mangaCharacterImageView.setImageURI(item.image?.image)
                    mangaCharacterNameTv.text = item.name?.full
                    mangaCharacterRoleTv.text = edge.role?.let { characterRoles[it] }
                    mangaCharacterContainer.setOnClickListener {
                        OpenCharacterEvent(
                            item.id
                        ).postEvent
                    }
                }
            }
        }
    }
}
