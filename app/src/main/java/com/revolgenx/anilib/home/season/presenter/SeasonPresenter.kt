package com.revolgenx.anilib.home.season.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.entry.data.meta.EntryEditorMeta
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.databinding.SeasonPresenterLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenMediaInfoEvent
import com.revolgenx.anilib.infrastructure.event.OpenMediaListEditorEvent
import com.revolgenx.anilib.infrastructure.event.OpenSearchEvent
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.search.data.model.filter.SearchFilterModel
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.util.string

class SeasonPresenter(context: Context) :
    BasePresenter<SeasonPresenterLayoutBinding, MediaModel>(context) {

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

    private val textPrimary =
        DynamicTheme.getInstance().get().textPrimaryColor

    private val isLoggedIn by lazy {
        context.loggedIn()
    }


    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): SeasonPresenterLayoutBinding {
        return SeasonPresenterLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onBind(page: Page, holder: Holder, element: Element<MediaModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        val binding = holder.getBinding() ?: return
        binding.apply {
            mediaTitleTv.naText(item.title!!.title(context))
            coverImageIv.setImageURI(item.coverImage!!.image(context))
            if (item.type == MediaType.ANIME.ordinal) {
                mediaEpisodeTv.text =
                    context.string(R.string.ep_d_s).format(item.episodes.naText(), item.duration.naText())
            } else {
                mediaEpisodeTv.text =
                    context.string(R.string.chap_s).format(item.chapters.naText(), item.volumes.naText())
            }
            mediaStartDateTv.text =
                item.startDate?.date.naText() + " ~ " + item.endDate?.date.naText()
            mediaGenreLayout.addGenre(
                item.genres?.take(5)
            ) { genre ->
                OpenSearchEvent(SearchFilterModel(genre = genre)).postEvent
            }

            mediaRatingTv.text = item.averageScore

            mediaFormatTv.text = item.format?.let {
                mediaFormats[it]
            }.naText()

            mediaFormatTv.status = item.mediaListEntry?.status

            mediaStatusTv.naText(item.status?.let {
                mediaStatusTv.color = Color.parseColor(statusColors[it])
                mediaStatus[it]
            })

            mediaCardView.setOnClickListener {
                OpenMediaInfoEvent(
                    MediaInfoMeta(
                        item.id,
                        item.type!!,
                        item.title!!.romaji!!,
                        item.coverImage!!.image(context),
                        item.coverImage!!.largeImage,
                        item.bannerImage
                    )
                ).postEvent
            }

            root.setOnLongClickListener {
                if (isLoggedIn) {
                    OpenMediaListEditorEvent(
                        EntryEditorMeta(
                            item.id,
                            item.type!!,
                            item.title!!.title(context)!!,
                            item.coverImage!!.image(context),
                            item.bannerImage
                        )
                    ).postEvent
                } else {
                    context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
                }
                true
            }

            if (isLoggedIn) {
                entryProgressTv.visibility = View.VISIBLE
                entryProgressTv.compoundDrawablesRelative[0]?.setTint(textPrimary)
                entryProgressTv.text = context.getString(R.string.s_slash_s).format(
                    item.mediaListEntry?.progress?.toString().naText(),
                    when (item.type) {
                        MediaType.ANIME.ordinal -> {
                            item.episodes.naText()
                        }
                        else -> {
                            item.chapters.naText()
                        }

                    }
                )
            } else {
                entryProgressTv.visibility = View.GONE
            }

        }
    }

}