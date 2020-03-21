package com.revolgenx.anilib.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.GridLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.flexbox.FlexboxLayoutManager
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.model.MediaWatchModel
import com.revolgenx.anilib.util.dp
import com.revolgenx.anilib.util.openLink
import kotlinx.android.synthetic.main.media_watch_presenter.view.*

class MediaWatchPresenter(context: Context) : Presenter<MediaWatchModel>(context) {

    override val elementTypes: Collection<Int>
        get() = listOf(0)

    private val crunchyDrawable by lazy {
        ContextCompat.getDrawable(context, R.drawable.ic_crunchyroll_logo)
    }

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.media_watch_presenter,
                parent,
                false
            )
        )
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaWatchModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return

        holder.itemView.apply {
            watchThumbnail.setImageURI(item.thumbnail)
            watchTitleTv.text = item.title
            watchFromTv.text = item.site
            setOnClickListener {
                context.openLink(item.url)
            }
        }
    }


}
