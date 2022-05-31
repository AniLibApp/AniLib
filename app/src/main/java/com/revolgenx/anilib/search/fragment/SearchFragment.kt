package com.revolgenx.anilib.search.fragment

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
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor
import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.search.data.field.SearchTypes
import com.revolgenx.anilib.ui.selector.data.meta.SelectableMeta
import com.revolgenx.anilib.search.data.model.SearchFilterEventModel
import com.revolgenx.anilib.databinding.SearchFragmentLayoutBinding
import com.revolgenx.anilib.ui.selector.dialog.SelectableDialogFragment
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.data.tuples.MutablePair
import com.revolgenx.anilib.search.bottomsheet.SearchFilterBottomSheet
import com.revolgenx.anilib.search.presenter.SearchPresenter
import com.revolgenx.anilib.search.viewmodel.SearchFragmentViewModel
import com.revolgenx.anilib.ui.selector.constant.SelectedState
import com.revolgenx.anilib.ui.view.*
import com.revolgenx.anilib.util.loginContinue
import org.koin.androidx.viewmodel.ext.android.viewModel

// TODO add readable on for manga
class SearchFragment : BasePresenterFragment<BaseModel>() {
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

    private var applyingFilter = false

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
        return sBinding.root
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
        sBinding.initListener()

        searchFilterEventModel?.openFilter?.takeIf { it }?.let {
            openSearchFilterBottomSheet()
        }
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

    private fun SearchFragmentLayoutBinding.initListener() {
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
            search()
        }

        searchTypeTabLayout.doOnTabSelected(viewLifecycleOwner) { _, position ->
            if (applyingFilter) return@doOnTabSelected

            filterModel.searchType = SearchTypes.values()[position]
            field.searchFilterModel.searchType = filterModel.searchType
            filter()
        }
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_filter_iv -> {
                openSearchFilterBottomSheet()
                true
            }
            else -> super.onToolbarMenuSelected(item)
        }
    }

    private fun openSearchFilterBottomSheet() {
        SearchFilterBottomSheet().also {
            it.applyFilterListener = {
                sBinding.applyFilter()
            }
            it.show(this@SearchFragment)
        }
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

    private fun SearchFragmentLayoutBinding.applyFilter(){
        applyingFilter = true
        searchTypeTabLayout.getTabAt(field.searchFilterModel.searchType.ordinal)?.select()
        searchEt.setText(field.search)
        filter()
    }

}