package com.revolgenx.anilib.ui.presenter.list

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.facebook.drawee.view.SimpleDraweeView
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.getMediaListGridPresenter
import com.revolgenx.anilib.common.preference.listEditOrBrowse
import com.revolgenx.anilib.constant.MediaListDisplayMode
import com.revolgenx.anilib.data.model.list.AlMediaListModel
import com.revolgenx.anilib.search.data.model.filter.MediaSearchFilterModel
import com.revolgenx.anilib.databinding.*
import com.revolgenx.anilib.infrastructure.event.OpenSearchEvent
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.ui.presenter.list.binding.ListBindingHelper
import com.revolgenx.anilib.ui.view.GenreLayout
import com.revolgenx.anilib.ui.view.score.MediaScoreBadge
import com.revolgenx.anilib.util.naText

class AlListCollectionPresenter(context: Context) :
    BasePresenter<ViewBinding, AlMediaListModel>(context) {

    override val elementTypes: Collection<Int>
        get() = listOf(0)

    private val displayMode = getMediaListGridPresenter()
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
    ): ViewBinding {
        return when (displayMode) {
            MediaListDisplayMode.COMPACT -> MediaListCollectionCompactPresenterLayoutBinding.inflate(
                getLayoutInflater(),
                parent,
                false
            )
            MediaListDisplayMode.NORMAL -> MediaListCollectionNormalPresenterLayoutBinding.inflate(
                getLayoutInflater(),
                parent,
                false
            )
            MediaListDisplayMode.CARD -> MediaListCollectionCardPresenterLayoutBinding.inflate(
                getLayoutInflater(),
                parent,
                false
            )
            MediaListDisplayMode.MINIMAL -> MediaListCollectionMinimalPresenterLayoutBinding.inflate(
                getLayoutInflater(),
                parent,
                false
            )
            MediaListDisplayMode.MINIMAL_LIST -> MediaListPresenterListMinimalPresenterBinding.inflate(
                getLayoutInflater(),
                parent,
                false
            )
            else -> MediaListCollectionClassicCardPresenterLayoutBinding.inflate(
                getLayoutInflater(),
                parent,
                false
            )
        }
    }

    override fun onBind(page: Page, holder: Holder, element: Element<AlMediaListModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return

        var titleTv: DynamicTextView? = null
        var coverIv: SimpleDraweeView? = null
        var formatTv: DynamicTextView? = null
        var statusTv: DynamicTextView? = null
        var progressTv: DynamicTextView? = null
        var statusDivider: View? = null
        var scoreBadgeTv: MediaScoreBadge? = null
        var genreLayout: GenreLayout? = null
        var startDateTv:TextView? = null

        holder.getBinding()?.also {
            when (it) {
                is MediaListCollectionCompactPresenterLayoutBinding -> {
                    titleTv = it.mediaListTitleTv
                    coverIv = it.mediaListCoverImageView
                    formatTv = it.mediaListFormatTv
                    statusTv = it.mediaListStatusTv
                    progressTv = it.mediaListProgressTv
                    scoreBadgeTv = it.mediaListRatingTv
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
                }
                is MediaListCollectionCardPresenterLayoutBinding -> {
                    titleTv = it.mediaListTitleTv
                    coverIv = it.mediaListCoverImageView
                    formatTv = it.mediaListFormatTv
                    progressTv = it.mediaListProgressTv
                    statusDivider = it.statusDivider
                    scoreBadgeTv = it.mediaListRatingTv
                }
                is MediaListCollectionMinimalPresenterLayoutBinding -> {
                    titleTv = it.mediaListTitleTv
                    coverIv = it.mediaListCoverImageView
                    progressTv = it.mediaListProgressTv
                }

                is MediaListPresenterListMinimalPresenterBinding -> {
                    titleTv = it.mediaListTitleTv
                    coverIv = it.mediaListCoverImageView
                    progressTv = it.mediaListProgressTv
                }
                is MediaListCollectionClassicCardPresenterLayoutBinding -> {
                    titleTv = it.mediaListTitleTv
                    coverIv = it.mediaListCoverImageView
                    formatTv = it.mediaListFormatTv
                    progressTv = it.mediaListProgressTv
                    scoreBadgeTv = it.mediaListRatingTv
                }
            }

            titleTv?.text = item.title?.userPreferred
            coverIv?.setImageURI(item.coverImage?.image(context))
            formatTv?.text = item.format?.let { mediaFormats[it] }.naText()

            if (displayMode == MediaListDisplayMode.CLASSIC) {
                formatTv?.append(item.status?.let { " · " + mediaStatus[it] }.naText())
            }

            item.status?.let {
                statusDivider?.setBackgroundColor(Color.parseColor(statusColors[it]))
            }

            statusTv?.text = item.status?.let { status ->
                statusTv?.color = Color.parseColor(statusColors[status])
                mediaStatus[status]
            }.naText()

            progressTv?.text = context.getString(R.string.s_slash_s).format(
                item.progress?.toString().naText(),
                if (item.type == MediaType.ANIME.ordinal) item.episodes.naText() else item.chapters.naText()
            )

            genreLayout?.addGenre(item.genres?.take(3)) { genre ->
                OpenSearchEvent(MediaSearchFilterModel().also {
                    it.genre = listOf(genre.trim())
                }).postEvent
            }

            startDateTv?.text = context.getString(R.string.startdate_format)
                .format(item.startDate?.toString().naText(), item.endDate?.toString().naText())


            when (item.scoreFormat) {
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
                    ListBindingHelper.openMediaListEditor(
                        context,
                        item
                    )
                    return@setOnClickListener;
                }
            }

            it.root.setOnLongClickListener {
                if (listEditOrBrowse(context)) {
                    ListBindingHelper.openMediaBrowse(context, item)
                    return@setOnLongClickListener true
                }
                true
            }
        }


    }

}