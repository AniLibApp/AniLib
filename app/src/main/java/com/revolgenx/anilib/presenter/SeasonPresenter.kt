package com.revolgenx.anilib.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.event.BrowseGenreEvent
import com.revolgenx.anilib.event.BrowseMediaEvent
import com.revolgenx.anilib.event.ListEditorEvent
import com.revolgenx.anilib.event.meta.MediaBrowserMeta
import com.revolgenx.anilib.event.meta.ListEditorMeta
import com.revolgenx.anilib.model.CommonMediaModel
import com.revolgenx.anilib.model.search.filter.MediaBrowseFilterModel
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.util.makeSnakeBar
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.util.string
import kotlinx.android.synthetic.main.season_presenter_layout.view.*

class SeasonPresenter(context: Context) : Presenter<CommonMediaModel>(context) {

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
            LayoutInflater.from(parent.context).inflate(
                R.layout.season_presenter_layout,
                parent,
                false
            )
        )
    }


    override fun onBind(page: Page, holder: Holder, element: Element<CommonMediaModel>) {
        super.onBind(page, holder, element)
        val data = element.data!!

        holder.itemView.apply {
            mediaTitleTv.naText(data.title!!.title(context))
            coverImageIv.setImageURI(data.coverImage!!.image)
            if (data.type == MediaType.ANIME.ordinal) {
                mediaEpisodeTv.text =
                    string(R.string.ep_d_s).format(data.episodes.naText(), data.duration.naText())
            } else {
                mediaEpisodeTv.text =
                    string(R.string.chap_s).format(data.chapters.naText(), data.volumes.naText())
            }
            mediaStartDateTv.naText(data.endDate?.date ?: data.startDate?.date)
            mediaGenreLayout.addGenre(
                data.genres?.take(5)
            ) { genre ->
                BrowseGenreEvent(MediaBrowseFilterModel().also {
                    it.genre = listOf(genre.trim())
                }).postEvent
            }

            mediaRatingTv.text = data.averageScore?.toString().naText()

            mediaFormatTv.text = data.format?.let {
                mediaFormats[it]
            }.naText()

            mediaStatusTv.naText(data.status?.let {
                mediaStatusTv.color = Color.parseColor(statusColors[it])
                mediaStatus[it]
            })

            mediaCardView.setOnClickListener {
                BrowseMediaEvent(
                    MediaBrowserMeta(
                        data.mediaId,
                        data.type!!,
                        data.title!!.romaji!!,
                        data.coverImage!!.image,
                        data.bannerImage
                    ), holder.itemView.coverImageIv
                ).postEvent
            }

            bookmarkIv.setOnClickListener {
                if (context.loggedIn()) {
                    ListEditorEvent(
                        ListEditorMeta(
                            data.mediaId,
                            data.type!!,
                            data.title!!.title(context)!!,
                            data.coverImage!!.image,
                            data.bannerImage
                        ), holder.itemView.coverImageIv
                    ).postEvent
                } else {
                    (parent as View).makeSnakeBar(R.string.please_log_in)
                }
            }
        }
    }

}