package com.revolgenx.anilib.media.presenter

import android.content.Context
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.media.data.model.MediaMetaCollection
import com.revolgenx.anilib.databinding.MediaMetaPresenterLayoutBinding
import com.revolgenx.anilib.common.presenter.BasePresenter

class MediaMetaPresenter(context: Context) : BasePresenter<MediaMetaPresenterLayoutBinding, MediaMetaCollection>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): MediaMetaPresenterLayoutBinding {
        return MediaMetaPresenterLayoutBinding.inflate(getLayoutInflater(), parent, false)
    }


    override fun onBind(page: Page, holder: Holder, element: Element<MediaMetaCollection>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        val binding:MediaMetaPresenterLayoutBinding = holder.getBinding() ?: return
        holder.itemView.apply {
            binding.header.title = item.header
            binding.header.subtitleView?.movementMethod = LinkMovementMethod.getInstance()
            binding.header.subtitle = item.subTitle ?: item.subTitleSpannable
        }
    }
}