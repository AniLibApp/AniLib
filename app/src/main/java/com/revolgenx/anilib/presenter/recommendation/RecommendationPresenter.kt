package com.revolgenx.anilib.presenter.recommendation

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.event.BrowseMediaEvent
import com.revolgenx.anilib.event.ListEditorEvent
import com.revolgenx.anilib.event.meta.ListEditorMeta
import com.revolgenx.anilib.event.meta.MediaBrowserMeta
import com.revolgenx.anilib.model.recommendation.RecommendationModel
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.util.makeSnakeBar
import com.revolgenx.anilib.util.naText
import kotlinx.android.synthetic.main.recommendation_presenter_layout.view.*

class RecommendationPresenter(context: Context) : Presenter<RecommendationModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    private val statusColors by lazy {
        context.resources.getStringArray(R.array.status_color)
    }

    private val mediaStatus by lazy {
        context.resources.getStringArray(R.array.media_status)
    }

    private val mediaFormats by lazy {
        context.resources.getStringArray(R.array.media_format)
    }

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            LayoutInflater.from(context).inflate(
                R.layout.recommendation_presenter_layout,
                parent,
                false
            )
        )
    }

    override fun onBind(page: Page, holder: Holder, element: Element<RecommendationModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        holder.itemView.apply {
            item.recommendationFrom?.let { from ->
                recommendedFromTitleTv.text = from.title?.title(context)
                recommendedFromImageView.setImageURI(from.coverImage?.image)
                recommendedFromRatingTv.text = from.averageScore?.toString().naText()
                recommendedFromStatusTv.text = from.status?.let {
                    recommendedFromStatusTv.color = Color.parseColor(statusColors[it])
                    mediaStatus[it]
                }.naText()
                recommendedFromFormatYearTv.text =
                    context.getString(R.string.media_format_year_s).format(
                        from.format?.let { mediaFormats[it] }.naText(),
                        from.seasonYear?.toString().naText()
                    )

                recommendedFromMediaContainer.setOnClickListener {
                    BrowseMediaEvent(
                        MediaBrowserMeta(
                            from.mediaId,
                            from.type!!,
                            from.title!!.romaji!!,
                            from.coverImage!!.image,
                            from.bannerImage
                        ), recommendedFromImageView
                    ).postEvent
                }

                recommendedFromMediaContainer.setOnLongClickListener {
                    if (context.loggedIn()) {
                        ListEditorEvent(
                            ListEditorMeta(
                                from.mediaId,
                                from.type!!,
                                from.title!!.title(context)!!,
                                from.coverImage!!.image,
                                from.bannerImage
                            ), recommendedFromImageView
                        ).postEvent
                    } else {
                        (parent as View).makeSnakeBar(R.string.please_log_in)
                    }
                    true
                }
            }

            item.recommended?.let { rec ->
                recommendedTitleTv.text = rec.title?.title(context)
                recommendedImageView.setImageURI(rec.coverImage?.image)
                recommendedMediaRatingTv.text = rec.averageScore?.toString().naText()
                recommendedStatusTv.text = rec.status?.let {
                    recommendedStatusTv.color = Color.parseColor(statusColors[it])
                    mediaStatus[it]
                }.naText()
                recommendedFormatYearTv.text =
                    context.getString(R.string.media_format_year_s).format(
                        rec.format?.let { mediaFormats[it] }.naText(),
                        rec.seasonYear?.toString().naText()
                    )

                recommendedContainer.setOnClickListener {
                    BrowseMediaEvent(
                        MediaBrowserMeta(
                            rec.mediaId,
                            rec.type!!,
                            rec.title!!.romaji!!,
                            rec.coverImage!!.image,
                            rec.bannerImage
                        ), recommendedImageView
                    ).postEvent
                }

                recommendedContainer.setOnLongClickListener {
                    if (context.loggedIn()) {
                        ListEditorEvent(
                            ListEditorMeta(
                                rec.mediaId,
                                rec.type!!,
                                rec.title!!.title(context)!!,
                                rec.coverImage!!.image,
                                rec.bannerImage
                            ), recommendedImageView
                        ).postEvent
                    } else {
                        (parent as View).makeSnakeBar(R.string.please_log_in)
                    }
                    true
                }
            }


        }
    }
}
