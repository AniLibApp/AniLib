package com.revolgenx.anilib.list.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import androidx.viewbinding.ViewBinding
import com.facebook.drawee.view.SimpleDraweeView
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.common.presenter.Constant
import com.revolgenx.anilib.constant.MediaListDisplayMode
import com.revolgenx.anilib.entry.data.meta.EntryEditorMeta
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.databinding.*
import com.revolgenx.anilib.infrastructure.event.OpenMediaInfoEvent
import com.revolgenx.anilib.infrastructure.event.OpenMediaListEditorEvent
import com.revolgenx.anilib.infrastructure.event.OpenSearchEvent
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.search.data.model.filter.SearchFilterModel
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.ui.view.GenreLayout
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.ui.view.score.MediaScoreBadge
import com.revolgenx.anilib.user.data.model.UserModel
import com.revolgenx.anilib.util.naText

class MediaListCollectionPresenter(
    context: Context,
    private val isLoggedInUser: Boolean = false,
    private val mediaType: MediaType
) : BasePresenter<ViewBinding, MediaListModel>(context) {
    override val elementTypes: Collection<Int> = listOf(0)

    var listUser: UserModel? = null

    private val displayMode
        get() = if (isLoggedInUser) getUserMediaListCollectionDisplayMode(mediaType)
        else getGeneralMediaListCollectionDisplayMode(mediaType)

    private val mediaFormats =
        context.resources.getStringArray(R.array.media_format)
    private val mediaStatus =
        context.resources.getStringArray(R.array.media_status)
    private val statusColors =
        context.resources.getStringArray(R.array.status_color)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): ViewBinding = when (displayMode) {
        MediaListDisplayMode.COMPACT -> MediaListCollectionCompactPresenterLayoutBinding.inflate(
            inflater,
            parent,
            false
        )
        MediaListDisplayMode.NORMAL -> MediaListCollectionNormalPresenterLayoutBinding.inflate(
            inflater,
            parent,
            false
        )
        MediaListDisplayMode.CARD -> MediaListCollectionCardPresenterLayoutBinding.inflate(
            inflater,
            parent,
            false
        ).also {
            it.mediaMetaBackground.setBackgroundColor(
                ColorUtils.setAlphaComponent(
                    dynamicBackgroundColor,
                    200
                )
            )
        }
        MediaListDisplayMode.MINIMAL -> MediaListCollectionMinimalPresenterLayoutBinding.inflate(
            inflater,
            parent,
            false
        )
        MediaListDisplayMode.MINIMAL_LIST -> MediaListPresenterListMinimalPresenterBinding.inflate(
            inflater,
            parent,
            false
        )
        else -> MediaListCollectionClassicCardPresenterLayoutBinding.inflate(
            inflater,
            parent,
            false
        )
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaListModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        val binding: ViewBinding = holder[Constant.PRESENTER_BINDING_KEY] ?: return

        var titleTv: DynamicTextView? = null
        var coverIv: SimpleDraweeView? = null
        var formatTv: DynamicTextView? = null
        var statusTv: DynamicTextView? = null
        var progressTv: DynamicTextView? = null
        var scoreBadgeTv: MediaScoreBadge? = null
        var genreLayout: GenreLayout? = null
        var startDateTv: TextView? = null
        var mediaListProgressIncrease: DynamicImageView? = null

        binding.also {
            when (it) {
                is MediaListCollectionCompactPresenterLayoutBinding -> {
                    titleTv = it.mediaListTitleTv
                    coverIv = it.mediaListCoverImageView
                    formatTv = it.mediaListFormatTv
                    statusTv = it.mediaListStatusTv
                    progressTv = it.mediaListProgressTv
                    scoreBadgeTv = it.mediaListRatingTv
                    mediaListProgressIncrease = it.mediaListProgressIncrease
                }
                is MediaListCollectionNormalPresenterLayoutBinding -> {
                    titleTv = it.mediaListTitleTv
                    coverIv = it.mediaListCoverImageView
                    formatTv = it.mediaListFormatTv
                    statusTv = it.mediaListStatusTv
                    progressTv = it.mediaListProgressTv
                    scoreBadgeTv = it.mediaListRatingTv
                    genreLayout = it.mediaListGenreLayout
                    startDateTv = it.mediaListStartDateTv
                    mediaListProgressIncrease = it.mediaListProgressIncrease
                }
                is MediaListCollectionCardPresenterLayoutBinding -> {
                    titleTv = it.mediaListTitleTv
                    coverIv = it.mediaListCoverImageView
                    formatTv = it.mediaListFormatTv
                    statusTv = it.mediaListStatusTv
                    progressTv = it.mediaListProgressTv
                    scoreBadgeTv = it.mediaListRatingTv
                    mediaListProgressIncrease = it.mediaListProgressIncrease
                }
                is MediaListCollectionMinimalPresenterLayoutBinding -> {
                    titleTv = it.mediaListTitleTv
                    coverIv = it.mediaListCoverImageView
                    progressTv = it.mediaListProgressTv
                    mediaListProgressIncrease = it.mediaListProgressIncrease
                }

                is MediaListPresenterListMinimalPresenterBinding -> {
                    titleTv = it.mediaListTitleTv
                    coverIv = it.mediaListCoverImageView
                    progressTv = it.mediaListProgressTv
                    mediaListProgressIncrease = it.mediaListProgressIncrease
                }
                is MediaListCollectionClassicCardPresenterLayoutBinding -> {
                    titleTv = it.mediaListTitleTv
                    coverIv = it.mediaListCoverImageView
                    formatTv = it.mediaListFormatTv
                    statusTv = it.mediaListStatusTv
                    progressTv = it.mediaListProgressTv
                    scoreBadgeTv = it.mediaListRatingTv
                    mediaListProgressIncrease = it.mediaListProgressIncrease
                }
            }

            if (isLoggedInUser) {
                mediaListProgressIncrease?.visibility = View.VISIBLE

            } else {
                mediaListProgressIncrease?.visibility = View.GONE
            }

            titleTv?.text = item.media?.title?.userPreferred
            coverIv?.setImageURI(item.media?.coverImage?.image(context))
            formatTv?.text = item.media?.format?.let { mediaFormats[it] }.naText()

            statusTv?.text = item.media?.status?.let { status ->
                statusTv?.color = Color.parseColor(statusColors[status])
                mediaStatus[status]
            }.naText()

            progressTv?.text = context.getString(R.string.s_slash_s).format(
                item.progress?.toString().naText(),
                if (item.media?.type == MediaType.ANIME.ordinal) item.media?.episodes.naText() else item.media?.chapters.naText()
            )

            progressTv?.compoundDrawablesRelative?.get(0)?.setTint(dynamicTextColorPrimary)

            genreLayout?.addGenre(item.media?.genres?.take(3)) { genre ->
                OpenSearchEvent(SearchFilterModel(genre = genre)).postEvent
            }

            startDateTv?.text = context.getString(R.string.startdate_format)
                .format(
                    item.media?.startDate?.toString().naText(),
                    item.media?.endDate?.toString().naText()
                )


            when (item.user?.mediaListOptions?.scoreFormat) {
                ScoreFormat.POINT_3.ordinal -> {
                    val drawable = when (item.score?.toInt()) {
                        1 -> R.drawable.ic_score_sad
                        2 -> R.drawable.ic_score_neutral
                        3 -> R.drawable.ic_score_smile
                        else -> R.drawable.ic_role
                    }
                    scoreBadgeTv?.setImageResource(drawable)
                    scoreBadgeTv?.scoreTextVisibility = View.GONE

                }
                ScoreFormat.POINT_10_DECIMAL.ordinal -> {
                    scoreBadgeTv?.setText(item.score)
                }
                else -> {
                    scoreBadgeTv?.text = item.score?.toInt()
                }
            }


            it.root.setOnClickListener {
                if (listEditOrBrowse(context)) {
                    openMediaListEditor(
                        context,
                        item.media!!
                    )
                    return@setOnClickListener;
                }
            }

            it.root.setOnLongClickListener {
                if (listEditOrBrowse(context)) {
                    openMediaBrowse(context, item.media!!)
                    return@setOnLongClickListener true
                }
                true
            }
        }
    }

    private fun openMediaBrowse(context: Context, item: MediaModel) {
        OpenMediaInfoEvent(
            MediaInfoMeta(
                item.id,
                item.type!!,
                item.title!!.userPreferred,
                item.coverImage!!.image(context),
                item.coverImage!!.largeImage,
                item.bannerImage
            )
        ).postEvent
    }

    private fun openMediaListEditor(context: Context, item: MediaModel) {
        if (context.loggedIn()) {
            OpenMediaListEditorEvent(
                EntryEditorMeta(
                    item.id,
                    item.type!!,
                    item.title!!.userPreferred,
                    item.coverImage!!.image(context),
                    item.bannerImage
                )
            ).postEvent
        } else {
            context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
        }
    }

}