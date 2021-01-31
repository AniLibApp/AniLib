package com.revolgenx.anilib.ui.presenter.media

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.data.model.MediaWatchModel
import com.revolgenx.anilib.databinding.MediaWatchPresenterBinding
import com.revolgenx.anilib.ui.presenter.BasePresenter
import com.revolgenx.anilib.util.openLink

class MediaWatchPresenter(context: Context) : BasePresenter<MediaWatchPresenterBinding, MediaWatchModel>(context) {

    override val elementTypes: Collection<Int>
        get() = listOf(0)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): MediaWatchPresenterBinding {
        return MediaWatchPresenterBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaWatchModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return

        holder.getBinding()?.apply {
            watchThumbnail.setImageURI(item.thumbnail)
            watchTitleTv.text = item.title
            watchFromTv.text = item.site
            root.setOnClickListener {
                context.openLink(item.url)
            }
        }
    }


}
