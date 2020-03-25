package com.revolgenx.anilib.adapter

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
import com.revolgenx.anilib.model.MediaRelationshipModel
import com.revolgenx.anilib.util.naText
import kotlinx.android.synthetic.main.browser_relationship_presenter_layout.view.*

class BrowserRelationshipPresenter(context: Context) : Presenter<MediaRelationshipModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    private val statusColors by lazy {
        context.resources.getStringArray(R.array.status_color)
    }


    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.browser_relationship_presenter_layout,
                parent,
                false
            )
        )
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaRelationshipModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return

        holder.itemView.apply {

            relationshipTitleTv.text = item.title!!.title(context)
            relationshipCoverImage.setImageURI(item.coverImageModel?.largeImage)
            relationshipMediaRatingTv.text = item.averageScore?.toString().naText()
            mediaSourceSeasonYearTv.text =
                context.getString(R.string.source_seasonyear_s).format(
                    item.relationshipType?.let { context.resources.getStringArray(R.array.media_relation)[it] }.naText()
                    , item.seasonYear?.toString().naText()
                )

            mediaFormatStatusTv.text = context.getString(R.string.format_status_s).format(
                item.format?.let { context.resources.getStringArray(R.array.media_format)[it] }.naText(),
                item.status?.let { context.resources.getStringArray(R.array.media_status)[it] }.naText()
            )
            setOnClickListener {
                BrowseMediaEvent(
                    MediaBrowserMeta(
                        item.mediaId,
                        item.type!!,
                        item.title!!.romaji!!,
                        item.coverImageModel!!.image,
                        item.bannerImage
                    ), relationshipCoverImage
                ).postEvent
            }

            item.status?.let {
                statusDivider.setBackgroundColor(Color.parseColor(statusColors[it]))
            }
        }

    }

}