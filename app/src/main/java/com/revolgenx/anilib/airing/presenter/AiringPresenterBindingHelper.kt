package com.revolgenx.anilib.airing.presenter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import com.revolgenx.anilib.R
import com.revolgenx.anilib.airing.data.model.AiringScheduleModel
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.databinding.AiringCompactPresenterLayoutBinding
import com.revolgenx.anilib.databinding.AiringMinimalListPresenterLayoutBinding
import com.revolgenx.anilib.databinding.AiringPresenterLayoutBinding
import com.revolgenx.anilib.common.event.OpenMediaInfoEvent
import com.revolgenx.anilib.common.event.OpenMediaListEditorEvent
import com.revolgenx.anilib.common.event.OpenSearchEvent
import com.revolgenx.anilib.search.data.model.filter.SearchFilterModel
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.util.loginContinue
import com.revolgenx.anilib.util.naText

object AiringPresenterBindingHelper {
    fun AiringPresenterLayoutBinding.bindPresenter(
        context: Context,
        item: AiringScheduleModel,
        vararg misc: Array<String> //formats status statuscolor
    ) {
        val media = item.media ?: return
        mediaTitleTv.naText(media.title!!.title(context))
        coverImageIv.setImageURI(media.coverImage!!.image(context))

        if (media.type == MediaType.ANIME.ordinal) {
            mediaEpisodeTv.text =
                context.getString(R.string.ep_d_s)
                    .format(media.episodes.naText(), media.duration.naText())
        } else {
            mediaEpisodeTv.text =
                context.getString(R.string.chap_s)
                    .format(media.chapters.naText(), media.volumes.naText())
        }
        mediaStartDateTv.text = context.getString(R.string.startdate_format)
            .format(media.startDate?.date.naText(), media.endDate?.date.naText())

        mediaGenreLayout.addGenre(
            media.genres?.take(5)
        ) { genre ->
            OpenSearchEvent(SearchFilterModel(genre = genre)).postEvent
        }

        mediaRatingTv.text = media.averageScore

        mediaFormatTv.text = media.format?.let {
            misc[0][it]
        }.naText()

        mediaFormatTv.status = media.mediaListEntry?.status


        mediaStatusTv.naText(media.status?.let {
            mediaStatusTv.color = Color.parseColor(misc[2][it])
            misc[1][it]
        })

        airingCardView.setOnClickListener {
            OpenMediaInfoEvent(
                MediaInfoMeta(
                    media.id,
                    media.type!!,
                    media.title!!.romaji!!,
                    media.coverImage!!.image(context),
                    media.coverImage!!.largeImage,
                    media.bannerImage
                )
            ).postEvent
        }

        root.setOnLongClickListener {
            context.loginContinue {
                OpenMediaListEditorEvent(media.id).postEvent
            }
            true
        }

        airingTimeTv.setAiringText(item)


        if (context.loggedIn()) {
            entryProgressTv.visibility = View.VISIBLE
            entryProgressTv.compoundDrawablesRelative[0]?.setTint(
                dynamicTextColorPrimary
            )
            entryProgressTv.text = context.getString(R.string.s_slash_s).format(
                media.mediaListEntry?.progress?.toString().naText(),
                when (media.type) {
                    MediaType.ANIME.ordinal -> {
                        media.episodes.naText()
                    }
                    else -> {
                        media.chapters.naText()
                    }

                }
            )
        } else {
            entryProgressTv.visibility = View.GONE
        }
    }

    fun AiringCompactPresenterLayoutBinding.bindPresenter(
        context: Context,
        item: AiringScheduleModel,
        vararg misc: Array<String>
    ) { //formats status statuscolor
        val media = item.media ?: return
        mediaTitleTv.naText(media.title!!.title(context))
        coverImageIv.setImageURI(media.coverImage!!.image(context))

        if (media.isAnime) {
            mediaEpisodeTv.text =
                context.getString(R.string.ep_d_s)
                    .format(media.episodes.naText(), media.duration.naText())
        } else {
            mediaEpisodeTv.text =
                context.getString(R.string.chap_s)
                    .format(media.chapters.naText(), media.volumes.naText())
        }

        mediaRatingTv.text = media.averageScore

        mediaFormatTv.text = media.format?.let {
            misc[0][it]
        }.naText()

        mediaFormatTv.status = media.mediaListEntry?.status


        root.setOnClickListener {
            OpenMediaInfoEvent(
                MediaInfoMeta(
                    media.id,
                    media.type!!,
                    media.title!!.romaji!!,
                    media.coverImage!!.image(context),
                    media.coverImage!!.largeImage,
                    media.bannerImage
                )
            ).postEvent
        }

        root.setOnLongClickListener {
            context.loginContinue {
                OpenMediaListEditorEvent(media.id).postEvent
            }
            true
        }

        airingTimeTv.text = context.getString(R.string.episode_format_s).format(
            item.episode,
            item.airingAtModel?.airingTime
        )

        if (context.loggedIn()) {
            entryProgressTv.visibility = View.VISIBLE
            entryProgressTv.compoundDrawablesRelative[0]?.setTint(dynamicTextColorPrimary)
            entryProgressTv.text = context.getString(R.string.s_slash_s).format(
                media.mediaListEntry?.progress?.toString().naText(),
                when (media.type) {
                    MediaType.ANIME.ordinal -> {
                        media.episodes.naText()
                    }
                    else -> {
                        media.chapters.naText()
                    }

                }
            )
        } else {
            entryProgressTv.visibility = View.GONE
        }
    }

    fun AiringMinimalListPresenterLayoutBinding.bindPresenter(
        context: Context,
        item: AiringScheduleModel,
        blurDrawable: Drawable,
        vararg misc: Array<String>
    ) {
        val media = item.media ?: return
        mediaBannerIv.setImageURI(media.bannerImage)
        mediaTitleTv.text = media.title?.title(context)
        blurFrameLayout.background = blurDrawable
        mediaAiringAtHeaderTv.text =
            context.getString(R.string.ep_1_s_airing_at).format(item.episode)
        mediaAiringAtTv.text = item.airingAtModel?.airingTime
        airingTimeTv.setAiringText(item, false)
        mediaStatusTv.status = media.mediaListEntry?.status
        media.mediaListEntry?.status?.let {
            mediaStatusTv.text = misc[1][it]
            mediaStatusTv.textView.color = Color.parseColor(misc[2][it])
        } ?: let {
            mediaStatusTv.text = ""
        }

        root.setOnClickListener {
            OpenMediaInfoEvent(
                MediaInfoMeta(
                    media.id,
                    media.type!!,
                    media.title!!.romaji!!,
                    media.coverImage!!.image(context),
                    media.coverImage!!.largeImage,
                    media.bannerImage
                )
            ).postEvent
        }



        root.setOnLongClickListener {
            context.loginContinue {
                OpenMediaListEditorEvent(media.id).postEvent
            }
            true
        }
    }
}