package com.revolgenx.anilib.ui.presenter.character

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.model.VoiceActorModel
import com.revolgenx.anilib.databinding.CharacterActorPresenterLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenStaffEvent
import com.revolgenx.anilib.ui.presenter.BasePresenter

class CharacterActorPresenter(context: Context) : BasePresenter<CharacterActorPresenterLayoutBinding, VoiceActorModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): CharacterActorPresenterLayoutBinding {
        return CharacterActorPresenterLayoutBinding.inflate(inflater, parent, false)
    }


    private val language by lazy {
        context.resources.getStringArray(R.array.staff_language)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<VoiceActorModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        val binding = holder.getBinding()?:return
        binding.apply {
            actorImageView.setImageURI(item.voiceActorImageModel?.image)
            actorNameTv.text = item.name
            item.language?.let {
                actorLanguageTv.text = language[it]
            }
            root.setOnClickListener {
                OpenStaffEvent(
                    item.actorId!!,
                ).postEvent
            }
        }
    }
}
