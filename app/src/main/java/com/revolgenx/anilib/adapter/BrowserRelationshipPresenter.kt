package com.revolgenx.anilib.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.event.BrowseMediaEvent
import com.revolgenx.anilib.event.meta.MediaBrowserMeta
import com.revolgenx.anilib.model.MediaRelationshipModel
import com.revolgenx.anilib.type.MediaStatus
import com.revolgenx.anilib.util.getAverageScore
import com.revolgenx.anilib.util.naText
import kotlinx.android.synthetic.main.browser_relationship_layout.view.*

class BrowserRelationshipPresenter(context: Context) : Presenter<MediaRelationshipModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)


    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.browser_relationship_layout,
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
            relationshipMediaRatingTv.text = getAverageScore(item.averageScore)
            mediaSourceSeasonYearTv.text =
                context.getString(R.string.source_seasonyear_s).format(
                    naText(item.relationshipType?.let { context.resources.getStringArray(R.array.media_relation)[it] })
                    , naText(item.seasonYear?.toString())
                )

            mediaFormatStatusTv.text = context.getString(R.string.format_status_s).format(
                naText(item.format?.let { context.resources.getStringArray(R.array.media_format)[it] })
                ,
                naText(item.status?.let { context.resources.getStringArray(R.array.media_status)[it] })
            )

            val statusColor = when (item.status) {
                MediaStatus.RELEASING.ordinal -> {
                    ContextCompat.getColor(context!!, R.color.colorReleasing)
                }
                MediaStatus.FINISHED.ordinal -> {
                    ContextCompat.getColor(context!!, R.color.colorFinished)
                };
                MediaStatus.NOT_YET_RELEASED.ordinal -> {
                    ContextCompat.getColor(context!!, R.color.colorNotReleased)
                }
                MediaStatus.CANCELLED.ordinal -> {
                    ContextCompat.getColor(context!!, R.color.colorCancelled)
                }
                else -> {
                    ContextCompat.getColor(context!!, R.color.colorUnknown)
                }
            }

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

            statusDivider.setBackgroundColor(statusColor)
        }

    }

}