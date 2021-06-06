package com.revolgenx.anilib.ui.presenter.media

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.model.media_info.MediaCharacterModel
import com.revolgenx.anilib.databinding.AnimeCharacterPresenterLayoutBinding
import com.revolgenx.anilib.databinding.MangaCharacterPresenterLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenCharacterEvent
import com.revolgenx.anilib.infrastructure.event.OpenStaffEvent
import com.revolgenx.anilib.ui.presenter.BasePresenter

class MediaCharacterPresenter(context: Context) :
    BasePresenter<ViewBinding, MediaCharacterModel>(context) {

    override val elementTypes: Collection<Int>
        get() = listOf(0, 1)

    private val characterRoles by lazy {
        context.resources.getStringArray(R.array.character_role)
    }
    private val staffLanguages by lazy {
        context.resources.getStringArray(R.array.staff_language)
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

    override fun onBind(page: Page, holder: Holder, element: Element<MediaCharacterModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        val binding = holder.getBinding() ?: return
        when (holder.elementType) {
            0 -> {
                (binding as AnimeCharacterPresenterLayoutBinding).apply {
                    characterImageView.setImageURI(item.image?.image)
                    characterNameTv.text = item.name?.full
                    characterRoleTv.text = item.role?.let { characterRoles[it] }
                    characterLayoutContainer.setOnClickListener {
                        OpenCharacterEvent(
                            item.id!!
                        ).postEvent
                    }

                    item.voiceActor?.let { actorModel ->
                        voiceActorLayoutContainer
                        voiceActorImageView.setImageURI(actorModel.voiceActorImageModel?.image)
                        voiceActorNameTv.text = actorModel.name
                        voiceActorLanguage.text =
                            actorModel.language?.let { staffLanguages[it] }

                        voiceActorLayoutContainer.setOnClickListener {
                            OpenStaffEvent(
                                actorModel.actorId!!,
                            ).postEvent
                        }
                    }

                }
            }
            1 -> {
                (binding as MangaCharacterPresenterLayoutBinding).apply {
                    mangaCharacterImageView.setImageURI(item.image?.image)
                    mangaCharacterNameTv.text = item.name?.full
                    mangaCharacterRoleTv.text = item.role?.let { characterRoles[it] }
                    mangaCharacterContainer.setOnClickListener {
                        OpenCharacterEvent(
                            item.id!!
                        ).postEvent
                    }
                }
            }
        }
    }
}
