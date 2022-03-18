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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
import com.revolgenx.anilib.search.data.model.SearchFilterEventModel
import com.revolgenx.anilib.databinding.SearchFragmentLayoutBinding
import com.revolgenx.anilib.ui.selector.dialog.SelectableDialogFragment
import com.revolgenx.anilib.activity.event.ActivityEventListener
import com.revolgenx.anilib.common.data.meta.TagState
import com.revolgenx.anilib.common.data.model.FuzzyDateIntModel
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
import com.revolgenx.anilib.ui.view.*
import com.revolgenx.anilib.ui.view.widgets.checkbox.AlCheckBox
import com.revolgenx.anilib.util.loginContinue
import com.revolgenx.anilib.util.onItemSelected
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

// TODO add readable on for manga
class SearchFragment : BasePresenterFragment<BaseModel>(), ActivityEventListener {
    companion object {
        private const val SEARCH_FILTER_DATA_KEY = "SEARCH_FILTER_DATA_KEY"

        fun newInstance(searchFilterEventModel: SearchFilterEventModel?) = SearchFragment().also {
            it.arguments = bundleOf(SEARCH_FILTER_DATA_KEY to searchFilterEventModel)
        }
    }

    private val viewModel by viewModel<SearchFragmentViewModel>()
    private val searchFilterEventModel
        get() = arguments?.getParcelable<SearchFilterEventModel?>(
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
    private val filterModel get() = viewModel.filterModel
    private val handler = Handler(Looper.getMainLooper())

    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    private val yearGreater = 1970f
    private val yearLesser = Calendar.getInstance().get(Calendar.YEAR) + 1f

    private val episodesGreater = 0f
    private val episodesLesser by lazy {
        maxEpisodesPref.toFloat()
    }

    private val chaptersGreater = 0f
    private val chaptersLesser by lazy {
        maxChaptersPref.toFloat()
    }

    private val durationGreater = 0f
    private val durationLesser by lazy {
        maxDurationsPref.toFloat()
    }

    private val volumesGreater = 0f
    private val volumesLesser by lazy {
        maxVolumesPref.toFloat()
    }

    private var applyingFilter = false
    private val yearList by lazy {
        (yearLesser.toInt() downTo 1940).toList()
    }

    private val searchTypeAnime get() = filterModel.searchType == SearchTypes.ANIME
    private val searchTypeManga get() = filterModel.searchType == SearchTypes.MANGA

    private val genrePresenter by lazy {
        SearchTagPresenter(requireContext()).also {
            it.tagRemoved { genre ->
                filterModel.genreIn?.remove(genre)
                filterModel.genreNotIn?.remove(genre)
            }
        }
    }

    private val licensedByPresenter by lazy {
        SearchTagPresenter(requireContext()).also {
            it.tagRemoved { licensedBy ->
                if (filterModel.searchType == SearchTypes.ANIME) {
                    filterModel.streamingOn?.remove(licensedBy)
                } else if (filterModel.searchType == SearchTypes.MANGA) {
                    filterModel.readableOn?.remove(licensedBy)
                }
            }
        }
    }

    private val tagPresenter by lazy {
        SearchTagPresenter(requireContext()).also {
            it.tagRemoved { tag ->
                filterModel.tagsIn?.remove(tag)
                filterModel.tagsNotIn?.remove(tag)
            }
        }
    }

    private val streamingOnList by lazy {
        getUserStream()
    }

    private val readableOnList by lazy {
        getUserStream()
    }

    private val tagList by lazy {
        getUserTag()
    }

    private val genreList by lazy {
        getUserGenre()
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


    override var gridMaxSpan: Int = 6
    override var gridMinSpan: Int = 3

    override fun getItemSpanSize(position: Int) = if (adapter?.getItemViewType(position)?.let {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetBehavior = BottomSheetBehavior.from(sBinding.searchFilterBottomSheet)
        ViewCompat.setOnApplyWindowInsetsListener(
            sBinding.searchFilterBottomSheet
        ) { v, insets ->
            v.setPadding(
                v.paddingLeft,
                insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                v.paddingRight,
                v.bottom + insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            )
            bottomSheetBehavior?.peekHeight =
                insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            insets
        }

        hideBottomSheet()
        bottomSheetBehavior!!.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED, BottomSheetBehavior.STATE_HALF_EXPANDED, BottomSheetBehavior.STATE_SETTLING -> {
                        sBinding.bottomSheetDimLayout.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        hideBottomSheet()
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        sBinding.bottomSheetDimLayout.visibility = View.GONE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        filterModel.genreNotIn = getExcludedGenre().toMutableList()
        field.searchFilterModel.genreNotIn = filterModel.genreNotIn?.toMutableList()

        filterModel.tagsNotIn = getExcludedTags().toMutableList()
        field.searchFilterModel.tagsNotIn = filterModel.tagsNotIn?.toMutableList()

        filterModel.isHentai = if (canShowAdult()) null else false
        field.searchFilterModel.isHentai = filterModel.isHentai

        searchFilterEventModel?.let {
            it.genre?.let { genre ->
                filterModel.genreIn =
                    (field.searchFilterModel.genreIn ?: mutableListOf()).also { it.add(genre) }
                field.searchFilterModel.genreIn = filterModel.genreIn?.toMutableList()
            }
            it.tag?.let { tag ->
                filterModel.tagsIn =
                    (field.searchFilterModel.tagsIn ?: mutableListOf()).also { it.add(tag) }
                field.searchFilterModel.tagsIn = filterModel.tagsIn?.toMutableList()
            }
            filterModel.sort = it.sort
            field.searchFilterModel.sort = filterModel.sort
        }

        sBinding.bind()
        sBinding.bindFilter()
        sBinding.initListener()
        sBinding.initFilterListener()
    }

    private fun SearchFragmentLayoutBinding.bind() {
        searchTypeTabLayout.apply {
            listOf(
                R.string.anime,
                R.string.manga,
                R.string.character,
                R.string.staff,
                R.string.studio
            ).forEach {
                addTab(newTab().setText(it))
            }
            loginContinue(false) {
                addTab(newTab().setText(R.string.users))
            }

            getTabAt(field.searchFilterModel.searchType.ordinal)?.select()
        }
    }


    private fun SearchFragmentLayoutBinding.bindFilter() {
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
            mutableListOf(SelectableSpinnerMenu(getString(R.string.none), filterModel.year == null))
        yearList.map {
            searchYearItems.add(
                SelectableSpinnerMenu(
                    it.toString(),
                    it == filterModel.year
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

        filterTypeSpinner.setSelection(filterModel.searchType.ordinal)
        filterSeasonSpinner.setSelection(filterModel.season?.plus(1) ?: 0)
        filterFormatSpinner.setSelection(filterModel.format?.plus(1) ?: 0)
        filterStatusSpinner.setSelection(filterModel.status?.plus(1) ?: 0)
        filterSourceSpinner.setSelection(filterModel.source?.plus(1) ?: 0)
        filterYearSpinner.setSelection(filterModel.year?.let { yearList.indexOf(it) }
            ?.takeIf { it != -1 }?.plus(1) ?: 0)
        filterCountrySpinner.setSelection(filterModel.countryOfOrigin?.plus(1) ?: 0)

        filterYearSpinner.onItemSelected { position ->
            searchYearItems.firstOrNull { it.isSelected }?.isSelected = false
            searchYearItems[position].isSelected = true
            filterModel.year = position.takeIf { it > 0 }?.let {
                yearList[it - 1]
            }
        }


        hentaiCheckbox.visibility =
            if (canShowAdult()) View.VISIBLE else View.GONE

        hentaiCheckbox.updateState(if (filterModel.isHentai == null) false else filterModel.isHentai?.takeIf { it })

        doujinCheckbox.isChecked = filterModel.doujins == true

        val alMediaSorts = AlMediaSort.values()
        val alMediaSortList = requireContext().resources.getStringArray(R.array.al_media_sort)

        val mediaSortItems = alMediaSortList.mapIndexed { index, s ->
            AniLibSortingModel(alMediaSorts[index], s, SortOrder.NONE)
        }
        filterSortLayout.setSortItems(mediaSortItems)

        filterModel.sort?.let { sort ->
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
        minimumTagRank.value = filterModel.minimumTagRank?.toFloat() ?: 18f
        updateMinimumTagPercentageHeader()

        yearRangeSlider.values = listOf(
            filterModel.yearGreater?.year?.toFloat() ?: yearGreater,
            filterModel.yearLesser?.year?.toFloat() ?: yearLesser
        )

        updateYearRangeHeader()

        if (searchTypeAnime) {
            changeEpisodesRangeSlider()
            changeDurationRangeSlider()

            updateEpisodeRangeHeader()
            updateDurationRangeHeader()
        } else if (searchTypeManga) {
            changeChaptersRangeSlider()
            changeVolumeRangeSlider()

            updateChapterRangeHeader()
            updateVolumeRangeHeader()
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

        searchTypeTabLayout.doOnTabSelected(viewLifecycleOwner) { _, position ->
            if (applyingFilter) return@doOnTabSelected

            filterTypeSpinner.setSelection(position, false)
            filterModel.searchType = SearchTypes.values()[position]
            field.searchFilterModel.searchType = filterModel.searchType
            filter()
        }
    }

    private fun SearchFragmentLayoutBinding.initFilterListener() {
        applySearchHistory.setOnClickListener {
            viewModel.loadRecentField()
            bindFilter()
        }

        filterClearIv.setOnClickListener {
            filterSearchEt.setText("")
        }

        filterTypeSpinner.onItemSelected {
            val searchType = SearchTypes.values()[it]
            filterModel.searchType = searchType

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
            filterModel.season = it.takeIf { it > 0 }?.let { it - 1 }
        }

        filterStatusSpinner.onItemSelected {
            filterModel.status = it.takeIf { it > 0 }?.let { it - 1 }
        }

        filterFormatSpinner.onItemSelected {
            filterModel.format = it.takeIf { it > 0 }?.let { it - 1 }
        }

        filterSortLayout.onSortItemSelected = sort@{
            if (context == null) return@sort
            filterModel.sort = if (it != null) {
                if (it.order == SortOrder.DESC) {
                    (it.data as AlMediaSort).sort + 1
                } else {
                    (it.data as AlMediaSort).sort
                }
            } else null
        }
        filterSourceSpinner.onItemSelected {
            filterModel.source = it.takeIf { it > 0 }?.let { it - 1 }
        }

        filterCountrySpinner.onItemSelected {
            filterModel.countryOfOrigin = it.takeIf { it > 0 }?.let { it - 1 }
        }

        doujinCheckbox.setOnCheckedChangeListener(null)
        doujinCheckbox.setOnCheckedChangeListener { _, isChecked ->
            filterModel.doujins = isChecked
        }

        hentaiCheckbox.onCheckChangeListener = check@{
            context ?: return@check

            filterModel.isHentai = it.takeIf { it != AlCheckBox.CheckBoxState.UNCHECKED }?.let {
                it == AlCheckBox.CheckBoxState.CHECKED
            }
        }

        filterGenreChip.setOnClickListener {
            val selectableList = genreList.map {
                val state = when {
                    filterModel.genreIn?.contains(it) == true -> {
                        SelectedState.SELECTED
                    }
                    filterModel.genreNotIn?.contains(it) == true -> {
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
                    filterModel.genreIn = mutableListOf()
                    filterModel.genreNotIn = mutableListOf()
                    list.forEach { genrePair ->
                        if (genrePair.second == SelectedState.SELECTED) {
                            filterModel.genreIn!!.add(genrePair.first)
                        } else if (genrePair.second == SelectedState.INTERMEDIATE) {
                            filterModel.genreNotIn!!.add(genrePair.first)
                        }
                    }
                    invalidateGenreAdapter()
                }
            }
        }

        filterTagChip.setOnClickListener {
            val selectableList = tagList.map {
                val state = when {
                    filterModel.tagsIn?.contains(it) == true -> {
                        SelectedState.SELECTED
                    }
                    filterModel.tagsNotIn?.contains(it) == true -> {
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
                    filterModel.tagsIn = mutableListOf()
                    filterModel.tagsNotIn = mutableListOf()
                    list.forEach { pair ->
                        if (pair.second == SelectedState.SELECTED) {
                            filterModel.tagsIn!!.add(pair.first)
                        } else if (pair.second == SelectedState.INTERMEDIATE) {
                            filterModel.tagsNotIn!!.add(pair.first)
                        }
                    }
                    invalidateTagAdapter()
                }
            }
        }

        licensedByTagChip.setOnClickListener {
            if (searchTypeAnime) {
                val selectableList = streamingOnList.map {
                    val state = when {
                        filterModel.streamingOn?.contains(it) == true -> {
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
                        filterModel.streamingOn = mutableListOf()
                        list.forEach { pair ->
                            if (pair.second == SelectedState.SELECTED) {
                                filterModel.streamingOn!!.add(pair.first)
                            }
                        }
                        invalidateStreamingOnAdapter()
                    }
                }
            } else if (searchTypeManga) {
                val selectableList = readableOnList.map {
                    val state = when {
                        filterModel.readableOn?.contains(it) == true -> {
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
                        filterModel.readableOn = mutableListOf()
                        list.forEach { pair ->
                            if (pair.second == SelectedState.SELECTED) {
                                filterModel.readableOn!!.add(pair.first)
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
            filterModel.yearGreater = currentYearGreater.takeIf { it != yearGreater }
                ?.let { FuzzyDateIntModel(it.toInt(), 0, 0) }
            filterModel.yearLesser = currentYearLesser.takeIf { it != yearLesser }
                ?.let { FuzzyDateIntModel(it.toInt(), 0, 0) }

            updateYearRangeHeader()
        }

        episodeOrChapterRangeSlider.addOnChangeListener { slider, _, _ ->
            val currentEpisodeGreater = slider.values[0]
            val currentEpisodeLesser = slider.values[1]

            if (searchTypeAnime) {
                filterModel.episodesGreater =
                    currentEpisodeGreater.takeIf { it != episodesGreater }?.toInt()
                filterModel.episodesLesser =
                    currentEpisodeLesser.takeIf { it != episodesLesser }?.toInt()
                updateEpisodeRangeHeader()
            } else if (searchTypeManga) {
                filterModel.chaptersGreater =
                    currentEpisodeGreater.takeIf { it != chaptersGreater }?.toInt()
                filterModel.chaptersLesser =
                    currentEpisodeLesser.takeIf { it != chaptersLesser }?.toInt()

                updateChapterRangeHeader()
            }
        }

        durationOrVolumeRangeSlider.addOnChangeListener { slider, _, _ ->
            val currentDurationGreater = slider.values[0]
            val currentDurationLesser = slider.values[1]

            if (searchTypeAnime) {
                filterModel.durationGreater =
                    currentDurationGreater.takeIf { it != durationGreater }?.toInt()
                filterModel.durationLesser =
                    currentDurationLesser.takeIf { it != durationLesser }?.toInt()
                updateDurationRangeHeader()
            } else if (searchTypeManga) {
                filterModel.volumesGreater =
                    currentDurationGreater.takeIf { it != volumesGreater }?.toInt()
                filterModel.volumesLesser =
                    currentDurationLesser.takeIf { it != volumesLesser }?.toInt()
                updateVolumeRangeHeader()
            }
        }

        minimumTagRank.addOnChangeListener { _, value, _ ->
            filterModel.minimumTagRank = value.toInt()
            updateMinimumTagPercentageHeader()
        }

        yearRangeFilterHeader.setOnClickListener {
            yearRangeSlider.values = listOf(yearGreater, yearLesser)
        }

        episodeOrChapterRangeFilterHeader.setOnClickListener {
            episodeOrChapterRangeSlider.values =
                listOf(
                    if (searchTypeAnime) episodesGreater else chaptersGreater,
                    if (searchTypeAnime) episodesLesser else chaptersLesser
                )
        }

        durationOrVolumeRangeFilterHeader.setOnClickListener {
            durationOrVolumeRangeSlider.values =
                listOf(
                    if (searchTypeAnime) durationGreater else volumesGreater,
                    if (searchTypeAnime) durationLesser else volumesLesser
                )
        }

        minimumTagPercetageHeader.setOnClickListener {
            minimumTagRank.value = 18f
        }

        searchApplyFilter.setOnClickListener {
            applyFilter()
        }

    }

    private fun SearchFragmentLayoutBinding.updateMinimumTagPercentageHeader() {
        minimumTagPercetageHeader.text =
            getString(R.string.minimum_tag_percentage_s).format(filterModel.minimumTagRank ?: 18f)
    }

    private fun SearchFragmentLayoutBinding.updateYearRangeHeader() {
        if (filterModel.yearGreater != null || filterModel.yearLesser != null) {
            yearRangeFilterHeader.text = getString(R.string.year_range_s_s).format(
                filterModel.yearGreater?.year ?: yearGreater.toInt(),
                filterModel.yearLesser?.year ?: yearLesser.toInt()
            )
        } else {
            yearRangeFilterHeader.setText(R.string.year_range)
        }
    }

    private fun SearchFragmentLayoutBinding.updateEpisodeRangeHeader() {
        if (filterModel.episodesGreater != null || filterModel.episodesLesser != null) {
            episodeOrChapterRangeFilterHeader.text =
                getString(R.string.episodes_range_s_s).format(
                    filterModel.episodesGreater ?: episodesGreater.toInt(),
                    filterModel.episodesLesser ?: episodesLesser.toInt()
                )
        } else {
            episodeOrChapterRangeFilterHeader.setText(R.string.episodes)
        }
    }

    private fun SearchFragmentLayoutBinding.updateChapterRangeHeader() {
        if (filterModel.chaptersGreater != null || filterModel.chaptersLesser != null) {
            episodeOrChapterRangeFilterHeader.text =
                getString(R.string.chapters_range_s_s).format(
                    filterModel.chaptersGreater ?: chaptersGreater.toInt(),
                    filterModel.chaptersLesser ?: chaptersLesser.toInt()
                )
        } else {
            episodeOrChapterRangeFilterHeader.setText(R.string.chapters)
        }
    }

    private fun SearchFragmentLayoutBinding.updateVolumeRangeHeader() {
        if (filterModel.volumesGreater != null || filterModel.volumesLesser != null) {
            durationOrVolumeRangeFilterHeader.text =
                getString(R.string.volumes_range_s_s).format(
                    filterModel.volumesGreater ?: volumesGreater.toInt(),
                    filterModel.volumesLesser ?: volumesLesser.toInt()
                )
        } else {
            durationOrVolumeRangeFilterHeader.setText(R.string.volumes)
        }
    }

    private fun SearchFragmentLayoutBinding.updateDurationRangeHeader() {
        if (filterModel.durationGreater != null || filterModel.durationLesser != null) {
            durationOrVolumeRangeFilterHeader.text =
                getString(R.string.duration_range_s_s).format(
                    filterModel.durationGreater ?: durationGreater.toInt(),
                    filterModel.durationLesser ?: durationLesser.toInt()
                )
        } else {
            durationOrVolumeRangeFilterHeader.setText(R.string.duration)
        }
    }


    private fun SearchFragmentLayoutBinding.changeViewForAnime() {
        filterSeasonLayout.visibility = View.VISIBLE
        licensedByTagChip.setText(R.string.streaming_on)

        episodeOrChapterRangeSlider.valueFrom = episodesGreater
        episodeOrChapterRangeSlider.valueTo = episodesLesser
        durationOrVolumeRangeSlider.valueFrom = durationGreater
        durationOrVolumeRangeSlider.valueTo = durationLesser

        changeEpisodesRangeSlider()
        changeDurationRangeSlider()

        invalidateStreamingOnAdapter()
    }


    private fun SearchFragmentLayoutBinding.changeViewForManga() {
        licensedByTagChip.setText(R.string.readable_on)
        filterSeasonLayout.visibility = View.GONE

        episodeOrChapterRangeSlider.valueFrom = chaptersGreater
        episodeOrChapterRangeSlider.valueTo = chaptersLesser
        durationOrVolumeRangeSlider.valueFrom = volumesGreater
        durationOrVolumeRangeSlider.valueTo = volumesLesser

        changeChaptersRangeSlider()
        changeVolumeRangeSlider()

        invalidateReadableOnAdapter()
    }


    private fun SearchFragmentLayoutBinding.changeDurationRangeSlider() {
        durationOrVolumeRangeSlider.values = listOf(
            filterModel.durationGreater?.toFloat() ?: durationGreater,
            filterModel.durationLesser?.toFloat() ?: durationLesser
        )
    }

    private fun SearchFragmentLayoutBinding.changeVolumeRangeSlider() {
        durationOrVolumeRangeSlider.values = listOf(
            filterModel.volumesGreater?.toFloat() ?: volumesGreater,
            filterModel.volumesLesser?.toFloat() ?: volumesLesser
        )
    }

    private fun SearchFragmentLayoutBinding.changeChaptersRangeSlider() {
        episodeOrChapterRangeSlider.values = listOf(
            filterModel.chaptersGreater?.toFloat() ?: chaptersGreater,
            filterModel.chaptersLesser?.toFloat() ?: chaptersLesser
        )
    }

    private fun SearchFragmentLayoutBinding.changeEpisodesRangeSlider() {
        episodeOrChapterRangeSlider.values = listOf(
            filterModel.episodesGreater?.toFloat() ?: episodesGreater,
            filterModel.episodesLesser?.toFloat() ?: episodesLesser
        )
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
                filterModel.streamingOn?.contains(it) == true -> {
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
                filterModel.readableOn?.contains(it) == true -> {
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
                filterModel.genreIn?.contains(it) == true -> {
                    TagField(it, TagState.TAGGED)
                }
                filterModel.genreNotIn?.contains(it) == true -> {
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
                filterModel.tagsIn?.contains(it) == true -> {
                    TagField(it, TagState.TAGGED)
                }
                filterModel.tagsNotIn?.contains(it) == true -> {
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
    }

    private fun SearchFragmentLayoutBinding.applyFilter() {
        filterSearchEt.hideKeyboard()
        applyingFilter = true
        hideBottomSheet()
        viewModel.applyFilter()
        searchTypeTabLayout.getTabAt(field.searchFilterModel.searchType.ordinal)?.select()
        searchEt.setText(filterSearchEt.text?.toString())
        filter()
    }

    private fun changeLayoutManager() {
        layoutManager = when (field.searchFilterModel.searchType) {
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