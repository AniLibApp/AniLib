package com.revolgenx.anilib.ui.presenter.media

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.data.model.media_info.MediaStaffModel
import com.revolgenx.anilib.databinding.MediaStaffPresenterLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenStaffEvent
import com.revolgenx.anilib.ui.presenter.BasePresenter

class MediaStaffPresenter(context: Context) : BasePresenter<MediaStaffPresenterLayoutBinding, MediaStaffModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): MediaStaffPresenterLayoutBinding {
        return MediaStaffPresenterLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaStaffModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        holder.getBinding()?.apply {
            staffNameTv.text = item.name?.full
            staffRoleTv.text = item.role
            staffImageView.setImageURI(item.image?.image)
            root.setOnClickListener {
                OpenStaffEvent(
                    item.id!!,
                ).postEvent

            }
        }
    }
}
