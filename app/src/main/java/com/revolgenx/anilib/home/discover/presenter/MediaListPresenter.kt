package com.revolgenx.anilib.home.discover.presenter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary
import com.revolgenx.anilib.common.preference.UserPreference
import com.revolgenx.anilib.common.preference.userName
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.data.meta.MediaListMeta
import com.revolgenx.anilib.databinding.MediaListPresenterBinding
import com.revolgenx.anilib.common.event.OpenMediaInfoEvent
import com.revolgenx.anilib.common.event.OpenMediaListEditorEvent
import com.revolgenx.anilib.common.event.OpenSearchEvent
import com.revolgenx.anilib.common.presenter.BasePresenter.Companion.PRESENTER_BINDING_KEY
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.home.discover.viewmodel.MediaListVM
import com.revolgenx.anilib.media.data.model.isAnime
import com.revolgenx.anilib.search.data.model.SearchFilterEventModel
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.loginContinue
import com.revolgenx.anilib.util.naText

class MediaListPresenter(
    context: Context,
    private val mediaListMeta: MediaListMeta,
    private val viewModel: MediaListVM
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
            Holder(it.root).also { h -> h[PRESENTER_BINDING_KEY] = it }
        }
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaListModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        val media = item.media ?: return
        val binding: MediaListPresenterBinding = holder[PRESENTER_BINDING_KEY] ?: return

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

            mediaListProgressTv.updateProgressView(item)

            mediaListProgressTv.compoundDrawablesRelative[0]?.setTint(dynamicTextColorPrimary)

            mediaListGenreLayout.addGenre(media.genres?.take(3)) { genre ->
                OpenSearchEvent(SearchFilterEventModel(genre = genre)).postEvent
            }

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
                    viewModel.increaseProgress(item)
                }

                item.onDataChanged = {
                    when (it) {
                        is Resource.Success -> {
                            mediaListProgressTv.updateProgressView(item)
                        }
                        is Resource.Error -> {
                            context.makeToast(R.string.operation_failed)
                        }
                        else -> {}
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
                context.loginContinue {
                    OpenMediaListEditorEvent(media.id).postEvent
                }
            }
        }
    }

    private fun DynamicTextView.updateProgressView(item: MediaListModel) {
        text =
            context.getString(R.string.s_slash_s).format(
                item.progress?.toString().naText(),
                if (isAnime(item.media)) item.media?.episodes.naText() else item.media?.chapters.naText()
            )
    }

}