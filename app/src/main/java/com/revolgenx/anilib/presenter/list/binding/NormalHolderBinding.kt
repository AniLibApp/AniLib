package com.revolgenx.anilib.presenter.list.binding

import android.graphics.Color
import android.view.View
import com.apollographql.apollo.exception.ApolloHttpException
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.HTTP_TOO_MANY_REQUEST
import com.revolgenx.anilib.event.BrowseGenreEvent
import com.revolgenx.anilib.event.BrowseMediaEvent
import com.revolgenx.anilib.event.ListEditorEvent
import com.revolgenx.anilib.meta.ListEditorMeta
import com.revolgenx.anilib.meta.MediaBrowserMeta
import com.revolgenx.anilib.model.EntryListEditorMediaModel
import com.revolgenx.anilib.model.list.MediaListModel
import com.revolgenx.anilib.model.search.filter.MediaSearchFilterModel
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.presenter.list.MediaListCollectionPresenter
import com.revolgenx.anilib.repository.util.Status
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.viewmodel.media_list.MediaListCollectionViewModel
import kotlinx.android.synthetic.main.media_list_collection_normal_presenter_layout.view.*

object NormalHolderBinding {


    fun bind(
        view: View,
        item: MediaListModel,
        mediaFormats: Array<String>,
        mediaStatus: Array<String>,
        statusColors: Array<String>,
        isLoggedInUser: Boolean,
        viewModel: MediaListCollectionViewModel
    ) {
        view.apply {
            mediaListTitleTv.text = item.title?.userPreferred
            mediaListCoverImageView.setImageURI(item.coverImage?.large)
            mediaListFormatTv.text = item.format?.let {
                mediaFormats[it]
            }.naText()

            mediaListStatusTv.text = item.status?.let {
                mediaListStatusTv.color = Color.parseColor(statusColors[it])
                mediaStatus[it]
            }.naText()

            mediaListProgressTv.text =
                context.getString(R.string.s_slash_s_brackets)
                    .format(
                        item.progress?.toString().naText(),
                        if (item.type == MediaType.ANIME.ordinal) item.episodes.naText() else item.chapters.naText(),
                        context.getString(R.string.startdate_format).format(
                            item.listStartDate?.toString().naText(),
                            item.listCompletedDate?.toString().naText()
                        )
                    )

            mediaListProgressTv.compoundDrawablesRelative[0]?.setTint(MediaListCollectionPresenter.tintSurfaceColor)

            when (item.scoreFormat) {
                ScoreFormat.POINT_3.ordinal -> {
                    val drawable = when (item.score?.toInt()) {
                        1 -> R.drawable.ic_score_sad
                        2 -> R.drawable.ic_score_neutral
                        3 -> R.drawable.ic_score_smile
                        else -> R.drawable.ic_role
                    }
                    mediaListRatingIv.setImageResource(drawable)
                }
                ScoreFormat.POINT_10_DECIMAL.ordinal -> {
                    mediaListRatingTv.text = item.score?.toString().naText()
                }
                else -> {
                    mediaListRatingTv.text = item.score?.toInt()?.toString().naText()
                }
            }

            mediaListGenreLayout.addGenre(item.genres?.take(3)) { genre ->
                BrowseGenreEvent(MediaSearchFilterModel().also {
                    it.genre = listOf(genre.trim())
                }).postEvent
            }

            mediaListStartDateTv.text = context.getString(R.string.startdate_format)
                .format(item.startDate?.toString().naText(), item.endDate?.toString().naText())

            if (isLoggedInUser) {
                mediaListProgressIncrease.setOnClickListener {
                    viewModel.increaseProgress(EntryListEditorMediaModel().also {
                        it.mediaId = item.mediaId
                        it.listId = item.mediaListId
                        it.progress = (item.progress ?: 0).plus(1)
                    }) { res ->
                        when (res.status) {
                            Status.SUCCESS -> {
                                if (res.data?.mediaId == item.mediaId) {
                                    item.progress = res.data?.progress
                                    mediaListProgressTv.text =
                                            context.getString(R.string.s_slash_s_brackets)
                                                .format(
                                                    item.progress?.toString().naText(),
                                                    if (item.type == MediaType.ANIME.ordinal) item.episodes.naText() else item.chapters.naText(),
                                                    context.getString(R.string.startdate_format)
                                                        .format(
                                                            item.listStartDate?.toString().naText(),
                                                            item.listCompletedDate?.toString()
                                                                .naText()
                                                        )
                                                )
                                }
                            }
                            Status.ERROR -> {
                                if (res.exception is ApolloHttpException) {
                                    when (res.exception.code()) {
                                        HTTP_TOO_MANY_REQUEST -> {
                                            context.makeToast(
                                                R.string.too_many_request,
                                                icon = R.drawable.ic_error
                                            )
                                        }
                                        else -> {
                                            context.makeToast(
                                                R.string.operation_failed,
                                                icon = R.drawable.ic_error
                                            )
                                        }
                                    }
                                } else {
                                    context.makeToast(
                                        R.string.operation_failed,
                                        icon = R.drawable.ic_error
                                    )
                                }
                            }
                            Status.LOADING -> {

                            }
                        }

                    }
                }

            } else {
                mediaListProgressIncrease.visibility = View.GONE
            }

            mediaListContainer.setOnClickListener {
                BrowseMediaEvent(
                    MediaBrowserMeta(
                        item.mediaId,
                        item.type!!,
                        item.title!!.userPreferred,
                        item.coverImage!!.image(context),
                        item.coverImage!!.largeImage,
                        item.bannerImage
                    ), mediaListCoverImageView
                ).postEvent
            }

            mediaListContainer.setOnLongClickListener {
                if (context.loggedIn()) {
                    ListEditorEvent(
                        ListEditorMeta(
                            item.mediaId,
                            item.type!!,
                            item.title!!.userPreferred,
                            item.coverImage!!.image(context),
                            item.bannerImage
                        ), mediaListCoverImageView
                    ).postEvent
                } else {
                    context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
                }
                true
            }
        }
    }
}