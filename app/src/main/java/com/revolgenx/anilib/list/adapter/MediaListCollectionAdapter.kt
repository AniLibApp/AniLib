package com.revolgenx.anilib.list.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.facebook.drawee.view.SimpleDraweeView
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.setting.data.model.MediaListOptionModel
import com.revolgenx.anilib.common.preference.getMediaListGridPresenter
import com.revolgenx.anilib.common.preference.listEditOrBrowse
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.constant.MediaListDisplayMode
import com.revolgenx.anilib.entry.data.meta.EntryEditorMeta
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.search.data.model.filter.MediaSearchFilterModel
import com.revolgenx.anilib.databinding.*
import com.revolgenx.anilib.infrastructure.event.OpenMediaInfoEvent
import com.revolgenx.anilib.infrastructure.event.OpenMediaListEditorEvent
import com.revolgenx.anilib.infrastructure.event.OpenSearchEvent
import com.revolgenx.anilib.list.data.model.FilteredMediaListGroupModel
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.ui.view.GenreLayout
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.ui.view.score.MediaScoreBadge
import com.revolgenx.anilib.util.naText

class MediaListCollectionAdapter(private val context: Context) :
    RecyclerView.Adapter<MediaListCollectionAdapter.AlListRecyclerViewVH>() {

    var filteredMediaListGroupModel: FilteredMediaListGroupModel? = null
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    private val displayMode = getMediaListGridPresenter()
    private val mediaFormats =
        context.resources.getStringArray(R.array.media_format)
    private val mediaStatus =
        context.resources.getStringArray(R.array.media_status)
    private val statusColors =
        context.resources.getStringArray(R.array.status_color)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlListRecyclerViewVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = when (displayMode) {
            MediaListDisplayMode.COMPACT -> MediaListCollectionCompactPresenterLayoutBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            MediaListDisplayMode.NORMAL -> MediaListCollectionNormalPresenterLayoutBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            MediaListDisplayMode.CARD -> MediaListCollectionCardPresenterLayoutBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            MediaListDisplayMode.MINIMAL -> MediaListCollectionMinimalPresenterLayoutBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            MediaListDisplayMode.MINIMAL_LIST -> MediaListPresenterListMinimalPresenterBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            else -> MediaListCollectionClassicCardPresenterLayoutBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        }
        return AlListRecyclerViewVH(binding)
    }

    override fun onBindViewHolder(holder: AlListRecyclerViewVH, position: Int) {
        val mediaListGroupModel = filteredMediaListGroupModel ?: return
        val alMediaListsModel = mediaListGroupModel.entries?.get(position) ?: return
        holder.onBind(alMediaListsModel)
    }

    override fun getItemCount(): Int = filteredMediaListGroupModel?.entries?.count() ?: 0

    inner class AlListRecyclerViewVH(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val context = itemView.context

        fun onBind(item: MediaListModel) {
            var titleTv: DynamicTextView? = null
            var coverIv: SimpleDraweeView? = null
            var formatTv: DynamicTextView? = null
            var statusTv: DynamicTextView? = null
            var progressTv: DynamicTextView? = null
            var statusDivider: View? = null
            var scoreBadgeTv: MediaScoreBadge? = null
            var genreLayout: GenreLayout? = null
            var startDateTv: TextView? = null

            binding.also {
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

                titleTv?.text = item.media?.title?.userPreferred
                coverIv?.setImageURI(item.media?.coverImage?.image(context))
                formatTv?.text = item.media?.format?.let { mediaFormats[it] }.naText()

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
                    if (item.media?.type == MediaType.ANIME.ordinal) item.media?.episodes.naText() else item.media?.chapters.naText()
                )

                genreLayout?.addGenre(item.media?.genres?.take(3)) { genre ->
                    OpenSearchEvent(MediaSearchFilterModel().also {
                        it.genre = listOf(genre.trim())
                    }).postEvent
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

}