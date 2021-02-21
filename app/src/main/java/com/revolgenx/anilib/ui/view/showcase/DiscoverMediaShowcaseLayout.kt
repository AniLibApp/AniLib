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
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.disableCardStyleInHomeScreen
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.data.meta.ListEditorMeta
import com.revolgenx.anilib.data.meta.MediaBrowserMeta
import com.revolgenx.anilib.data.model.EntryListEditorMediaModel
import com.revolgenx.anilib.data.model.entry.MediaEntryListModel
import com.revolgenx.anilib.data.model.home.SelectableCommonMediaModel
import com.revolgenx.anilib.databinding.DiscoverShowCaseLayoutBinding
import com.revolgenx.anilib.infrastructure.event.BrowseMediaEvent
import com.revolgenx.anilib.infrastructure.event.ListEditorEvent
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.ui.viewmodel.home.discover.ShowCaseViewModel
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.util.prettyNumberFormat

class DiscoverMediaShowcaseLayout : LinearLayout {

    val showcaseRecyclerView: RecyclerView

    private var mediaModel: SelectableCommonMediaModel? = null

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

        addView(showcaseRecyclerView)
    }


    fun bindShowCaseMedia(
        media: SelectableCommonMediaModel,
        lifecycleOwner: LifecycleOwner,
        viewModel: ShowCaseViewModel
    ) {
        if (this.mediaModel == media || disableCardStyleInHomeScreen() || binding == null) return

        this.viewLifecycleOwner = lifecycleOwner
        this.viewModel = viewModel
        this.mediaModel?.let {
            it.isSelected = false
            it.selectionListener?.invoke(false)
        }

        this.mediaModel = media

        with(binding!!) {
            showCaseImageView.setImageURI(media.bannerImage)
            frameBlurLayout.visibility = View.VISIBLE
            mediaTitleTv.text = media.title?.title(context)

            val studioOrWriter = if (media.type == MediaType.ANIME.ordinal) {
                media.studios?.filter { it.isAnimationStudio }
                    ?.joinToString(", ") { it.studioName!! }
            } else {
                media.staff?.joinToString(", ") { it.name!! }
            }
            studioOrWriterTv.text = studioOrWriter?.takeIf { !it.isEmpty() } ?: ""
            mediaDescriptionTv.text =
                HtmlCompat.fromHtml(media.description!!, HtmlCompat.FROM_HTML_MODE_COMPACT)
            mediaPopularityTv.text = media.popularity?.prettyNumberFormat().naText()
            mediaFavTv.text = media.favourites?.prettyNumberFormat().naText()
            mediaListStatusTv.text = media.mediaEntryListModel?.status?.let {
                mediaListStatus[it]
            } ?: context.getString(R.string.add_to_list)

            showCaseListStatusMoreIv.onPopupMenuClickListener = { _, position ->
                changeMediaListStatus(position)
            }

            root.setOnClickListener {
                BrowseMediaEvent(
                    MediaBrowserMeta(
                        media.mediaId,
                        media.type!!,
                        media.title!!.romaji!!,
                        media.coverImage!!.image(context),
                        media.coverImage!!.largeImage,
                        media.bannerImage
                    ), null
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

    private fun openListEditor(){
        if(mediaModel == null) return

        if (context.loggedIn()) {
            ListEditorEvent(
                ListEditorMeta(
                    mediaModel!!.mediaId,
                    mediaModel!!.type!!,
                    mediaModel!!.title!!.title(context)!!,
                    mediaModel!!.coverImage!!.image(context),
                    mediaModel!!.bannerImage
                ), null
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


        viewModel!!.saveMediaListEntry(EntryListEditorMediaModel().also {
            it.mediaId = mediaModel!!.mediaId
            it.status = position
        }).observe(viewLifecycleOwner!!) {
            if (it.status == Status.SUCCESS) {
                val data = it.data ?: return@observe
                if (mediaModel!!.mediaEntryListModel == null) {
                    mediaModel!!.mediaEntryListModel =
                        MediaEntryListModel(data.progress, data.status)
                } else {
                    mediaModel!!.mediaEntryListModel?.status = data.status
                    mediaModel!!.mediaEntryListModel?.progress = data.status
                }
                mediaModel!!.mediaEntryListModel?.status?.let {
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