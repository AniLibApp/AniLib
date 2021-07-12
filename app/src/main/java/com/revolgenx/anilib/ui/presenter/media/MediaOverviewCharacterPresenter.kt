package com.revolgenx.anilib.ui.presenter.media

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.model.media_info.MediaCharacterModel
import com.revolgenx.anilib.databinding.MediaOverviewCharacterPresenterLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenCharacterEvent
import com.revolgenx.anilib.ui.presenter.BasePresenter

class MediaOverviewCharacterPresenter(context: Context) :
    BasePresenter<MediaOverviewCharacterPresenterLayoutBinding, MediaCharacterModel>(context) {

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


    override fun onBind(page: Page, holder: Holder, element: Element<MediaCharacterModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        holder.getBinding()?.apply {
            characterIv.setImageURI(item.image?.image)
            characterNameTv.text = item.name?.full
            characterRoleTv.text = characterRoles[item.role!!]

            root.setOnClickListener {
                OpenCharacterEvent(
                        item.id!!
                ).postEvent
            }
        }
    }
}