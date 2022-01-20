package com.revolgenx.anilib.media.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.databinding.MediaStaffPresenterLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenStaffEvent
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.staff.data.model.StaffEdgeModel

class MediaStaffPresenter(context: Context) :
    BasePresenter<MediaStaffPresenterLayoutBinding, StaffEdgeModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): MediaStaffPresenterLayoutBinding {
        return MediaStaffPresenterLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<StaffEdgeModel>) {
        super.onBind(page, holder, element)
        val data = element.data ?: return
        val item = data.node ?: return
        holder.getBinding()?.apply {
            staffNameTv.text = item.name?.full
            staffRoleTv.text = data.role
            staffImageView.setImageURI(item.image?.image)
            root.setOnClickListener {
                OpenStaffEvent(
                    item.id,
                ).postEvent

            }
        }
    }
}
