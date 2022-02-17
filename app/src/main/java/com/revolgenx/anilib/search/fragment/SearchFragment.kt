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
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayoutManager
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
import com.revolgenx.anilib.common.ui.model.SelectableSpinnerMenu
import com.revolgenx.anilib.constant.AlMediaSort
import com.revolgenx.anilib.data.tuples.MutablePair
import com.revolgenx.anilib.search.presenter.SearchPresenter
import com.revolgenx.anilib.search.viewmodel.SearchFragmentViewModel
import com.revolgenx.anilib.ui.dialog.sorting.AniLibSortingModel
import com.revolgenx.anilib.ui.dialog.sorting.SortOrder
import com.revolgenx.anilib.search.presenter.SearchTagPresenter
import com.revolgenx.anilib.ui.selector.constant.SelectedState
import com.revolgenx.anilib.ui.view.hideKeyboard
import com.revolgenx.anilib.ui.view.makeSelectableSpinnerAdapter
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter
import com.revolgenx.anilib.ui.view.showKeyboard
import com.revolgenx.anilib.ui.view.widgets.checkbox.AlCheckBox
import com.revolgenx.anilib.util.onItemSelected
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

// TODO add readable on for manga
class SearchFragment : BasePresenterFragment<BaseModel>(), ActivityEventListener {
    companion object {
        private const val SEARCH_FILTER_DATA_KEY = "SEARCH_FILTER_DATA_KEY"

        fun newInstance(searchFilterModel: SearchFilterModel?) = SearchFragment().also {
            it.arguments = bundleOf(SEARCH_FILTER_DATA_KEY to searchFilterModel)
        }
    }

    private val viewModel by viewModel<SearchFragmentViewModel>()
    private val searchFilterModel
        get() = arguments?.getParcelable<SearchFilterModel?>(
            SEARCH_FILTER_DATA_KEY
        )

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
    override val autoAddLayoutManager: Boolean = false

    private var genreAdapter: Adapter? = null
    private var tagsAdapter: Adapter? = null
    private var streamingOnAdapter: Adapter? = null
    private var readableOnAdapter: Adapter? = null

    private val yearGreater = 1970f
    private val yearLesser = Calendar.getInstance().get(Calendar.YEAR) + 1f

    private val episodesGreater = 0f
    private val episodesLesser = 150f

    private val chaptersGreater = 0f
    private val chaptersLesser = 500f

    private val durationGreater = 0f
    private val durationLesser = 170f

    private val volumesGreater = 0f
    private val volumesLesser = 50f

    private var applyingFilter = false
    private val yearList by lazy {
        (yearLesser.toInt() downTo 1940).toList()
    }

    private val searchTypeAnime get() = field.searchType == SearchTypes.ANIME
    private val searchTypeManga get() = field.searchType == SearchTypes.MANGA

    private val genrePresenter by lazy {
        SearchTagPresenter(requireContext()).also {
            it.tagRemoved { genre ->
                field.genreIn?.remove(genre)
                field.genreNotIn?.remove(genre)
            }
        }
    }

    private val licensedByPresenter by lazy {
        SearchTagPresenter(requireContext()).also {
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
        SearchTagPresenter(requireContext()).also {
            it.tagRemoved { tag ->
                field.tagsIn?.remove(tag)
                field.tagsNotIn?.remove(tag)
            }
        }
    }

    private val streamingOnList by lazy {
        getUserStream(requireContext())
    }

    private val readableOnList by lazy {
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
        hideBottomSheet()
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
            field.isHentai = if (canShowAdult(requireContext())) null else false

            searchFilterModel?.let {
                it.genre?.let { genre ->
                    field.genreIn = (field.genreIn ?: mutableListOf()).also { it.add(genre) }
                }
                it.tag?.let { tag ->
                    field.tagsIn = (field.tagsIn ?: mutableListOf()).also { it.add(tag) }
                }
                field.sort = it.sort
            }
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

        val searchYearItems =
            mutableListOf(SelectableSpinnerMenu(getString(R.string.none), field.year == null))
        yearList.map {
            searchYearItems.add(
                SelectableSpinnerMenu(
                    it.toString(),
                    it == field.year
                )
            )
        }

        filterTypeSpinner.adapter = makeSpinnerAdapter(requireContext(), searchTypeItems)
        filterSeasonSpinner.adapter = makeSpinnerAdapter(requireContext(), searchSeasonItems)
        filterFormatSpinner.adapter = makeSpinnerAdapter(requireContext(), searchFormatItems)
        filterStatusSpinner.adapter = makeSpinnerAdapter(requireContext(), searchStatusItems)
        filterSourceSpinner.adapter = makeSpinnerAdapter(requireContext(), searchSourceItems)
        filterYearSpinner.adapter = makeSelectableSpinnerAdapter(requireContext(), searchYearItems)
        filterCountrySpinner.adapter = makeSpinnerAdapter(requireContext(), searchCountryItems)

        filterYearSpinner.onItemSelected { position ->
            searchYearItems.firstOrNull { it.isSelected }?.isSelected = false
            searchYearItems[position].isSelected = true
            field.year = position.takeIf { it > 0 }?.let {
                yearList[it - 1]
            }
        }

        hentaiCheckbox.visibility =
            if (canShowAdult(requireContext())) View.VISIBLE else View.GONE

        val alMediaSorts = AlMediaSort.values()
        val alMediaSortList = requireContext().resources.getStringArray(R.array.al_media_sort)

        val mediaSortItems = alMediaSortList.mapIndexed { index, s ->
            AniLibSortingModel(alMediaSorts[index], s, SortOrder.NONE)
        }
        filterSortLayout.setSortItems(mediaSortItems)

        field.sort?.let { sort ->
            var sortOrder = SortOrder.NONE
            val currentSort = if (sort < 34) {
                if (sort % 2 == 0) {
                    sortOrder = SortOrder.ASC
                    sort
                } else {
                    sortOrder = SortOrder.DESC
                    sort - 1
                }
            } else if (sort > 34) {
                if (sort % 2 == 0) {
                    sortOrder = SortOrder.DESC
                    sort - 1
                } else {
                    sortOrder = SortOrder.ASC
                    sort
                }
            } else {
                null
            }

            if (currentSort != null) {
                val currentSortEnum = alMediaSorts.first { it.sort == currentSort }
                AniLibSortingModel(
                    currentSortEnum,
                    alMediaSortList[currentSortEnum.ordinal],
                    sortOrder
                ).let { model ->
                    filterSortLayout.setActiveSortItem(model)
                }
            }
        }

        genreRecyclerView.layoutManager = FlexboxLayoutManager(requireContext())
        tagsRecyclerView.layoutManager = FlexboxLayoutManager(requireContext())
        licensedByRecyclerView.layoutManager = FlexboxLayoutManager(requireContext())
        invalidateGenreAdapter()
        invalidateTagAdapter()
        invalidateStreamingOnAdapter()

        yearRangeSlider.valueFrom = yearGreater
        yearRangeSlider.valueTo = yearLesser
        yearRangeSlider.stepSize = 1f
        yearRangeSlider.setLabelFormatter {
            it.toInt().toString()
        }

        episodeOrChapterRangeSlider.valueFrom = episodesGreater
        episodeOrChapterRangeSlider.valueTo = episodesLesser
        episodeOrChapterRangeSlider.stepSize = 1f
        episodeOrChapterRangeSlider.setLabelFormatter {
            it.toInt().toString()
        }

        durationOrVolumeRangeSlider.valueFrom = durationGreater
        durationOrVolumeRangeSlider.valueTo = durationLesser
        durationOrVolumeRangeSlider.stepSize = 1f
        durationOrVolumeRangeSlider.setLabelFormatter {
            it.toInt().toString()
        }

        minimumTagRank.setLabelFormatter {
            it.toInt().toString()
        }

        if (field.yearGreater != null || field.yearLesser != null) {
            yearRangeFilterHeader.text = getString(R.string.year_range_s_s).format(
                field.yearGreater ?: yearGreater.toInt(),
                field.yearLesser ?: yearLesser.toInt()
            )
        } else {
            yearRangeFilterHeader.setText(R.string.year_range)
        }

        if (field.searchType == SearchTypes.ANIME) {
            if (field.episodesGreater != null || field.episodesLesser != null) {
                episodeOrChapterRangeFilterHeader.text =
                    getString(R.string.episodes_range_s_s).format(
                        field.episodesGreater ?: episodesGreater.toInt(),
                        field.episodesLesser ?: episodesLesser.toInt()
                    )
            } else {
                episodeOrChapterRangeFilterHeader.setText(R.string.episodes)
            }
        } else if (field.searchType == SearchTypes.MANGA) {
            if (field.chaptersGreater != null || field.chaptersLesser != null) {
                episodeOrChapterRangeFilterHeader.text =
                    getString(R.string.chapters_range_s_s).format(
                        field.chaptersGreater ?: chaptersGreater.toInt(),
                        field.chaptersLesser ?: chaptersLesser.toInt()
                    )
            } else {
                episodeOrChapterRangeFilterHeader.setText(R.string.chapters)
            }
        }

        if (field.searchType == SearchTypes.ANIME) {
            if (field.durationGreater != null || field.durationLesser != null) {
                durationOrVolumeRangeFilterHeader.text =
                    getString(R.string.duration_range_s_s).format(
                        field.durationGreater ?: durationGreater.toInt(),
                        field.durationLesser ?: durationLesser.toInt()
                    )
            } else {
                durationOrVolumeRangeFilterHeader.setText(R.string.duration)
            }
        } else if (field.searchType == SearchTypes.MANGA) {
            if (field.volumesGreater != null || field.volumesLesser != null) {
                durationOrVolumeRangeFilterHeader.text =
                    getString(R.string.volumes_range_s_s).format(
                        field.volumesGreater ?: volumesGreater.toInt(),
                        field.volumesLesser ?: volumesLesser.toInt()
                    )
            } else {
                durationOrVolumeRangeFilterHeader.setText(R.string.volumes)
            }
        }

        if (field.minimumTagRank != null) {
            minimumTagPercetageHeader.text =
                getString(R.string.minimum_tag_percentage_s).format(field.minimumTagRank)
        } else {
            minimumTagPercetageHeader.setText(R.string.minimum_tag_percentage)
        }

        if (savedInstanceState == null) {
            yearRangeSlider.values = listOf(yearGreater, yearLesser)
            episodeOrChapterRangeSlider.values = listOf(episodesGreater, episodesLesser)
            durationOrVolumeRangeSlider.values = listOf(durationGreater, durationLesser)
        }

    }

    private fun hideBottomSheet() {
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun SearchFragmentLayoutBinding.initListener() {
        bottomSheetDimLayout.setOnClickListener {
            hideBottomSheet()
        }
        clearIv.setOnClickListener {
            searchEt.showKeyboard()
            searchEt.setText("")
        }

        searchEt.doOnTextChanged { text, _, _, _ ->
            if (field.search != text) {
                field.search = text?.toString()
            } else {
                return@doOnTextChanged
            }
            if (applyingFilter) {
                applyingFilter = false
                return@doOnTextChanged
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
            val searchType = SearchTypes.values()[it]
            if (field.searchType == searchType) return@onItemSelected
            field.searchType = searchType

            when (it) {
                0, 1 -> {
                    filterLayer1.visibility = View.VISIBLE
                    filterLayer2.visibility = View.VISIBLE
                    filterLayer3.visibility = View.VISIBLE
                    filterLayer4.visibility = View.VISIBLE
                    filterLayer5.visibility = View.VISIBLE
                    filterLayer6.visibility = View.VISIBLE
                    when (it) {
                        0 -> {
                            changeViewForAnime()
                        }
                        else -> {
                            changeViewForManga()
                        }
                    }
                }
                else -> {
                    changeViewForOtherSearchType()
                }
            }
        }

        filterSeasonSpinner.onItemSelected {
            field.season = it.takeIf { it > 0 }?.let { it - 1 }
        }

        filterStatusSpinner.onItemSelected {
            field.status = it.takeIf { it > 0 }?.let { it - 1 }
        }

        filterFormatSpinner.onItemSelected {
            field.format = it.takeIf { it > 0 }?.let { it - 1 }
        }

        filterSortLayout.onSortItemSelected = sort@{
            if (context == null) return@sort
            field.sort = if (it != null) {
                if (it.order == SortOrder.DESC) {
                    (it.data as AlMediaSort).sort + 1
                } else {
                    (it.data as AlMediaSort).sort
                }
            } else null
        }
        filterSourceSpinner.onItemSelected {
            field.source = it.takeIf { it > 0 }?.let { it - 1 }
        }

        filterCountrySpinner.onItemSelected {
            field.countryOfOrigin = it.takeIf { it > 0 }?.let { it - 1 }
        }

        doujinCheckbox.setOnCheckedChangeListener(null)
        doujinCheckbox.setOnCheckedChangeListener { _, isChecked ->
            field.doujins = isChecked
        }

        hentaiCheckbox.onCheckChangeListener = check@{
            context ?: return@check

            field.isHentai = it.takeIf { it != AlCheckBox.CheckBoxState.UNCHECKED }?.let {
                it == AlCheckBox.CheckBoxState.CHECKED
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
            openSelectorDialog(R.string.genre, selectableList, true) { list ->
                if (context != null) {
                    field.genreIn = mutableListOf()
                    field.genreNotIn = mutableListOf()
                    list.forEach { genrePair ->
                        if (genrePair.second == SelectedState.SELECTED) {
                            field.genreIn!!.add(genrePair.first)
                        } else if (genrePair.second == SelectedState.INTERMEDIATE) {
                            field.genreNotIn!!.add(genrePair.first)
                        }
                    }
                    invalidateGenreAdapter()
                }
            }
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
            openSelectorDialog(R.string.tags, selectableList, true) { list ->
                if (context != null) {
                    field.tagsIn = mutableListOf()
                    field.tagsNotIn = mutableListOf()
                    list.forEach { pair ->
                        if (pair.second == SelectedState.SELECTED) {
                            field.tagsIn!!.add(pair.first)
                        } else if (pair.second == SelectedState.INTERMEDIATE) {
                            field.tagsNotIn!!.add(pair.first)
                        }
                    }
                    invalidateTagAdapter()
                }
            }
        }

        licensedByTagChip.setOnClickListener {
            if (field.searchType == SearchTypes.ANIME) {
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
                openSelectorDialog(R.string.streaming_on, selectableList) { list ->
                    if (context != null) {
                        field.streamingOn = mutableListOf()
                        list.forEach { pair ->
                            if (pair.second == SelectedState.SELECTED) {
                                field.streamingOn!!.add(pair.first)
                            }
                        }
                        invalidateStreamingOnAdapter()
                    }
                }
            } else if (field.searchType == SearchTypes.MANGA) {
                val selectableList = readableOnList.map {
                    val state = when {
                        field.readableOn?.contains(it) == true -> {
                            SelectedState.SELECTED
                        }
                        else -> {
                            SelectedState.NONE
                        }
                    }
                    MutablePair(it, state)
                }

                openSelectorDialog(R.string.readable_on, selectableList) { list ->
                    if (context != null) {
                        field.streamingOn = mutableListOf()
                        list.forEach { pair ->
                            if (pair.second == SelectedState.SELECTED) {
                                field.streamingOn!!.add(pair.first)
                            }
                        }
                        invalidateReadableOnAdapter()
                    }
                }
            }
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

        episodeOrChapterRangeSlider.addOnChangeListener { slider, _, _ ->
            val currentEpisodeGreater = slider.values[0]
            val currentEpisodeLesser = slider.values[1]

            if (searchTypeAnime) {
                field.episodesGreater =
                    currentEpisodeGreater.takeIf { it != episodesGreater }?.toInt()
                field.episodesLesser = currentEpisodeLesser.takeIf { it != episodesLesser }?.toInt()

                if (field.episodesGreater != null || field.episodesLesser != null) {
                    episodeOrChapterRangeFilterHeader.text =
                        getString(R.string.episodes_range_s_s).format(
                            field.episodesGreater ?: episodesGreater.toInt(),
                            field.episodesLesser ?: episodesLesser.toInt()
                        )
                } else {
                    episodeOrChapterRangeFilterHeader.setText(R.string.episodes)
                }
            } else if (searchTypeManga) {
                field.chaptersGreater =
                    currentEpisodeGreater.takeIf { it != chaptersGreater }?.toInt()
                field.chaptersLesser = currentEpisodeLesser.takeIf { it != chaptersLesser }?.toInt()

                if (field.chaptersGreater != null || field.chaptersLesser != null) {
                    episodeOrChapterRangeFilterHeader.text =
                        getString(R.string.chapters_range_s_s).format(
                            field.chaptersGreater ?: chaptersGreater.toInt(),
                            field.chaptersLesser ?: chaptersLesser.toInt()
                        )
                } else {
                    episodeOrChapterRangeFilterHeader.setText(R.string.chapters)
                }
            }
        }

        durationOrVolumeRangeSlider.addOnChangeListener { slider, _, _ ->
            val currentDurationGreater = slider.values[0]
            val currentDurationLesser = slider.values[1]

            if (searchTypeAnime) {
                field.durationGreater =
                    currentDurationGreater.takeIf { it != durationGreater }?.toInt()
                field.durationLesser =
                    currentDurationLesser.takeIf { it != durationLesser }?.toInt()

                if (field.durationGreater != null || field.durationLesser != null) {
                    durationOrVolumeRangeFilterHeader.text =
                        getString(R.string.duration_range_s_s).format(
                            field.durationGreater ?: durationGreater.toInt(),
                            field.durationLesser ?: durationLesser.toInt()
                        )
                } else {
                    durationOrVolumeRangeFilterHeader.setText(R.string.duration)
                }
            } else if (searchTypeManga) {
                field.volumesGreater =
                    currentDurationGreater.takeIf { it != volumesGreater }?.toInt()
                field.volumesLesser =
                    currentDurationLesser.takeIf { it != volumesLesser }?.toInt()

                if (field.volumesGreater != null || field.volumesLesser != null) {
                    durationOrVolumeRangeFilterHeader.text =
                        getString(R.string.volumes_range_s_s).format(
                            field.volumesGreater ?: volumesGreater.toInt(),
                            field.volumesLesser ?: volumesLesser.toInt()
                        )
                } else {
                    durationOrVolumeRangeFilterHeader.setText(R.string.volumes)
                }
            }
        }

        minimumTagRank.addOnChangeListener { _, value, _ ->
            field.minimumTagRank = value.toInt()
            minimumTagPercetageHeader.text =
                getString(R.string.minimum_tag_percentage_s).format(field.minimumTagRank)
        }

        yearRangeFilterHeader.setOnClickListener {
            yearRangeSlider.values = listOf(yearGreater, yearLesser)
        }

        episodeOrChapterRangeFilterHeader.setOnClickListener {
            if (searchTypeAnime) {
                episodeOrChapterRangeSlider.values = listOf(episodesGreater, episodesLesser)
            } else if (searchTypeManga) {
                episodeOrChapterRangeSlider.values = listOf(chaptersGreater, chaptersLesser)
            }
        }

        durationOrVolumeRangeFilterHeader.setOnClickListener {
            durationOrVolumeRangeSlider.values = listOf(durationGreater, durationLesser)
        }

        minimumTagPercetageHeader.setOnClickListener {
            minimumTagRank.value = 18f
        }

        searchApplyFilter.setOnClickListener {
            applyFilter()
        }

    }


    private fun SearchFragmentLayoutBinding.changeViewForAnime() {
        filterSeasonLayout.visibility = View.VISIBLE
        licensedByTagChip.setText(R.string.streaming_on)
        episodeOrChapterRangeFilterHeader.setText(R.string.episodes)
        durationOrVolumeRangeFilterHeader.setText(R.string.duration)

        episodeOrChapterRangeSlider.valueFrom = episodesGreater
        episodeOrChapterRangeSlider.valueTo = episodesLesser
        episodeOrChapterRangeSlider.values = listOf(episodesGreater, episodesLesser)

        durationOrVolumeRangeSlider.valueFrom = durationGreater
        durationOrVolumeRangeSlider.valueTo = durationLesser
        durationOrVolumeRangeSlider.values = listOf(durationGreater, durationLesser)

        invalidateStreamingOnAdapter()
    }

    private fun SearchFragmentLayoutBinding.changeViewForManga() {
        licensedByTagChip.setText(R.string.readable_on)
        filterSeasonLayout.visibility = View.GONE
        episodeOrChapterRangeFilterHeader.setText(R.string.chapters)
        durationOrVolumeRangeFilterHeader.setText(R.string.volumes)

        episodeOrChapterRangeSlider.valueFrom = chaptersGreater
        episodeOrChapterRangeSlider.valueTo = chaptersLesser
        episodeOrChapterRangeSlider.values = listOf(chaptersGreater, chaptersLesser)

        durationOrVolumeRangeSlider.valueFrom = volumesGreater
        durationOrVolumeRangeSlider.valueTo = volumesLesser
        durationOrVolumeRangeSlider.values = listOf(volumesGreater, volumesLesser)

        invalidateReadableOnAdapter()
    }

    private fun SearchFragmentLayoutBinding.changeViewForOtherSearchType() {
        filterLayer1.visibility = View.INVISIBLE
        filterLayer2.visibility = View.INVISIBLE
        filterLayer3.visibility = View.INVISIBLE
        filterLayer4.visibility = View.INVISIBLE
        filterLayer5.visibility = View.INVISIBLE
        filterLayer6.visibility = View.INVISIBLE
        filterSeasonLayout.visibility = View.GONE
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


    private fun invalidateReadableOnAdapter() {
        val readableTagMap = readableOnList.mapNotNull {
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
                Source.fromList(readableTagMap)
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
        hasIntermediateMode: Boolean = false,
        callback: (selectedList: List<MutablePair<String, SelectedState>>) -> Unit
    ) {
        SelectableDialogFragment.newInstance(
            SelectableMeta(
                title = title,
                hasIntermediateMode = hasIntermediateMode,
                selectableItems = selectableItem
            )
        ).also {
            it.onSelectionDoneListener = callback
        }.show(childFragmentManager, SelectableDialogFragment::class.java.simpleName)
    }

    private fun search() {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            filter()
        }, 600)
    }

    private fun filter() {
        changeLayoutManager()
        createSource()
        invalidateAdapter()
        visibleToUser = true
    }

    private fun SearchFragmentLayoutBinding.applyFilter() {
        filterSearchEt.hideKeyboard()
        applyingFilter = true
        hideBottomSheet()
        searchEt.setText(filterSearchEt.text?.toString())
        filter()
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
                hideBottomSheet()
                true
            }
            else -> {
                false
            }
        }
    }

}