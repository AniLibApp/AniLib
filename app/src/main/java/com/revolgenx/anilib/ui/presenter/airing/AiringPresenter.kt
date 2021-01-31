package com.revolgenx.anilib.ui.presenter.airing

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary
import com.revolgenx.anilib.common.preference.getAiringDisplayMode
import com.revolgenx.anilib.infrastructure.event.BrowseGenreEvent
import com.revolgenx.anilib.infrastructure.event.BrowseMediaEvent
import com.revolgenx.anilib.infrastructure.event.ListEditorEvent
import com.revolgenx.anilib.data.meta.ListEditorMeta
import com.revolgenx.anilib.data.meta.MediaBrowserMeta
import com.revolgenx.anilib.data.model.airing.AiringMediaModel
import com.revolgenx.anilib.data.model.search.filter.MediaSearchFilterModel
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.constant.AiringListDisplayMode
import com.revolgenx.anilib.databinding.AiringCompactPresenterLayoutBinding
import com.revolgenx.anilib.databinding.AiringPresenterLayoutBinding
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.ui.presenter.BasePresenter
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.util.string

class AiringPresenter(context: Context) :
    BasePresenter<ViewBinding, AiringMediaModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): ViewBinding {
        return when (displayMode) {
            AiringListDisplayMode.NORMAL -> {
                AiringPresenterLayoutBinding.inflate(inflater, parent, false)
            }
            else -> {
                AiringCompactPresenterLayoutBinding.inflate(inflater, parent, false)
            }
        }
    }

    private val statusColors by lazy {
        context.resources.getStringArray(R.array.status_color)
    }

    private val mediaFormats by lazy {
        context.resources.getStringArray(R.array.media_format)
    }

    private val mediaStatus by lazy {
        context.resources.getStringArray(R.array.media_status)
    }


    private val displayMode = getAiringDisplayMode()


    override fun onBind(page: Page, holder: Holder, element: Element<AiringMediaModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return

        when (val binding = holder.getBinding()) {
            is AiringPresenterLayoutBinding -> {
                binding.apply {
                    mediaTitleTv.naText(item.title!!.title(context))
                    coverImageIv.setImageURI(item.coverImage!!.image(context))

                    if (item.type == MediaType.ANIME.ordinal) {
                        mediaEpisodeTv.text =
                            context.string(R.string.ep_d_s)
                                .format(item.episodes.naText(), item.duration.naText())
                    } else {
                        mediaEpisodeTv.text =
                            context.string(R.string.chap_s)
                                .format(item.chapters.naText(), item.volumes.naText())
                    }
                    mediaStartDateTv.text = context.getString(R.string.startdate_format)
                        .format(item.startDate?.date.naText(), item.endDate?.date?.naText())

                    mediaGenreLayout.addGenre(
                        item.genres?.take(5)
                    ) { genre ->
                        BrowseGenreEvent(MediaSearchFilterModel().also {
                            it.genre = listOf(genre.trim())
                        }).postEvent
                    }

                    mediaRatingTv.text = item.averageScore?.toString().naText()

                    mediaFormatTv.text = item.format?.let {
                        mediaFormats[it]
                    }.naText()

                    mediaFormatTv.status = item.mediaEntryListModel?.status


                    mediaStatusTv.naText(item.status?.let {
                        mediaStatusTv.color = Color.parseColor(statusColors[it])
                        mediaStatus[it]
                    })

                    airingCardView.setOnClickListener {
                        BrowseMediaEvent(
                            MediaBrowserMeta(
                                item.mediaId,
                                item.type!!,
                                item.title!!.romaji!!,
                                item.coverImage!!.image(context),
                                item.coverImage!!.largeImage,
                                item.bannerImage
                            ), coverImageIv
                        ).postEvent
                    }

                    bookmarkIv.setOnClickListener {
                        if (context.loggedIn()) {
                            ListEditorEvent(
                                ListEditorMeta(
                                    item.mediaId,
                                    item.type!!,
                                    item.title!!.title(context)!!,
                                    item.coverImage!!.image(context),
                                    item.bannerImage
                                ), coverImageIv
                            ).postEvent
                        } else {
                            context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
                        }
                    }

                    airingTimeTv.setAiringText(item.airingTimeModel)


                    if (context.loggedIn()) {
                        entryProgressTv.visibility = View.VISIBLE
                        entryProgressTv.compoundDrawablesRelative[0]?.setTint(
                            dynamicTextColorPrimary)
                        entryProgressTv.text = context.getString(R.string.s_slash_s).format(
                            item.mediaEntryListModel?.progress?.toString().naText(),
                            when (item.type) {
                                MediaType.ANIME.ordinal -> {
                                    item.episodes.naText()
                                }
                                else -> {
                                    item.chapters.naText()
                                }

                            }
                        )

                        item.mediaEntryListModel?.let {
                            it.progress?.let {
                                bookmarkIv.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        context,
                                        R.drawable.ic_bookmark_filled
                                    )
                                )
                            } ?: let {
                                bookmarkIv.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        context,
                                        R.drawable.ic_bookmark
                                    )
                                )
                            }
                        }
                    } else {
                        entryProgressTv.visibility = View.GONE
                    }
                }
            }
            is AiringCompactPresenterLayoutBinding -> {
                binding.apply {
                    mediaTitleTv.naText(item.title!!.title(context))
                    coverImageIv.setImageURI(item.coverImage!!.image(context))

                    if (item.type == MediaType.ANIME.ordinal) {
                        mediaEpisodeTv.text =
                            context.string(R.string.ep_d_s)
                                .format(item.episodes.naText(), item.duration.naText())
                    } else {
                        mediaEpisodeTv.text =
                            context.string(R.string.chap_s)
                                .format(item.chapters.naText(), item.volumes.naText())
                    }

                    mediaRatingTv.text = item.averageScore?.toString().naText()

                    mediaFormatTv.text = item.format?.let {
                        mediaFormats[it]
                    }.naText()

                    mediaFormatTv.status = item.mediaEntryListModel?.status


                    root.setOnClickListener {
                        BrowseMediaEvent(
                            MediaBrowserMeta(
                                item.mediaId,
                                item.type!!,
                                item.title!!.romaji!!,
                                item.coverImage!!.image(context),
                                item.coverImage!!.largeImage,
                                item.bannerImage
                            ), coverImageIv
                        ).postEvent
                    }

                    root.setOnLongClickListener {
                        if (context.loggedIn()) {
                            ListEditorEvent(
                                ListEditorMeta(
                                    item.mediaId,
                                    item.type!!,
                                    item.title!!.title(context)!!,
                                    item.coverImage!!.image(context),
                                    item.bannerImage
                                ), coverImageIv
                            ).postEvent
                        } else {
                            context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
                        }
                        true
                    }

                    airingTimeTv.text = context.getString(R.string.episode_format_s).format(
                        item.airingTimeModel?.episode,
                        item.airingTimeModel?.airingAt!!.airingTime
                    )

                    if (context.loggedIn()) {
                        entryProgressTv.visibility = View.VISIBLE
                        entryProgressTv.compoundDrawablesRelative[0]?.setTint(dynamicTextColorPrimary)
                        entryProgressTv.text = context.getString(R.string.s_slash_s).format(
                            item.mediaEntryListModel?.progress?.toString().naText(),
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

    }
}
