package com.revolgenx.anilib.studio.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.databinding.StudioMediaPresenterBinding
import com.revolgenx.anilib.common.event.OpenMediaInfoEvent
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.util.naText

class StudioMediaPresenter(context: Context) : BasePresenter<StudioMediaPresenterBinding, MediaModel>(context) {
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

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): StudioMediaPresenterBinding {
        return StudioMediaPresenterBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return

        holder.getBinding()?.apply {
            studioMediaImageView.setImageURI(item.coverImage?.image())
            studioMediaTitleTv.text = item.title?.title()
            studioMediaRatingTv.text = item.averageScore
            studioMediaFormatYearTv.text = context.getString(R.string.media_format_year_s).format(
                item.format?.let { mediaFormats[it] }.naText(), item.seasonYear?.toString().naText()
            )
            studioMediaStatusTv.text = item.status?.let {
                studioMediaStatusTv.color = Color.parseColor(statusColors[it])
                mediaStatus[it]
            }.naText()
            root.setOnClickListener {
                OpenMediaInfoEvent(
                    MediaInfoMeta(
                        item.id,
                        item.type!!,
                        item.title!!.romaji!!,
                        item.coverImage!!.image(),
                        item.coverImage!!.largeImage,
                        item.bannerImage
                    )
                ).postEvent
            }
        }
    }

}