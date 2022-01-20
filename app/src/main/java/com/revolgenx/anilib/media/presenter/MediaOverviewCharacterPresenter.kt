package com.revolgenx.anilib.media.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.character.data.model.CharacterEdgeModel
import com.revolgenx.anilib.media.data.model.MediaEdgeModel
import com.revolgenx.anilib.databinding.MediaOverviewCharacterPresenterLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenCharacterEvent
import com.revolgenx.anilib.common.presenter.BasePresenter

class MediaOverviewCharacterPresenter(context: Context) :
    BasePresenter<MediaOverviewCharacterPresenterLayoutBinding, CharacterEdgeModel>(context) {

    private val characterRoles by lazy {
        context.resources.getStringArray(R.array.character_role)
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): MediaOverviewCharacterPresenterLayoutBinding {
        return MediaOverviewCharacterPresenterLayoutBinding.inflate(inflater, parent, false)
    }

    override val elementTypes: Collection<Int> = listOf(0)


    override fun onBind(page: Page, holder: Holder, element: Element<CharacterEdgeModel>) {
        super.onBind(page, holder, element)
        val data = element.data ?: return
        val item = data.node ?: return
        holder.getBinding()?.apply {
            characterIv.setImageURI(item.image?.image)
            characterNameTv.text = item.name?.full
            characterRoleTv.text = characterRoles[data.role!!]

            root.setOnClickListener {
                OpenCharacterEvent(
                        item.id
                ).postEvent
            }
        }
    }
}