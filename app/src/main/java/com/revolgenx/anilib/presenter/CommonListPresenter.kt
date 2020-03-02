package com.revolgenx.anilib.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.model.CommonMediaModel
import kotlinx.android.synthetic.main.common_list_presenter_layout.view.*

class CommonListPresenter(context: Context) :
    Presenter<CommonMediaModel>(context) {

    override val elementTypes: Collection<Int>
        get() = listOf(0)


    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.common_list_presenter_layout,
                parent,
                false
            )
        )
    }

    override fun onBind(page: Page, holder: Holder, element: Element<CommonMediaModel>) {
        super.onBind(page, holder, element)
        holder.itemView.animeTitleTv.text = element.data!!.title!!.title(context)
        holder.itemView.coverImageIv.setImageURI(element.data!!.coverImage!!.large)
    }
}