package com.revolgenx.anilib.media.fragment

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.otaliastudios.elements.pagers.PageSizePager
import com.revolgenx.anilib.ui.view.widgets.AlCardView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.contrastAccentWithSurface
import com.revolgenx.anilib.common.preference.enableAutoMlTranslation
import com.revolgenx.anilib.common.preference.enableMlTranslation
import com.revolgenx.anilib.common.preference.inUseMlLanguageModel
import com.revolgenx.anilib.constant.*
import com.revolgenx.anilib.media.data.field.MediaOverviewField
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.databinding.MediaOverviewFragmentBinding
import com.revolgenx.anilib.common.event.OpenStudioEvent
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.media.presenter.MediaInfoRelationshipPresenter
import com.revolgenx.anilib.media.presenter.MediaExternalLinkPresenter
import com.revolgenx.anilib.media.presenter.MediaMetaPresenter
import com.revolgenx.anilib.media.presenter.MediaRecommendationPresenter
import com.revolgenx.anilib.common.viewmodel.getViewModelOwner
import com.revolgenx.anilib.infrastructure.source.MediaOverviewRecommendationSource
import com.revolgenx.anilib.media.data.model.*
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.ui.adapter.MediaGenreChipAdapter
import com.revolgenx.anilib.ui.adapter.MediaTagChipAdapter
import com.revolgenx.anilib.ui.dialog.MediaTagDescriptionDialog
import com.revolgenx.anilib.media.presenter.MediaOverviewCharacterPresenter
import com.revolgenx.anilib.media.viewmodel.MediaInfoContainerSharedVM
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.ui.view.airing.AiringEpisodeView
import com.revolgenx.anilib.media.viewmodel.MediaOverviewVM
import com.revolgenx.anilib.studio.data.model.StudioEdgeModel
import com.revolgenx.anilib.util.getParcelableCompat
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaOverviewFragment : BaseLayoutFragment<MediaOverviewFragmentBinding>() {

    private val mediaBrowserMeta: MediaInfoMeta?
        get() = arguments?.getParcelableCompat(
            MEDIA_INFO_META_KEY
        )

    private val viewModel by viewModel<MediaOverviewVM>()
    private val sharedViewModel by viewModel<MediaInfoContainerSharedVM>(owner = getViewModelOwner())

    private val recommendationSource: MediaOverviewRecommendationSource
        get() = viewModel.source ?: createRecommendationSource()
    private val relationshipPresenter by lazy {
        MediaInfoRelationshipPresenter(requireContext())
    }

    private val genreChipAdapter = MediaGenreChipAdapter()
    private val tagsChipAdapter = MediaTagChipAdapter(::onTagInfoClicked)
    private val characterPresenter by lazy {
        MediaOverviewCharacterPresenter(requireContext())
    }

    private val recommendationPresenter by lazy {
        MediaRecommendationPresenter(
            viewLifecycleOwner,
            requireContext(),
            viewModel
        )
    }
    private val loadingPresenter: Presenter<Unit> by lazy {
        Presenter.forLoadingIndicator(
            requireContext(),
            R.layout.loading_layout
        )
    }

    private val mediaMetaList by lazy {
        mutableListOf<MediaMetaCollection>()
    }

    private val errorPresenter: Presenter<Unit> by lazy {
        Presenter.forErrorIndicator(requireContext(), R.layout.error_layout)
    }

    private val emptyPresenter: Presenter<Unit> by lazy {
        Presenter.forEmptyIndicator(requireContext(), R.layout.empty_layout)
    }


    private var metaLinkAdapter: Adapter? = null
    private var metaContainerAdapter: Adapter? = null

    companion object {
        private const val MEDIA_INFO_META_KEY = "MEDIA_INFO_META_KEY"
        fun newInstance(meta: MediaInfoMeta) = MediaOverviewFragment().also {
            it.arguments = bundleOf(MEDIA_INFO_META_KEY to meta)
        }
    }


    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): MediaOverviewFragmentBinding {
        return MediaOverviewFragmentBinding.inflate(inflater, parent, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val span =
            if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
        binding.metaContainerRecyclerView.layoutManager =
            GridLayoutManager(
                this.context,
                span
            ).also {
                it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (metaContainerAdapter?.getItemViewType(position) == 0) {
                            1
                        } else {
                            span
                        }
                    }
                }
            }

        binding.metaLinkRecyclerView.layoutManager = FlexboxLayoutManager(requireContext())

        binding.recommendationRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.relationRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        mediaBrowserMeta ?: return

        viewModel.field.mediaId = mediaBrowserMeta!!.mediaId

        binding.initView()

        viewModel.mediaOverviewLiveData.observe(viewLifecycleOwner) { res ->
            sharedViewModel.mediaLiveData.value = res
            when (res) {
                is Resource.Success -> {
                    binding.resourceStatusLayout.resourceStatusContainer.visibility = View.GONE
                    binding.resourceStatusLayout.resourceProgressLayout.progressLayout.visibility =
                        View.VISIBLE
                    binding.resourceStatusLayout.resourceErrorLayout.errorLayout.visibility =
                        View.GONE
                    binding.updateView(res.data!!)
                    invalidateRecommendationAdapter()
                }
                is Resource.Error -> {
                    binding.resourceStatusLayout.resourceStatusContainer.visibility = View.VISIBLE
                    binding.resourceStatusLayout.resourceProgressLayout.progressLayout.visibility =
                        View.GONE
                    binding.resourceStatusLayout.resourceErrorLayout.errorLayout.visibility =
                        View.VISIBLE
                    invalidateRecommendationAdapter()
                }
                is Resource.Loading -> {
                    binding.resourceStatusLayout.resourceStatusContainer.visibility = View.VISIBLE
                    binding.resourceStatusLayout.resourceProgressLayout.progressLayout.visibility =
                        View.VISIBLE
                    binding.resourceStatusLayout.resourceErrorLayout.errorLayout.visibility =
                        View.GONE
                }
            }
        }

        viewModel.getOverview(MediaOverviewField().also {
            it.mediaId = mediaBrowserMeta!!.mediaId
        })

    }

    private fun MediaOverviewFragmentBinding.initView() {
        genreRecyclerView.layoutManager = FlexboxLayoutManager(requireContext())
        mediaTagsRecyclerView.layoutManager = FlexboxLayoutManager(requireContext())
        mediaCharacterRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        genreRecyclerView.adapter = genreChipAdapter
        mediaTagsRecyclerView.adapter = tagsChipAdapter
    }

    private fun bindGenre(genres: List<String>?) {
        genreChipAdapter.submitList(genres)
    }

    private fun bindTags(tags: List<MediaTagModel>?) {
        tagsChipAdapter.submitList(tags)
    }

    private fun MediaOverviewFragmentBinding.updateView(overview: MediaModel) {
        context ?: return

        nativeTitle.subtitle = overview.title?.native.naText()
        englishTitle.subtitle = overview.title?.english.naText()

        setDescription(overview.description)

        mediaFormatTv.subtitle = overview.format?.let {
            requireContext().resources.getStringArray(R.array.media_format)[it]
        }.naText()
        mediaSourceTv.subtitle = overview.source?.let {
            requireContext().resources.getStringArray(R.array.media_source)[it.ordinal]
        }.naText()

        when (overview.type) {
            AlMediaType.ANIME.ordinal -> {
                mediaEpisodeTv.subtitle = overview.episodes.naText()
                mediaDurationTv.subtitle =
                    getString(R.string.min_s).format(overview.duration.naText())
            }
            AlMediaType.MANGA.ordinal -> {
                mediaEpisodeTv.title = getString(R.string.chapters)
                mediaEpisodeTv.subtitle = overview.chapters.naText()
                mediaDurationTv.title = getString(R.string.volumes)
                mediaDurationTv.subtitle = overview.volumes.naText()
            }
        }

        if (overview.characters?.edges.isNullOrEmpty()) {
            characterHeaderTv.visibility = View.GONE
            mediaCharacterRecyclerView.visibility = View.GONE
        } else {
            Adapter.builder(viewLifecycleOwner)
                .addSource(Source.fromList(overview.characters!!.edges!!))
                .addPresenter(characterPresenter)
                .addPresenter(emptyPresenter)
                .into(mediaCharacterRecyclerView)
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
                trailerLayout.visibility = View.GONE
                trailerHeaderTv.visibility = View.GONE
            }
        }

        mediaStatusTv.subtitle = overview.status?.let {
            requireContext().resources.getStringArray(R.array.media_status)[it]
        } ?: "?"

        startDateTv.subtitle = overview.startDate?.toString().naText()
        endDateTv.subtitle = overview.endDate?.toString().naText()

        overview.nextAiringEpisode?.let { atModel ->
            airingContainer.visibility = View.VISIBLE
            airingContainer.addView(
                AlCardView(requireContext()).also {
                    it.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    it.useCompatPadding = true
                }.also {
                    it.addView(
                        AiringEpisodeView(
                            requireContext()
                        ).also { ae ->
                            ae.layoutParams = FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            ).also { params ->
                                params.gravity = Gravity.CENTER
                            }
                            ae.setTimer(atModel)
                        })
                })
        }

        if (overview.relations?.edges.isNullOrEmpty()) {
            relationRecyclerView.visibility = View.GONE
            relationshipHeaderTv.visibility = View.GONE
        } else {
            invalidateRelationshipAdapter(overview.relations!!.edges!!)
        }


        if (mediaMetaList.isEmpty()) {
            addToMediaCollection(getString(R.string.romaji_title), overview.title?.romaji.naText())
            overview.hashtag?.let {
                addToMediaCollection(getString(R.string.hashtag), it)
            }

            addToMediaCollection(R.string.average_score, "${overview.averageScore ?: 0} %")
            addToMediaCollection(R.string.mean_score, "${overview.meanScore ?: 0} %")
            addToMediaCollection(R.string.popularity, "${overview.popularity ?: 0}")
            addToMediaCollection(R.string.favourites, "${overview.favourites ?: 0}")

            addSynonymsToMetaCollection(overview.synonyms)
            addStudioToMetaCollection(overview.studios?.edges?.filter { it.isMain })
            addProducerToMetaCollection(overview.studios?.edges?.filter { !it.isMain })

            bindGenre(overview.genres)


            if (overview.tags.isNullOrEmpty()) {
                mediaTagsHeaderTv.visibility = View.GONE
                mediaTagsRecyclerView.visibility = View.GONE
            } else {
                bindTags(overview.tags)
            }

        }


        metaContainerAdapter = Adapter.builder(viewLifecycleOwner)
            .addSource(Source.fromList(mediaMetaList))
            .addPresenter(
                MediaMetaPresenter(
                    requireContext()
                )
            )
            .into(metaContainerRecyclerView)


        if (overview.externalLinks.isNullOrEmpty()) {
            linkHeaderTv.visibility = View.GONE
            metaLinkRecyclerView.visibility = View.GONE
        } else {
            overview.externalLinks?.let {
                metaLinkAdapter = Adapter.builder(viewLifecycleOwner)
                    .addSource(Source.fromList(it))
                    .addPresenter(
                        MediaExternalLinkPresenter(
                            requireContext()
                        )
                    )
                    .into(metaLinkRecyclerView)
            }
        }


    }

    private fun MediaOverviewFragmentBinding.setDescription(description: String?) {

        if (description.isNullOrBlank()) {
            translateSwitch.visibility = View.GONE
            showTranslatedByGoogle(false)
            mediaDescriptionTv.setText(R.string.no_description)
//            mediaDescriptionView.descriptionTv.setText(R.string.no_description)
            return
        }

        val enableMlTranslation = enableMlTranslation()
        val isInUseModel = inUseMlLanguageModel(requireContext())

        if (enableMlTranslation) {
            if (isInUseModel.isEmpty()) {
                translateSwitch.visibility = View.GONE
                showTranslatedByGoogle(false)
                setMarkdownDescription(description)
            } else {
                translateSwitch.visibility = View.VISIBLE
                translateSwitch.setOnCheckedChangeListener(null)
                val enableAutoTranslate = enableAutoMlTranslation()
                translateSwitch.isChecked = enableAutoTranslate

                if (enableAutoTranslate) {
                    translateDescription(description)
                } else {
                    showTranslatedByGoogle(false)
                    setMarkdownDescription(description)
                }

                translateSwitch.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        translateDescription(description)
                    } else {
                        showTranslatedByGoogle(isChecked)
                        setMarkdownDescription(description)
                    }
                }
            }
        } else {
            translateSwitch.visibility = View.GONE
            showTranslatedByGoogle(false)
            setMarkdownDescription(description)
        }
    }

    private fun MediaOverviewFragmentBinding.setMarkdownDescription(
        description: String
    ) {
        AlMarkwonFactory.getMarkwon()
            .setMarkdown(mediaDescriptionTv, description)
//        AlMarkwonFactory.getMarkwon()
//            .setMarkdown(mediaDescriptionView.descriptionTv, description)
//        mediaDescriptionView.updateLayout()
    }

    private fun translateDescription(description: String) {
        binding.mediaDescriptionTv.text = getString(R.string.translating)
//        binding.mediaDescriptionView.descriptionTv.text = getString(R.string.translating)

        viewModel.translationStore.translate(requireContext(), description)
            .observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.mediaDescriptionTv.text = it
//                    binding.mediaDescriptionView.descriptionTv.text = it
                    showTranslatedByGoogle(true)
                }
            }
    }

    private fun showTranslatedByGoogle(b: Boolean) {
        binding.translatedByGoogleSign.visibility = if (b) View.VISIBLE else View.GONE
    }


    private fun addToMediaCollection(header: String, subtitle: String) {
        mediaMetaList.add(MediaMetaCollection().also { col ->
            col.header = header
            col.subTitle = subtitle
        })
    }

    private fun addToMediaCollection(@StringRes header: Int, subtitle: String) {
        mediaMetaList.add(MediaMetaCollection().also { col ->
            col.header = requireContext().getString(header)
            col.subTitle = subtitle
        })
    }


    private fun addSynonymsToMetaCollection(synonyms: List<String>?) {
        synonyms?.takeIf { it.isNotEmpty() }?.joinToString("\n")?.let { jSynonyms ->
            mediaMetaList.add(MediaMetaCollection().also { col ->
                col.header = getString(R.string.synonyms)
                col.subTitle = jSynonyms
            })
        }
    }

    private fun addStudioToMetaCollection(studios: List<StudioEdgeModel>?) {
        if (studios == null || studios.isEmpty()) return
        val spannableStringBuilder = SpannableStringBuilder()
        val lastIndex = studios.size - 1

        studios.forEachIndexed { index, it ->
            val current = spannableStringBuilder.length
            spannableStringBuilder.append(it.node?.studioName)

            spannableStringBuilder.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        OpenStudioEvent(it.node?.id!!).postEvent
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = contrastAccentWithSurface
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

    private fun onTagInfoClicked(model: MediaTagModel) {
        MediaTagDescriptionDialog.newInstance(model).show(requireContext())
    }


    private fun addProducerToMetaCollection(studios: List<StudioEdgeModel>?) {
        if (studios == null || studios.isEmpty()) return
        val spannableStringBuilder = SpannableStringBuilder()
        val lastIndex = studios.size - 1
        studios.forEachIndexed { index, it ->
            val current = spannableStringBuilder.length
            spannableStringBuilder.append(it.node?.studioName)

            spannableStringBuilder.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        OpenStudioEvent(it.node?.id!!).postEvent
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = contrastAccentWithSurface
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
        return viewModel.createSource()
    }

    private fun invalidateRelationshipAdapter(list: List<MediaEdgeModel>) {
        Adapter.builder(viewLifecycleOwner)
            .addSource(Source.fromList(list))
            .addPresenter(relationshipPresenter)
            .addResourceLoader()
            .into(binding.relationRecyclerView)
    }

    private fun invalidateRecommendationAdapter() {
        Adapter.builder(this, PAGE_SIZE_HINT)
            .setPager(PageSizePager(PAGE_SIZE))
            .addSource(recommendationSource)
            .addPresenter(recommendationPresenter)
            .addResourceLoader()
            .into(binding.recommendationRecyclerView)
    }

    private fun Adapter.Builder.addResourceLoader(): Adapter.Builder {
        addPresenter(loadingPresenter)
        addPresenter(errorPresenter)
        addPresenter(emptyPresenter)
        return this
    }


}