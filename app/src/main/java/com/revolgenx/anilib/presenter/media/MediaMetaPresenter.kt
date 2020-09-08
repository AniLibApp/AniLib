package com.revolgenx.anilib.presenter.media

import android.content.Context
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.model.MediaMetaCollection
import kotlinx.android.synthetic.main.media_meta_presenter_layout.view.*

class MediaMetaPresenter(context: Context) : Presenter<MediaMetaCollection>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)


    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            LayoutInflater.from(context).inflate(
                R.layout.media_meta_presenter_layout,
                parent,
                false
            )
        )
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaMetaCollection>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        holder.itemView.apply {
            header.title = item.header
            header.subtitleView.movementMethod = LinkMovementMethod.getInstance()
            header.subtitle = item.subTitle ?: item.subTitleSpannable
        }
    }
}