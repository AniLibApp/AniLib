package com.revolgenx.anilib.ui.presenter.media

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.infrastructure.event.BrowseStaffEvent
import com.revolgenx.anilib.data.meta.StaffMeta
import com.revolgenx.anilib.data.model.MediaStaffModel
import kotlinx.android.synthetic.main.media_staff_presenter_layout.view.*

class MediaStaffPresenter(context: Context) : Presenter<MediaStaffModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.media_staff_presenter_layout,
                parent,
                false
            )
        )
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaStaffModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        holder.itemView.apply {
            staffNameTv.text = item.name
            staffRoleTv.text = item.role
            staffImageView.setImageURI(item.staffImage?.image)
            setOnClickListener {
                BrowseStaffEvent(StaffMeta(item.staffId ?: -1, item.staffImage?.image)).postEvent
            }
        }
    }
}