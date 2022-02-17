package com.revolgenx.anilib.home.discover.presenter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import com.apollographql.apollo3.exception.ApolloHttpException
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary
import com.revolgenx.anilib.common.preference.UserPreference
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.preference.userName
import com.revolgenx.anilib.constant.HTTP_TOO_MANY_REQUEST
import com.revolgenx.anilib.entry.data.meta.EntryEditorMeta
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.data.meta.MediaListMeta
import com.revolgenx.anilib.databinding.MediaListPresenterBinding
import com.revolgenx.anilib.infrastructure.event.OpenMediaInfoEvent
import com.revolgenx.anilib.infrastructure.event.OpenMediaListEditorEvent
import com.revolgenx.anilib.infrastructure.event.OpenSearchEvent
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.common.presenter.Constant
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.home.discover.viewmodel.MediaListViewModel
import com.revolgenx.anilib.search.data.model.filter.SearchFilterModel
import com.revolgenx.anilib.util.naText

class MediaListPresenter(
    context: Context,
    private val mediaListMeta: MediaListMeta,
    private val viewModel: MediaListViewModel
) :
    Presenter<MediaListModel>(context) {

    override val elementTypes: Collection<Int>
        get() = listOf(0)

    private val mediaFormats by lazy {
        context.resources.getStringArray(R.array.media_format)
    }

    private val mediaStatus by lazy {
        context.resources.getStringArray(R.array.media_status)
    }
    private val statusColors by lazy {
        context.resources.getStringArray(R.array.status_color)
    }

    private val isLoggedInUser by lazy {
        mediaListMeta.userId == UserPreference.userId || mediaListMeta.userName == context.userName()
    }

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return MediaListPresenterBinding.inflate(getLayoutInflater(), parent, false).let {
            Holder(it.root).also { h -> h[Constant.PRESENTER_BINDING_KEY] = it }
        }
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaListModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        val media = item.media ?: return
        val binding: MediaListPresenterBinding = holder[Constant.PRESENTER_BINDING_KEY] ?: return

        binding.apply {
            mediaListTitleTv.text = media.title?.userPreferred
            mediaListCoverImageView.setImageURI(media.coverImage?.large)
            mediaListFormatTv.text = media.format?.let {
                mediaFormats[it]
            }.naText()
            mediaListStatusTv.text = media.status?.let {
                mediaListStatusTv.color = Color.parseColor(statusColors[it])
                mediaStatus[it]
            }.naText()

            mediaListProgressTv.text = context.getString(R.string.s_slash_s).format(
                item.progress?.toString().naText(),
                if (media.type == MediaType.ANIME.ordinal) media.episodes.naText() else media.chapters.naText()
            )

            mediaListProgressTv.compoundDrawablesRelative[0]?.setTint(dynamicTextColorPrimary)

            mediaListGenreLayout.addGenre(media.genres?.take(3)) { genre ->
                OpenSearchEvent(SearchFilterModel(genre = genre)).postEvent
            }

            //TODO SCORE FORMAT

            when (item.user?.mediaListOptions?.scoreFormat) {
                ScoreFormat.POINT_3.ordinal -> {
                    val drawable = when (item.score?.toInt()) {
                        1 -> R.drawable.ic_score_sad
                        2 -> R.drawable.ic_score_neutral
                        3 -> R.drawable.ic_score_smile
                        else -> R.drawable.ic_role
                    }
                    mediaListRatingTv.setImageResource(drawable)
                    mediaListRatingTv.scoreTextVisibility = View.GONE

                }
                ScoreFormat.POINT_10_DECIMAL.ordinal -> {
                    mediaListRatingTv.setText(item.score)
                }
                else -> {
                    mediaListRatingTv.text = item.score?.toInt()
                }
            }

            if (isLoggedInUser) {
                mediaListProgressIncrease.setOnClickListener {
                    viewModel.increaseProgress(MediaListModel().also {
                        it.mediaId = media.id
                        it.id = item.id
                        it.progress = (item.progress ?: 0).plus(1)
                    }) { res ->
                        when (res.status) {
                            Status.SUCCESS -> {
                                if (res.data?.mediaId == media.id) {
                                    item.progress = res.data.progress
                                    mediaListProgressTv.text =
                                        context.getString(R.string.s_slash_s).format(
                                            item.progress?.toString().naText(),
                                            if (media.type == MediaType.ANIME.ordinal) media.episodes.naText() else media.chapters.naText()
                                        )
                                }
                            }
                            Status.ERROR -> {
                                if (res.exception is ApolloHttpException) {
                                    when (res.exception.statusCode) {
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

            mediaListContainer.setOnLongClickListener {
                OpenMediaInfoEvent(
                    MediaInfoMeta(
                        media.id,
                        media.type!!,
                        media.title!!.userPreferred,
                        media.coverImage!!.image(context),
                        media.coverImage!!.largeImage,
                        media.bannerImage
                    )
                ).postEvent
                true
            }

            mediaListContainer.setOnClickListener {
                if (context.loggedIn()) {
                    OpenMediaListEditorEvent(media.id).postEvent
                } else {
                    context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
                }
            }
        }
    }

}