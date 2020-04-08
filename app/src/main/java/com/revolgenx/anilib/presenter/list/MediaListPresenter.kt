package com.revolgenx.anilib.presenter.list

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.model.list.MediaListModel
import com.revolgenx.anilib.util.naText
import kotlinx.android.synthetic.main.media_list_presenter_layout.view.*

class MediaListPresenter(context: Context) : Presenter<MediaListModel>(context) {

    override val elementTypes: Collection<Int>
        get() = listOf(0)

    private val mediaFormats by lazy {
        context.resources.getStringArray(R.array.media_format)
    }

    private val mediaStatus by lazy {
        context.resources.getStringArray(R.array.media_status)
    }
    private val statusColors by lazy {
        context.resources.getStringArray(R.array.status_color)
    }
    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            getLayoutInflater().inflate(
                R.layout.media_list_presenter_layout,
                parent,
                false
            )
        )
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaListModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        holder.itemView.apply {
            mediaListTitleTv.text = item.title?.userPreferred
            mediaListCoverImageView.setImageURI(item.coverImage?.large)
            mediaListStatusTv.text = item.status?.let {
                mediaListStatusTv.color = Color.parseColor(statusColors[it])
                mediaStatus[it]
            }.naText()
        }
    }

}
