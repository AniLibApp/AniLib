package com.revolgenx.anilib.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.event.BrowseGenreEvent
import com.revolgenx.anilib.event.BrowseMediaEvent
import com.revolgenx.anilib.event.ListEditorEvent
import com.revolgenx.anilib.event.meta.BrowseMediaMeta
import com.revolgenx.anilib.event.meta.ListEditorMeta
import com.revolgenx.anilib.model.CommonMediaModel
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.type.MediaFormat
import com.revolgenx.anilib.type.MediaStatus
import com.revolgenx.anilib.util.makeSnakeBar
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.util.string
import kotlinx.android.synthetic.main.media_list_presenter_layout.view.*

class MediaPresenter(context: Context) : Presenter<CommonMediaModel>(context) {

    override val elementTypes: Collection<Int>
        get() = listOf(0)


    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.media_list_presenter_layout,
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
            mediaEpisodeTv.text =
                string(R.string.ep_s).format(data.episodes!!.naText(), data.duration!!.naText())
            mediaStartDateTv.naText(data.endDate?.date ?: data.startDate?.date)
            mediaGenreLayout.addGenre(
                data.genres?.take(5)
            ) {
                BrowseGenreEvent(it).postEvent
            }

            mediaRatingTv.text = data.averageScore?.div(10f)?.let {
                String.format("%.1f", it)
            } ?: "?"

            mediaFormatTv.text = when (data.format) {
                MediaFormat.TV.ordinal -> string(R.string.tv)
                MediaFormat.TV_SHORT.ordinal -> string(R.string.tv_short)
                MediaFormat.MOVIE.ordinal -> string(R.string.movie)
                MediaFormat.MANGA.ordinal -> string(R.string.manga)
                MediaFormat.MUSIC.ordinal -> string(R.string.music)
                MediaFormat.NOVEL.ordinal -> string(R.string.novel)
                MediaFormat.ONA.ordinal -> string(R.string.ona)
                MediaFormat.OVA.ordinal -> string(R.string.ova)
                MediaFormat.ONE_SHOT.ordinal -> string(R.string.one_shot)
                MediaFormat.SPECIAL.ordinal -> string(R.string.special)
                else -> string(R.string.unknown)
            }

            when (data.status) {
                MediaStatus.RELEASING.ordinal -> {
                    mediaStatusTv.color = ContextCompat.getColor(context, R.color.colorReleasing)
                    mediaStatusTv.text = context.getString(R.string.releasing)
                }
                MediaStatus.FINISHED.ordinal -> {
                    mediaStatusTv.color = ContextCompat.getColor(context, R.color.colorFinished)
                    mediaStatusTv.text = context.getString(R.string.finished)
                };
                MediaStatus.NOT_YET_RELEASED.ordinal -> {
                    mediaStatusTv.color = ContextCompat.getColor(context, R.color.colorNotReleased)
                    mediaStatusTv.text = context.getString(R.string.not_released)
                }
                MediaStatus.CANCELLED.ordinal -> {
                    mediaStatusTv.color = ContextCompat.getColor(context, R.color.colorCancelled)
                    mediaStatusTv.text = context.getString(R.string.cancelled)
                }
            }


            mediaCardView.setOnClickListener {
                BrowseMediaEvent(
                    BrowseMediaMeta(
                        data.id,
                        data.type!!,
                        data.title!!.title(context)!!,
                        data.coverImage!!.image,
                        data.bannerImage
                    ), holder.itemView.coverImageIv
                ).postEvent
            }


            bookmarkIv.setOnClickListener {
                if (context.loggedIn()) {
                    ListEditorEvent(
                        ListEditorMeta(
                            data.id,
                            data.type!!,
                            data.title!!.title(context)!!,
                            data.coverImage!!.image,
                            data.bannerImage
                        ), holder.itemView.coverImageIv
                    ).postEvent
                } else {
                    (parent as View).makeSnakeBar(string(R.string.please_log_in))
                }
            }

        }
    }

}