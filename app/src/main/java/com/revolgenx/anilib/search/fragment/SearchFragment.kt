package com.revolgenx.anilib.search.fragment

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.paulrybitskyi.persistentsearchview.adapters.model.SuggestionItem
import com.paulrybitskyi.persistentsearchview.listeners.OnSuggestionChangeListener
import com.paulrybitskyi.persistentsearchview.utils.SuggestionCreationUtil
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.contrastAccentWithBg
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary
import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.constant.MediaTagFilterTypes
import com.revolgenx.anilib.constant.SearchTypes
import com.revolgenx.anilib.common.data.field.TagChooserField
import com.revolgenx.anilib.common.data.field.TagField
import com.revolgenx.anilib.search.data.model.filter.SearchFilterModel
import com.revolgenx.anilib.search.data.model.filter.StudioSearchFilterModel
import com.revolgenx.anilib.databinding.SearchFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.event.TagEvent
import com.revolgenx.anilib.ui.dialog.TagChooserDialogFragment
import com.revolgenx.anilib.activity.event.ActivityEventListener
import com.revolgenx.anilib.search.presenter.SearchPresenter
import com.revolgenx.anilib.ui.view.navigation.BrowseFilterNavigationView
import com.revolgenx.anilib.search.viewmodel.SearchFragmentViewModel
import com.revolgenx.anilib.util.DataProvider
import com.revolgenx.anilib.util.EventBusListener
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BasePresenterFragment<BaseModel>(),
    BrowseFilterNavigationView.AdvanceBrowseNavigationCallbackListener, EventBusListener,
    ActivityEventListener {

    private val viewModel by viewModel<SearchFragmentViewModel>()

    override val basePresenter: Presenter<BaseModel>
        get() = SearchPresenter(requireContext(), viewLifecycleOwner)

    override val baseSource: Source<BaseModel>
        get() = viewModel.source ?: createSource()


    private val dataProvider by lazy {
        DataProvider(requireContext())
    }

    private val tagAdapter: Adapter.Builder
        get() {
            return Adapter.builder(this)
        }

    private val backDrawable: Drawable
        get() = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)!!.also {
            it.setTint(dynamicTextColorPrimary)
        }

    private val filterDrawable: Drawable
        get() = ContextCompat.getDrawable(requireContext(), R.drawable.ic_button_setting)!!.also {
            it.setTint(dynamicTextColorPrimary)
        }

    private var _sBinding: SearchFragmentLayoutBinding? = null
    private val sBinding get() = _sBinding!!


    companion object {
        private const val SEARCH_FILTER_DATA_KEY = "SEARCH_FILTER_DATA_KEY"

        fun newInstance(searchFilterModel: SearchFilterModel?) = SearchFragment().also {
            it.arguments = bundleOf(SEARCH_FILTER_DATA_KEY to searchFilterModel)
        }
    }

    override fun createSource(): Source<BaseModel> {
        return viewModel.createSource()
    }


    override fun onStart() {
        super.onStart()
        registerForEvent()
    }

    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        _sBinding = SearchFragmentLayoutBinding.inflate(inflater, container, false)
        sBinding.browseFragmentContainer.addView(v)
        return sBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpTheme()
        setUpPersistentSearchView()
        setUpListener()
        setUpViews()
        if (savedInstanceState == null) {
            arguments?.getParcelable<SearchFilterModel>(SEARCH_FILTER_DATA_KEY)?.let {
                sBinding.browseFilterNavView.setFilter(it)
            }
        }
    }


    private fun setUpViews() {
        buildTagAdapter(viewModel.tagTagFields)
        buildGenreAdapter(viewModel.genreTagFields)
        invalidateStreamFilter(viewModel.streamTagFields)
    }

    private fun setUpPersistentSearchView() {
        with(sBinding.persistentSearchView) {
            setLeftButtonDrawable(backDrawable)
            setRightButtonDrawable(filterDrawable)
            showRightButton()

            setOnSearchConfirmedListener { _, query ->
                if (query.isEmpty()) {
                    sBinding.persistentSearchView.collapse(false)
                    return@setOnSearchConfirmedListener
                }
                dataProvider.addToSearchHistory(query)
                viewModel.searchQuery = query
                searchNow()
                sBinding.persistentSearchView.collapse(false)
            }

            setOnSuggestionChangeListener(object : OnSuggestionChangeListener {
                override fun onSuggestionRemoved(suggestion: SuggestionItem) {
                    dataProvider.removeFromSearchHistory(suggestion.itemModel.text)
                }

                override fun onSuggestionPicked(suggestion: SuggestionItem) {
                    viewModel.searchQuery = suggestion.itemModel.text
                    dataProvider.addToSearchHistory(viewModel.searchQuery)
                    searchNow()
                }
            })
            dataProvider.getAllHistory().takeIf { it.isNotEmpty() }?.let {
                setSuggestions(
                    SuggestionCreationUtil.asRecentSearchSuggestions(
                        it
                    ),
                    false
                )
            }
        }
    }

    private fun setUpTheme() {
        with(sBinding.persistentSearchView) {
            setCardBackgroundColor(dynamicBackgroundColor)
            ResourcesCompat.getFont(
                requireContext(),
                R.font.rubik_regular
            )!!.let {
                setQueryTextTypeface(it)
                setSuggestionTextTypeface(it)
            }
            dynamicTextColorPrimary.let {
                setSuggestionSelectedTextColor(it)
                setQueryInputHintColor(it)
                setSuggestionTextColor(it)
                setSuggestionIconColor(it)
                setSearchSuggestionIconColor(it)
                setRecentSearchIconColor(it)
                setQueryInputTextColor(it)
            }

            setQueryInputCursorColor(contrastAccentWithBg)

        }
        sBinding.rootDrawerLayout.setBackgroundColor(dynamicBackgroundColor)
        sBinding.advanceSearchCoordinatorLayout.setBackgroundColor(dynamicBackgroundColor)
    }


    private fun setUpListener() {
        with(sBinding.persistentSearchView) {
            setOnLeftBtnClickListener {
                if (isExpanded) {
                    collapse(true)
                } else
                    popBackStack()
            }
            setOnRightBtnClickListener {
                sBinding.rootDrawerLayout.openDrawer(GravityCompat.END)
            }
        }
        with(sBinding.browseFilterNavView) {
            setNavigationCallbackListener(this@SearchFragment)
        }
        sBinding.rootDrawerLayout.addDrawerListener(sBinding.browseFilterNavView.drawerListener)

        dataProvider.onDataChanged {
            sBinding.persistentSearchView.setSuggestions(
                SuggestionCreationUtil.asRecentSearchSuggestions(dataProvider.getAllHistory()),
                false
            )
        }
    }

    @Subscribe
    fun onTagEvent(event: TagEvent) {
        when (event.tagType) {
            MediaTagFilterTypes.TAGS -> invalidateTagFilter(event.tagFields)
            MediaTagFilterTypes.GENRES -> invalidateGenreFilter(event.tagFields)
            MediaTagFilterTypes.STREAMING_ON -> invalidateStreamFilter(event.tagFields)
            else -> {
            }
        }
    }

    private fun invalidateStreamFilter(list: List<TagField>) {
        viewModel.streamTagFields = list.toMutableList()
        sBinding.browseFilterNavView.buildStreamAdapter(
            tagAdapter,
            list
        )
    }

    private fun invalidateGenreFilter(list: List<TagField>) {
        viewModel.genreTagFields = list.toMutableList()
        buildGenreAdapter(list)
    }

    private fun buildGenreAdapter(list: List<TagField>) {
        sBinding.browseFilterNavView.buildGenreAdapter(
            tagAdapter,
            list
        )
    }

    private fun invalidateTagFilter(list: List<TagField>) {
        viewModel.tagTagFields = list.toMutableList()
        buildTagAdapter(list)
    }

    private fun buildTagAdapter(list: List<TagField>) {
        sBinding.browseFilterNavView.buildTagAdapter(
            tagAdapter,
            list
        )
    }


    override fun openTagChooserDialog(tags: List<TagField>, tagType: MediaTagFilterTypes) {
        TagChooserDialogFragment.newInstance(
            TagChooserField(
                tagType,
                tags
            )
        ).show(childFragmentManager, TagChooserDialogFragment::class.java.simpleName)
    }

    override fun onTagAdd(tags: List<TagField>, tagType: MediaTagFilterTypes) {
        when (tagType) {
            MediaTagFilterTypes.TAGS -> {
                viewModel.tagTagFields = tags.toMutableList()
            }
            MediaTagFilterTypes.GENRES -> {
                viewModel.genreTagFields = tags.toMutableList()
            }
            MediaTagFilterTypes.STREAMING_ON -> {
                viewModel.streamTagFields = tags.toMutableList()
            }
            else -> {
            }

        }

    }

    override fun onTagRemoved(tag: String, tagType: MediaTagFilterTypes) {
        when (tagType) {
            MediaTagFilterTypes.TAGS -> {
                viewModel.tagTagFields.removeAll { it.tag == tag }
            }
            MediaTagFilterTypes.GENRES -> {
                viewModel.genreTagFields.removeAll { it.tag == tag }
            }
            MediaTagFilterTypes.STREAMING_ON -> {
                viewModel.streamTagFields.removeAll { it.tag == tag }
            }
            else -> {
            }

        }
    }

    override fun updateTags(tagType: MediaTagFilterTypes) {
        when (tagType) {
            MediaTagFilterTypes.TAGS -> {
                sBinding.browseFilterNavView.invalidateTagAdapter(tagAdapter)
            }
            MediaTagFilterTypes.GENRES -> {
                sBinding.browseFilterNavView.invalidateGenreAdapter(tagAdapter)
            }
            MediaTagFilterTypes.STREAMING_ON -> {
                sBinding.browseFilterNavView.invalidateStreamAdapter(tagAdapter)
            }
            else -> {
            }

        }
    }


    /**
     * Called by advance search filter nav view to fill search box
     * */
    override fun getQuery(): String {
        return viewModel.searchQuery
    }


    override fun applyFilter() {
        if (sBinding.rootDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            sBinding.rootDrawerLayout.closeDrawer(GravityCompat.END)
        }
        sBinding.browseFilterNavView.getFilter().let {
            viewModel.searchQuery = it.query!!
            sBinding.persistentSearchView.inputQuery = viewModel.searchQuery
            searchNow()
        }
    }

    private fun searchNow() {
        sBinding.browseFilterNavView.getFilter().let {
            it.query = viewModel.searchQuery
            viewModel.field = it.toField()
            layoutManager = filterLayoutManager(it)
            baseRecyclerView.layoutManager = layoutManager

            createSource()
            invalidateAdapter()
            visibleToUser = true
        }
    }

    override fun onBackPressed(): Boolean {
        return when {
            sBinding.rootDrawerLayout.isDrawerOpen(GravityCompat.END) -> {
                sBinding.rootDrawerLayout.closeDrawer(GravityCompat.END)
                true
            }
            sBinding.persistentSearchView.isExpanded -> {
                sBinding.persistentSearchView.collapse()
                true
            }
            else -> {
                false
            }
        }
    }

    private fun filterLayoutManager(searchFilterModel: SearchFilterModel): RecyclerView.LayoutManager {
        return when (searchFilterModel) {
            is StudioSearchFilterModel -> {
                if (layoutManager is LinearLayoutManager) {
                    layoutManager
                } else {
                    LinearLayoutManager(requireContext())
                }
            }
            else -> {
                if (layoutManager is GridLayoutManager) {
                    layoutManager
                } else {
                    val span =
                        if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 6 else 3
                    return GridLayoutManager(
                        this.context,
                        span
                    ).also {
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
    }
}