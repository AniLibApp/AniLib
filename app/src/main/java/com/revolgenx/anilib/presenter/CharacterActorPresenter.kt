package com.revolgenx.anilib.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.model.VoiceActorModel
import kotlinx.android.synthetic.main.character_actor_presenter_layout.view.*

class CharacterActorPresenter(context: Context) : Presenter<VoiceActorModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.character_actor_presenter_layout,
                parent,
                false
            )
        )
    }

    private val language by lazy {
        context.resources.getStringArray(R.array.staff_language)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<VoiceActorModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return

        holder.itemView.apply {
            actorImageView.setImageURI(item.voiceActorImageModel?.image)
            actorNameTv.text = item.name
            item.language?.let {
                actorLanguageTv.text = language[it]
            }
        }
    }
}
