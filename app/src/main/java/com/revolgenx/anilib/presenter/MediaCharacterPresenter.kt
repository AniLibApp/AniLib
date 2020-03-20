package com.revolgenx.anilib.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.model.MediaCharacterModel
import kotlinx.android.synthetic.main.anime_character_presenter_layout.view.*

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
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                when (elementType) {
                    1 -> R.layout.manga_character_presenter_layout
                    else ->
                        R.layout.anime_character_presenter_layout
                },
                parent,
                false
            )
        )
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaCharacterModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        when (holder.elementType) {
            0 -> {
                holder.itemView.apply {
                    characterImageView.setImageURI(item.characterImageModel?.image)
                    characterNameTv.text = item.name
                    characterRoleTv.text = item.role?.let { characterRoles[it] } ?: ""
                    voiceActorImageView.setImageURI(item.voiceActor?.voiceActorImageModel?.image)
                    voiceActorNameTv.text = item.voiceActor?.name
                    voiceActorLanguage.text =
                        item.voiceActor?.language?.let { staffLanguages[it] } ?: ""

                    characterFavIv.setImageResource(R.drawable.ic_favorite)
                }
            }
            1 -> {

            }
        }
    }
}
