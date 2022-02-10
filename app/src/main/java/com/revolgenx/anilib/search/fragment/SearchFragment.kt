package com.revolgenx.anilib.search.fragment

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor
import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.search.data.field.SearchTypes
import com.revolgenx.anilib.ui.selector.data.meta.SelectableMeta
import com.revolgenx.anilib.common.data.field.TagField
import com.revolgenx.anilib.search.data.model.filter.SearchFilterModel
import com.revolgenx.anilib.databinding.SearchFragmentLayoutBinding
import com.revolgenx.anilib.ui.selector.dialog.SelectableDialogFragment
import com.revolgenx.anilib.activity.event.ActivityEventListener
import com.revolgenx.anilib.common.data.meta.TagState
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.constant.AlMediaSort
import com.revolgenx.anilib.data.tuples.MutablePair
import com.revolgenx.anilib.search.presenter.SearchPresenter
import com.revolgenx.anilib.search.viewmodel.SearchFragmentViewModel
import com.revolgenx.anilib.ui.dialog.sorting.AniLibSortingModel
import com.revolgenx.anilib.ui.dialog.sorting.SortOrder
import com.revolgenx.anilib.search.presenter.TagPresenter
import com.revolgenx.anilib.ui.selector.constant.SelectedState
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter
import com.revolgenx.anilib.util.onItemSelected
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class SearchFragment : BasePresenterFragment<BaseModel>(), ActivityEventListener {
    companion object {
        private const val SEARCH_FILTER_DATA_KEY = "SEARCH_FILTER_DATA_KEY"

        fun newInstance(searchFilterModel: SearchFilterModel?) = SearchFragment().also {
            it.arguments = bundleOf(SEARCH_FILTER_DATA_KEY to searchFilterModel)
        }
    }

    private val viewModel by viewModel<SearchFragmentViewModel>()

    override val basePresenter: Presenter<BaseModel>
        get() = SearchPresenter(requireContext(), viewLifecycleOwner)

    override val baseSource: Source<BaseModel>
        get() = viewModel.source ?: createSource()

    override val setHomeAsUp: Boolean = true
    override val menuRes: Int = R.menu.search_fragment_menu


    private var _sBinding: SearchFragmentLayoutBinding? = null
    private val sBinding get() = _sBinding!!

    private val field get() = viewModel.field
    private val handler = Handler(Looper.getMainLooper())

    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    override var autoAddLayoutManager: Boolean = false

    private var genreAdapter: Adapter? = null
    private var tagsAdapter: Adapter? = null
    private var streamingOnAdapter: Adapter? = null
    private var readableOnAdapter: Adapter? = null

    private val yearGreater = 1970f
    private val yearLesser = Calendar.getInstance().get(Calendar.YEAR) + 1f

    private val episodesGreater = 0f
    private val episodesLesser = 150f

    private val genrePresenter by lazy {
        TagPresenter(requireContext()).also {
            it.tagRemoved { genre ->
                field.genreIn?.remove(genre)
                field.genreNotIn?.remove(genre)
            }
        }
    }

    private val licensedByPresenter by lazy {
        TagPresenter(requireContext()).also {
            it.tagRemoved { licensedBy ->
                if (field.searchType == SearchTypes.ANIME) {
                    field.streamingOn?.remove(licensedBy)
                } else if (field.searchType == SearchTypes.MANGA) {
                    field.readableOn?.remove(licensedBy)
                }
            }
        }
    }

    private val tagPresenter by lazy {
        TagPresenter(requireContext()).also {
            it.tagRemoved { tag ->
                field.tagsIn?.remove(tag)
                field.tagsNotIn?.remove(tag)
            }
        }
    }

    private val streamingOnList by lazy {
        getUserStream(requireContext())
    }

    private val tagList by lazy {
        getUserTag(requireContext())
    }

    private val genreList by lazy {
        getUserGenre(requireContext())
    }

    override fun createSource(): Source<BaseModel> {
        return viewModel.createSource()
    }

    override fun getBaseToolbar(): Toolbar {
        return sBinding.dynamicToolbar
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        _sBinding = SearchFragmentLayoutBinding.inflate(inflater, container, false)
        sBinding.searchContainerFrameLayout.addView(v)
        sBinding.searchContainerFrameLayout.setBackgroundColor(dynamicBackgroundColor)
        setupSheetBackground(sBinding.searchFilterBottomSheet)
        return sBinding.root
    }


    private fun setupSheetBackground(view: View) {
        val model = ShapeAppearanceModel().toBuilder().apply {
            setTopRightCorner(CornerFamily.ROUNDED, 16f)
            setTopLeftCorner(CornerFamily.ROUNDED, 16f)
        }.build()

        val shape = MaterialShapeDrawable(model).apply {
            val backgroundColor = dynamicBackgroundColor
            fillColor = ColorStateList.valueOf(backgroundColor)
        }

        view.background = shape
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val span =
            if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 6 else 3
        layoutManager = GridLayoutManager(
            this.context,
            span
        ).also {
            it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapter?.getItemViewType(position)?.let {
                            it == SearchTypes.MANGA.ordinal
                                    || it == SearchTypes.ANIME.ordinal
                                    || it == SearchTypes.CHARACTER.ordinal
                                    || it == SearchTypes.STAFF.ordinal
                                    || it == SearchTypes.USER.ordinal
                        } == true) {
                        1
                    } else {
                        span
                    }
                }
            }
        }


        bottomSheetBehavior = BottomSheetBehavior.from(sBinding.searchFilterBottomSheet)
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior!!.peekHeight = 0
        bottomSheetBehavior!!.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED, BottomSheetBehavior.STATE_HALF_EXPANDED, BottomSheetBehavior.STATE_SETTLING -> {
                        sBinding.bottomSheetDimLayout.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_HIDDEN, BottomSheetBehavior.STATE_COLLAPSED -> {
                        sBinding.bottomSheetDimLayout.visibility = View.GONE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        if (savedInstanceState == null) {
            field.genreNotIn = getExcludedGenre(requireContext()).toMutableList()
            field.tagsNotIn = getExcludedTags(requireContext()).toMutableList()
        }

        sBinding.bind()
        sBinding.bindFilter(savedInstanceState)
        sBinding.initListener()
        sBinding.initFilterListener()
    }

    private fun SearchFragmentLayoutBinding.bind() {
    }


    private fun SearchFragmentLayoutBinding.bindFilter(savedInstanceState: Bundle?) {
        val searchTypeItems: List<DynamicMenu> =
            requireContext().resources.getStringArray(R.array.advance_search_type).map {
                DynamicMenu(null, it)
            }

        val searchSeasonItems: List<DynamicMenu> =
            requireContext().resources.getStringArray(R.array.advance_search_season).map {
                DynamicMenu(null, it)
            }

        val searchFormatItems: List<DynamicMenu> =
            requireContext().resources.getStringArray(R.array.advance_search_format).map {
                DynamicMenu(null, it)
            }

        val searchStatusItems: List<DynamicMenu> =
            requireContext().resources.getStringArray(R.array.advance_search_status).map {
                DynamicMenu(null, it)
            }

        val searchSourceItems: List<DynamicMenu> =
            requireContext().resources.getStringArray(R.array.advance_search_source).map {
                DynamicMenu(null, it)
            }
        val searchCountryItems: List<DynamicMenu> =
            requireContext().resources.getStringArray(R.array.advance_search_country).map {
                DynamicMenu(null, it)
            }

        filterTypeSpinner.adapter = makeSpinnerAdapter(requireContext(), searchTypeItems)
        filterSeasonSpinner.adapter = makeSpinnerAdapter(requireContext(), searchSeasonItems)
        filterFormatSpinner.adapter = makeSpinnerAdapter(requireContext(), searchFormatItems)
        filterStatusSpinner.adapter = makeSpinnerAdapter(requireContext(), searchStatusItems)
        filterSourceSpinner.adapter = makeSpinnerAdapter(requireContext(), searchSourceItems)

        val alMediaSorts = AlMediaSort.values()
        filterSortLayout.setSortItems(
            requireContext().resources.getStringArray(R.array.al_media_sort)
                .mapIndexed { index, s ->
                    AniLibSortingModel(alMediaSorts[index], s, SortOrder.NONE)
                })

        invalidateGenreAdapter()
        invalidateTagAdapter()
        invalidateStreamingOnAdapter()

        yearRangeSlider.valueFrom = yearGreater
        yearRangeSlider.valueTo = yearLesser
        yearRangeSlider.stepSize = 1f
        yearRangeSlider.setLabelFormatter {
            it.toInt().toString()
        }

        episodeRangeSlider.valueFrom = episodesGreater
        episodeRangeSlider.valueTo = episodesLesser
        episodeRangeSlider.stepSize = 1f

        if (field.yearGreater != null || field.yearLesser != null) {
            yearRangeFilterHeader.text = getString(R.string.year_range_s_s).format(
                field.yearGreater ?: yearGreater.toInt(),
                field.yearLesser ?: yearLesser.toInt()
            )
        } else {
            yearRangeFilterHeader.setText(R.string.year_range)
        }

        if (field.episodesGreater != null || field.episodesLesser != null) {
            episodeRangeFilterHeader.text = getString(R.string.episodes_range_s_s).format(
                field.episodesGreater ?: episodesGreater.toInt(),
                field.episodesLesser ?: episodesLesser.toInt()
            )
        } else {
            episodeRangeFilterHeader.setText(R.string.episodes)
        }

        if (savedInstanceState == null) {
            yearRangeSlider.values = listOf(yearGreater, yearLesser)
            episodeRangeSlider.values = listOf(episodesGreater, episodesLesser)
        }

    }

    private fun SearchFragmentLayoutBinding.initListener() {
        bottomSheetDimLayout.setOnClickListener {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }
        clearIv.setOnClickListener {
            searchEt.setText("")
        }

        searchEt.doOnTextChanged { text, _, _, _ ->
            if (field.search != text) {
                field.search = text?.toString()
            }
            filterSearchEt.setText(text)
            search()
        }
    }

    private fun SearchFragmentLayoutBinding.initFilterListener() {
        filterClearIv.setOnClickListener {
            filterSearchEt.setText("")
        }

        filterTypeSpinner.onItemSelected {
            when (it) {
                0, 1 -> {
                    filterLayer1.visibility = View.VISIBLE
                    filterLayer2.visibility = View.VISIBLE
                    filterSeasonLayout.visibility = View.VISIBLE
                }
                else -> {
                    filterLayer1.visibility = View.GONE
                    filterLayer2.visibility = View.GONE
                    filterSeasonLayout.visibility = View.GONE
                }
            }
        }

        filterGenreChip.setOnClickListener {
            val selectableList = genreList.map {
                val state = when {
                    field.genreIn?.contains(it) == true -> {
                        SelectedState.SELECTED
                    }
                    field.genreNotIn?.contains(it) == true -> {
                        SelectedState.INTERMEDIATE
                    }
                    else -> {
                        SelectedState.NONE
                    }
                }
                MutablePair(it, state)
            }
            openSelectorDialog(R.string.genre, selectableList, true)
        }

        filterTagChip.setOnClickListener {
            val selectableList = tagList.map {
                val state = when {
                    field.tagsIn?.contains(it) == true -> {
                        SelectedState.SELECTED
                    }
                    field.tagsNotIn?.contains(it) == true -> {
                        SelectedState.INTERMEDIATE
                    }
                    else -> {
                        SelectedState.NONE
                    }
                }
                MutablePair(it, state)
            }
            openSelectorDialog(R.string.tags, selectableList, true)
        }

        licensedByTagChip.setOnClickListener {
            val selectableList = streamingOnList.map {
                val state = when {
                    field.streamingOn?.contains(it) == true -> {
                        SelectedState.SELECTED
                    }
                    else -> {
                        SelectedState.NONE
                    }
                }
                MutablePair(it, state)
            }
            openSelectorDialog(R.string.streaming_on, selectableList)
        }

        yearRangeSlider.addOnChangeListener { slider, _, _ ->
            val currentYearGreater = slider.values[0]
            val currentYearLesser = slider.values[1]
            field.yearGreater = currentYearGreater.takeIf { it != yearGreater }?.toInt()
            field.yearLesser = currentYearLesser.takeIf { it != yearLesser }?.toInt()

            if (field.yearGreater != null || field.yearLesser != null) {
                yearRangeFilterHeader.text = getString(R.string.year_range_s_s).format(
                    field.yearGreater ?: yearGreater.toInt(),
                    field.yearLesser ?: yearLesser.toInt()
                )
            } else {
                yearRangeFilterHeader.setText(R.string.year_range)
            }
        }

        episodeRangeSlider.addOnChangeListener { slider, _, _ ->
            val currentEpisodeGreater = slider.values[0]
            val currentEpisodeLesser = slider.values[1]
            field.episodesGreater = currentEpisodeGreater.takeIf { it != episodesGreater }?.toInt()
            field.episodesLesser = currentEpisodeLesser.takeIf { it != episodesLesser }?.toInt()

            if (field.episodesGreater != null || field.episodesLesser != null) {
                episodeRangeFilterHeader.text = getString(R.string.episodes_range_s_s).format(
                    field.episodesGreater ?: episodesGreater.toInt(),
                    field.episodesLesser ?: episodesLesser.toInt()
                )
            } else {
                episodeRangeFilterHeader.setText(R.string.episodes)
            }
        }

        yearRangeFilterHeader.setOnClickListener {
            yearRangeSlider.values = listOf(yearGreater, yearLesser)
        }

        episodeRangeFilterHeader.setOnClickListener {
            episodeRangeSlider.values = listOf(episodesGreater, episodesLesser)
        }
    }


    private fun invalidateStreamingOnAdapter() {
        val streamingOnTagMap = streamingOnList.mapNotNull {
            when {
                field.streamingOn?.contains(it) == true -> {
                    TagField(it, TagState.TAGGED)
                }
                else -> {
                    null
                }
            }
        }
        Adapter.builder(this)
            .addSource(
                Source.fromList(streamingOnTagMap)
            )
            .addPresenter(licensedByPresenter)
            .into(sBinding.licensedByRecyclerView)
    }

    private fun invalidateGenreAdapter() {
        val genreTagMap = genreList.mapNotNull {
            when {
                field.genreIn?.contains(it) == true -> {
                    TagField(it, TagState.TAGGED)
                }
                field.genreNotIn?.contains(it) == true -> {
                    TagField(it, TagState.UNTAGGED)
                }
                else -> {
                    null
                }
            }
        }

        Adapter.builder(this)
            .addSource(
                Source.fromList(genreTagMap)
            )
            .addPresenter(genrePresenter)
            .into(sBinding.genreRecyclerView)
    }


    private fun invalidateTagAdapter() {
        val tagTagMap = tagList.mapNotNull {
            when {
                field.tagsIn?.contains(it) == true -> {
                    TagField(it, TagState.TAGGED)
                }
                field.tagsNotIn?.contains(it) == true -> {
                    TagField(it, TagState.UNTAGGED)
                }
                else -> {
                    null
                }
            }
        }
        Adapter.builder(this)
            .addSource(
                Source.fromList(tagTagMap)
            )
            .addPresenter(tagPresenter)
            .into(sBinding.tagsRecyclerView)
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_filter_iv -> {
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
                true
            }
            else -> super.onToolbarMenuSelected(item)
        }
    }

    private fun openSelectorDialog(
        title: Int,
        selectableItem: List<MutablePair<String, SelectedState>>,
        hasIntermediateMode: Boolean = false
    ) {
        SelectableDialogFragment.newInstance(
            SelectableMeta(
                title = title,
                hasIntermediateMode = hasIntermediateMode,
                selectableItems = selectableItem
            )
        ).show(childFragmentManager, SelectableDialogFragment::class.java.simpleName)
    }

    fun search() {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            filter()
        }, 400)
    }

    fun filter() {
        changeLayoutManager()
        createSource()
        invalidateAdapter()
        visibleToUser = true
    }

    private fun changeLayoutManager() {
        layoutManager = when (field.searchType) {
            SearchTypes.STUDIO -> {
                layoutManager.takeIf { it is LinearLayoutManager }
                    ?: LinearLayoutManager(requireContext())
            }
            else -> {
                if (layoutManager is GridLayoutManager) {
                    layoutManager
                } else {
                    val span =
                        if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 6 else 3
                    GridLayoutManager(this.context, span).also {
                        it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
                                return if (adapter?.getItemViewType(position) == 0) {
                                    1
                                } else {
                                    span
                                }
                            }
                        }
                    }
                }
            }
        }
        baseRecyclerView.layoutManager = layoutManager
    }

    override fun onBackPressed(): Boolean {
        return when (bottomSheetBehavior?.state) {
            BottomSheetBehavior.STATE_EXPANDED, BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
                true
            }
            else -> {
                false
            }
        }
    }

}