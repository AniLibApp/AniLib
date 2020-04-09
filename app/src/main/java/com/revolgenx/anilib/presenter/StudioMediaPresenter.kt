package com.revolgenx.anilib.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.event.BrowseMediaEvent
import com.revolgenx.anilib.event.meta.MediaBrowserMeta
import com.revolgenx.anilib.model.studio.StudioMediaModel
import com.revolgenx.anilib.util.naText
import kotlinx.android.synthetic.main.studio_media_presenter.view.*

class StudioMediaPresenter(context: Context) : Presenter<StudioMediaModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    private val statusColors by lazy {
        context.resources.getStringArray(R.array.status_color)
    }


    private val mediaFormats by lazy {
        context.resources.getStringArray(R.array.media_format)
    }

    private val mediaStatus by lazy {
        context.resources.getStringArray(R.array.media_status)
    }

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            LayoutInflater.from(context).inflate(
                R.layout.studio_media_presenter,
                parent,
                false
            )
        )
    }

    override fun onBind(page: Page, holder: Holder, element: Element<StudioMediaModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return

        holder.itemView.apply {
            studioMediaImageView.setImageURI(item.coverImage?.image)
            studioMediaTitleTv.text = item.title?.title(context)
            studioMediaRatingTv.text = item.averageScore?.toString().naText()
            studioMediaFormatYearTv.text = context.getString(R.string.media_format_year_s).format(
                item.format?.let { mediaFormats[it] }.naText(), item.seasonYear?.toString().naText()
            )
            studioMediaStatusTv.text = item.status?.let {
                studioMediaStatusTv.color = Color.parseColor(statusColors[it])
                mediaStatus[it]
            }.naText()
            setOnClickListener {
                BrowseMediaEvent(
                    MediaBrowserMeta(
                        item.mediaId,
                        item.type!!,
                        item.title!!.romaji!!,
                        item.coverImage!!.image,
                        item.bannerImage
                    ), holder.itemView.studioMediaImageView
                ).postEvent
            }
        }
    }

}