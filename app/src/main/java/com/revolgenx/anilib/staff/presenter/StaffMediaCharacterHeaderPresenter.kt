package com.revolgenx.anilib.staff.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.extensions.HeaderSource
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.event.OpenCharacterEvent
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.databinding.StaffMediaCharacterHeaderPresenterLayoutBinding
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.util.naText

class StaffMediaCharacterHeaderPresenter(context: Context) :
    BasePresenter<StaffMediaCharacterHeaderPresenterLayoutBinding, HeaderSource.Data<MediaModel, String>>(context) {
    override val elementTypes: Collection<Int> = listOf(HeaderSource.ELEMENT_TYPE)

    private val characterRoles by lazy {
        context.resources.getStringArray(R.array.character_role)
    }

    override fun bindView(inflater: LayoutInflater, parent: ViewGroup?, elementType: Int): StaffMediaCharacterHeaderPresenterLayoutBinding {
        return StaffMediaCharacterHeaderPresenterLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<HeaderSource.Data<MediaModel, String>>) {
        super.onBind(page, holder, element)
        val item = element.data?.anchor ?: return
        val character = item.character ?: return
        val binding = holder.getBinding() ?: return
        binding.apply {
            characterIv.setImageURI(character.image?.image)
            characterNameTv.text = character.name?.full
            characterRoleTv.text = item.characterRole?.let { characterRoles.getOrNull(it) }.naText()
            this.root.setOnClickListener {
                OpenCharacterEvent(character.id).postEvent
            }
        }
    }
}