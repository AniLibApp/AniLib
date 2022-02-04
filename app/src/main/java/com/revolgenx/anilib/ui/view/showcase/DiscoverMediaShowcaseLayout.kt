package com.revolgenx.anilib.ui.view.showcase

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.pranavpandey.android.dynamic.support.widget.DynamicViewPager2Layout
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.disableCardStyleInHomeScreen
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.entry.data.meta.EntryEditorMeta
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.entry.data.model.EntryListEditorMediaModel
import com.revolgenx.anilib.databinding.DiscoverShowCaseLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenMediaInfoEvent
import com.revolgenx.anilib.infrastructure.event.OpenMediaListEditorEvent
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.home.discover.viewmodel.ShowCaseViewModel
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.util.prettyNumberFormat

class DiscoverMediaShowcaseLayout : LinearLayout {

    val showcaseRecyclerView: RecyclerView

    private var mediaModel: MediaModel? = null

    private var binding: DiscoverShowCaseLayoutBinding? = null
    private var viewLifecycleOwner: LifecycleOwner? = null
    private var viewModel: ShowCaseViewModel? = null

    private val mediaListStatus by lazy {
        context.resources.getStringArray(R.array.media_list_status)
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        orientation = VERTICAL
        clipChildren = false
        if (!disableCardStyleInHomeScreen()) {
            binding =
                DiscoverShowCaseLayoutBinding.inflate(LayoutInflater.from(context), this, false)
            addView(binding!!.root)
        }

        showcaseRecyclerView = RecyclerView(context).also {
            it.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                context.resources.getDimensionPixelSize(
                    R.dimen.discover_recycler_height
                )
            )
            it.isNestedScrollingEnabled = false
        }

        val recyclerViewContainer = DynamicViewPager2Layout(context).also {
            it.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        recyclerViewContainer.addView(showcaseRecyclerView)

        addView(recyclerViewContainer)
    }


    fun bindShowCaseMedia(
        media: MediaModel,
        lifecycleOwner: LifecycleOwner,
        viewModel: ShowCaseViewModel
    ) {
        if (this.mediaModel == media || disableCardStyleInHomeScreen() || binding == null) return

        this.viewLifecycleOwner = lifecycleOwner
        this.viewModel = viewModel
        this.mediaModel?.let {
            it.isSelected = false
            it.onClickListener?.invoke(false)
        }

        this.mediaModel = media

        with(binding!!) {
            showCaseImageView.setImageURI(media.bannerImage)
            frameBlurLayout.visibility = View.VISIBLE
            mediaTitleTv.text = media.title?.title(context)

            val studioOrWriter = if (media.type == MediaType.ANIME.ordinal) {
                media.studios?.edges?.filter { it.isMain }?.joinToString(", ") { it.node?.studioName!! }
            } else {
                media.staffs?.edges?.joinToString(", ") { it.node?.name?.full ?: "" }
            }
            studioOrWriterTv.text = studioOrWriter?.takeIf { !it.isEmpty() } ?: ""
            mediaDescriptionTv.text =
                HtmlCompat.fromHtml(media.description ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
            mediaPopularityTv.text = media.popularity?.prettyNumberFormat().naText()
            mediaFavTv.text = media.favourites?.prettyNumberFormat().naText()
            mediaListStatusTv.text = media.mediaListEntry?.status?.let {
                mediaListStatus[it]
            } ?: context.getString(R.string.add_to_list)

            showCaseListStatusMoreIv.onPopupMenuClickListener = { _, position ->
                changeMediaListStatus(position)
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
                openListEditor()
                true
            }

            discoverListStatusLayout.setOnClickListener {
                openListEditor()
            }
        }
    }

    private fun openListEditor() {
        if (mediaModel == null) return

        if (context.loggedIn()) {
            OpenMediaListEditorEvent(
                EntryEditorMeta(
                    mediaModel!!.id,
                    mediaModel!!.type!!,
                    mediaModel!!.title!!.title(context)!!,
                    mediaModel!!.coverImage!!.image(context),
                    mediaModel!!.bannerImage
                )
            ).postEvent
        } else {
            context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
        }
    }


    private fun changeMediaListStatus(position: Int) {
        if (binding == null || this.mediaModel == null || this.viewLifecycleOwner == null || viewModel == null) return

        if (!context.loggedIn()) {
            context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
            return
        }

        viewModel!!.saveMediaListEntry(MediaListModel().also {
            it.mediaId = mediaModel!!.id
            it.status = position
        }).observe(viewLifecycleOwner!!) {
            if (it.status == Status.SUCCESS) {
                val data = it.data ?: return@observe
                if (mediaModel!!.mediaListEntry == null) {
                    mediaModel!!.mediaListEntry =
                        MediaListModel().also {
                            it.progress = data.progress
                            it.status = data.status
                        }
                } else {
                    mediaModel!!.mediaListEntry?.status = data.status
                    mediaModel!!.mediaListEntry?.progress = data.status
                }
                mediaModel!!.mediaListEntry?.status?.let {
                    binding!!.mediaListStatusTv.text =
                        resources.getStringArray(R.array.media_list_status)[it]
                }
                context.makeToast(R.string.saved_successfully)
            } else if (it.status == Status.ERROR) {
                context.makeToast(R.string.error)
            }
        }

    }
}