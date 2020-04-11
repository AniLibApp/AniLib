package com.revolgenx.anilib.fragment.browse

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.style.ClickableSpan
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.net.toUri
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.*
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.otaliastudios.elements.pagers.PageSizePager
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicCardView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.MediaBrowseActivity
import com.revolgenx.anilib.adapter.BrowseRelationshipPresenter
import com.revolgenx.anilib.constant.*
import com.revolgenx.anilib.event.BrowseGenreEvent
import com.revolgenx.anilib.event.BrowseStudioEvent
import com.revolgenx.anilib.event.BrowseTagEvent
import com.revolgenx.anilib.meta.MediaBrowserMeta
import com.revolgenx.anilib.meta.StudioMeta
import com.revolgenx.anilib.field.media.MediaOverviewField
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.field.media.MediaRecommendationField
import com.revolgenx.anilib.markwon.MarkwonImpl
import com.revolgenx.anilib.model.MediaMetaCollection
import com.revolgenx.anilib.model.MediaOverviewModel
import com.revolgenx.anilib.model.MediaRelationshipModel
import com.revolgenx.anilib.model.MediaStudioModel
import com.revolgenx.anilib.model.search.filter.MediaBrowseFilterModel
import com.revolgenx.anilib.presenter.MediaRecommendationPresenter
import com.revolgenx.anilib.presenter.MediaExternalLinkPresenter
import com.revolgenx.anilib.presenter.MediaMetaPresenter
import com.revolgenx.anilib.repository.util.Status
import com.revolgenx.anilib.source.MediaOverviewRecommendationSource
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.util.dp
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.view.AiringEpisodeView
import com.revolgenx.anilib.view.span.SpoilerSpan
import com.revolgenx.anilib.viewmodel.MediaOverviewViewModel
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.loading_layout.*
import kotlinx.android.synthetic.main.media_overview_fragment.*
import kotlinx.android.synthetic.main.resource_status_container_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaOverviewFragment : BaseFragment() {

    private var mediaBrowserMeta: MediaBrowserMeta? = null
    private val viewModel by viewModel<MediaOverviewViewModel>()

    private val recommendationField by lazy {
        MediaRecommendationField()
    }

    private val recommendationSource: MediaOverviewRecommendationSource
        get() = viewModel.source ?: createRecommendationSource()
    private val relationshipPresenter by lazy {
        BrowseRelationshipPresenter(requireContext())
    }

    private val recommendationPresenter by lazy {
        MediaRecommendationPresenter(viewLifecycleOwner, requireContext(), viewModel)
    }

    private val loadingPresenter: Presenter<Void> by lazy {
        Presenter.forLoadingIndicator(
            requireContext(),
            R.layout.loading_layout
        )
    }

    private val statusColors by lazy {
        requireContext().resources.getStringArray(R.array.status_color)
    }


    private val mediaMetaList by lazy {
        mutableListOf<MediaMetaCollection>()
    }

    private val errorPresenter: Presenter<Void> by lazy {
        Presenter.forErrorIndicator(requireContext(), R.layout.error_layout)
    }

    private val emptyPresenter: Presenter<Void> by lazy {
        Presenter.forEmptyIndicator(requireContext(), R.layout.empty_layout)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.media_overview_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        metaContainerRecyclerView.layoutManager = FlexboxLayoutManager(context)
        metaLinkRecyclerView.layoutManager = FlexboxLayoutManager(context).also { manager ->
            manager.justifyContent = JustifyContent.SPACE_EVENLY;
            manager.alignItems = AlignItems.CENTER;
        }
        recommendationRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        relationRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mediaBrowserMeta =
            arguments?.getParcelable(MediaBrowseActivity.MEDIA_BROWSER_META) ?: return

        viewModel.mediaOverviewLiveData.observe(viewLifecycleOwner) { res ->
            when (res.status) {
                Status.SUCCESS -> {
                    resourceStatusContainer.visibility = View.GONE
                    progressLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                    updateView(res.data!!)
                    invalidateRecommendationAdapter()
                }
                Status.ERROR -> {
                    resourceStatusContainer.visibility = View.VISIBLE
                    progressLayout.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE
                    invalidateRecommendationAdapter()
                }
                Status.LOADING -> {
                    resourceStatusContainer.visibility = View.VISIBLE
                    progressLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                }
            }
        }

        if (savedInstanceState == null) {
            viewModel.getOverview(MediaOverviewField().also {
                it.mediaId = mediaBrowserMeta!!.mediaId
            })
        }

        recommendationField.mediaId = mediaBrowserMeta!!.mediaId
    }

    private fun updateView(overview: MediaOverviewModel) {
        context ?: return

        overview.status?.let {
            statusDivider.setBackgroundColor(Color.parseColor(statusColors[it]))
        }


        if (overview.title!!.native != null) {
            nativeTitle.subtitle = overview.title!!.native
        } else {
            nativeTitle.visibility = View.GONE
        }

        if (overview.title!!.english != null) {
            englishTitle.subtitle = overview.title!!.english
        } else {
            englishTitle.visibility = View.GONE
        }

        MarkwonImpl.instanceHtml.setMarkdown(mediaDescriptionTv, overview.description ?: "")

        mediaFormatTv.subtitle = overview.format?.let {
            requireContext().resources.getStringArray(
                R.array.media_format
            )[it]
        }.naText()
        mediaSourceTv.subtitle = overview.source?.let {
            requireContext().resources.getStringArray(
                R.array.media_source
            )[it]
        }.naText()

        if (overview.type == MediaType.ANIME.ordinal) {
            mediaEpisodeTv.subtitle = overview.episodes.naText()
            mediaDurationTv.subtitle =
                getString(R.string.min_s).format(overview.duration.naText())
        } else if (overview.type == MediaType.MANGA.ordinal) {
            mediaEpisodeTv.title = getString(R.string.chapters)
            mediaEpisodeTv.subtitle = overview.chapters.naText()
            mediaDurationTv.title = getString(R.string.volumes)
            mediaDurationTv.subtitle = overview.volumes.naText()
        }

        when (overview.trailer?.site) {
            YOUTUBE -> {
                overview.trailer?.thumbnail?.let { trailerSimpleDrawee.setImageURI(it) }
                trailerLayout.setOnClickListener {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            (YOUTUBE_URL + overview.trailer?.id).toUri()
                        )
                    )
                }
            }
            DAILYMOTION -> {
                overview.trailer?.thumbnail?.let { trailerSimpleDrawee.setImageURI(it) }
                trailerLayout.setOnClickListener {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            (DAILYMOTION_URL + overview.trailer?.id).toUri()
                        )
                    )
                }
            }
            else -> {
                let { trailerLayout.visibility = View.GONE }
            }
        }

        startEndDateTv.text = getString(R.string.start_s_end_s).format(
            overview.startDate.toString().naText(),
            overview.endDate.toString().naText()
        )

        overview.airingTimeModel?.let { atModel ->
            airingContainer.visibility = View.VISIBLE
            airingContainer.addView(
                DynamicCardView(requireContext()).also {
                    it.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).also { params ->
                        params.marginEnd = dp(2f)
                        params.marginStart = dp(2f)
                        params.bottomMargin = dp(20f)
                    }
                }.also {
                    it.addView(AiringEpisodeView(requireContext()).also { ae ->
                        ae.layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        ).also { params ->
                            params.marginStart = dp(30f)
                            params.marginEnd = dp(10f)
                        }
                        ae.setTimer(atModel)
                    })
                })
        }

        invalidateRelationshipAdapter(overview.relationship ?: emptyList())


        if (mediaMetaList.isEmpty()) {
            overview.title?.romaji?.let {
                addToMediaCollection(getString(R.string.romaji_title), it)
            }

            addStudioToMetaCollection(overview.studios?.filter { it.isAnimationStudio })
            addProducerToMetaCollection(overview.studios?.filter { !it.isAnimationStudio })

            overview.hashTag?.let {
                addToMediaCollection(getString(R.string.hashtag), it)
            }

            overview.averageScore?.let {
                addToMediaCollection(getString(R.string.averageScore), "$it %")
            }

            overview.meanScore?.let {
                addToMediaCollection(getString(R.string.meanScore), "$it %")
            }

            overview.status?.let {
                requireContext().resources.getStringArray(R.array.media_status)[it]?.let {
                    addToMediaCollection(getString(R.string.status), it)
                }
            }

            overview.popularity?.let {
                addToMediaCollection(getString(R.string.popularity), "$it")
            }
            overview.favourites?.let {
                addToMediaCollection(getString(R.string.favourites), "$it")
            }

            addGenreToMetaCollection(overview)
            addTagToMetaCollection(overview)

        }


        overview.externalLink?.let {
            Adapter.builder(viewLifecycleOwner)
                .addSource(Source.fromList(it))
                .addPresenter(MediaExternalLinkPresenter(requireContext()))
                .into(metaLinkRecyclerView)
        }

        Adapter.builder(viewLifecycleOwner)
            .addSource(Source.fromList(mediaMetaList))
            .addPresenter(MediaMetaPresenter(requireContext()))
            .into(metaContainerRecyclerView)

    }


    private fun addToMediaCollection(header: String, subtitle: String) {
        mediaMetaList.add(MediaMetaCollection().also { col ->
            col.header = header
            col.subTitle = subtitle
        })
    }


    private fun addGenreToMetaCollection(overview: MediaOverviewModel) {
        if (overview.genres?.isNotEmpty() == true) {
            val spannableStringBuilder = SpannableStringBuilder()
            val lastIndex = overview.genres!!.size - 1
            overview.genres?.forEachIndexed { index, genre ->
                val current = spannableStringBuilder.length
                spannableStringBuilder.append(genre.trim())
                spannableStringBuilder.setSpan(
                    object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            BrowseGenreEvent(MediaBrowseFilterModel().also {
                                it.genre = listOf(genre.trim())
                            }).postEvent
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            ds.isUnderlineText = true
                        }
                    },
                    current,
                    spannableStringBuilder.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                if (index < lastIndex)
                    spannableStringBuilder.append(", ")
            }

            mediaMetaList.add(MediaMetaCollection().also { col ->
                col.header = getString(R.string.genre)
                col.subTitleSpannable = spannableStringBuilder
            })
        }
    }

    private fun addTagToMetaCollection(overview: MediaOverviewModel) {
        if (overview.tags?.isNotEmpty() == true) {
            val spannableStringBuilder = SpannableStringBuilder()
            val spoilerSpans = mutableListOf<SpoilerSpan>()
            val lastIndex = overview.tags!!.size - 1
            overview.tags?.forEachIndexed { index, tag ->
                val current = spannableStringBuilder.length
                spannableStringBuilder.append(tag.name?.trim())
                val clickableSpan = if (tag.isSpoilerTag) {
                    object : SpoilerSpan() {
                        init {
                            spoilerSpans.add(this)
                        }

                        override fun onClick(widget: View) {
                            if (shown)
                                BrowseTagEvent(MediaBrowseFilterModel().also {
                                    it.tags = listOf(tag.name!!.trim())
                                }).postEvent
                            else {
                                spoilerSpans.forEach { it.shown = true }
                                widget.invalidate()
                            }
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            ds.isUnderlineText = true
                        }
                    }
                } else {
                    object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            BrowseTagEvent(MediaBrowseFilterModel().also {
                                it.tags = listOf(tag.name!!.trim())
                            }).postEvent
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            ds.isUnderlineText = true
                        }
                    }
                }

                spannableStringBuilder.setSpan(
                    clickableSpan,
                    current,
                    spannableStringBuilder.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                if (index < lastIndex)
                    spannableStringBuilder.append(", ")
            }

            mediaMetaList.add(MediaMetaCollection().also { col ->
                col.header = getString(R.string.tags)
                col.subTitleSpannable = spannableStringBuilder
            })
        }

    }

    private fun addStudioToMetaCollection(studios: List<MediaStudioModel>?) {
        if (studios == null || studios.isEmpty()) return
        val spannableStringBuilder = SpannableStringBuilder()
        val lastIndex = studios.size - 1

        studios.forEachIndexed { index, it ->
            val current = spannableStringBuilder.length
            spannableStringBuilder.append(it.studioName)

            spannableStringBuilder.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        BrowseStudioEvent(StudioMeta(it.studioId)).postEvent
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = DynamicTheme.getInstance().get().tintSurfaceColor
                    }
                },
                current,
                spannableStringBuilder.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            if (index < lastIndex)
                spannableStringBuilder.append(", ")
        }

        mediaMetaList.add(MediaMetaCollection().also { col ->
            col.header = getString(R.string.studios)
            col.subTitleSpannable = spannableStringBuilder
        })
    }


    private fun addProducerToMetaCollection(studios: List<MediaStudioModel>?) {
        if (studios == null || studios.isEmpty()) return
        val spannableStringBuilder = SpannableStringBuilder()
        val lastIndex = studios.size - 1
        studios.forEachIndexed { index, it ->
            val current = spannableStringBuilder.length
            spannableStringBuilder.append(it.studioName)

            spannableStringBuilder.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        BrowseStudioEvent(StudioMeta(it.studioId)).postEvent
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = DynamicTheme.getInstance().get().tintSurfaceColor
                    }
                },
                current,
                spannableStringBuilder.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            if (index < lastIndex)
                spannableStringBuilder.append(", ")
        }

        mediaMetaList.add(MediaMetaCollection().also { col ->
            col.header = getString(R.string.producers)
            col.subTitleSpannable = spannableStringBuilder
        })
    }


    private fun createRecommendationSource(): MediaOverviewRecommendationSource {
        return viewModel.createSource(recommendationField)
    }

    private fun invalidateRelationshipAdapter(list: List<MediaRelationshipModel>) {
        Adapter.builder(viewLifecycleOwner)
            .addSource(Source.fromList(list))
            .addPresenter(relationshipPresenter)
            .into(relationRecyclerView)
    }

    private fun invalidateRecommendationAdapter() {
        Adapter.builder(this, PAGE_SIZE_HINT)
            .setPager(PageSizePager(PAGE_SIZE))
            .addSource(recommendationSource)
            .addPresenter(recommendationPresenter)
            .addPresenter(loadingPresenter)
            .addPresenter(errorPresenter)
            .addPresenter(emptyPresenter)
            .into(recommendationRecyclerView)
    }


}