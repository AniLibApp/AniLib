package com.revolgenx.anilib.list.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.otaliastudios.elements.pagers.NoPagesPager
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.constant.MediaListDisplayMode
import com.revolgenx.anilib.databinding.MediaListCollectionFragmentBinding
import com.revolgenx.anilib.list.presenter.MediaListCollectionPresenter
import com.revolgenx.anilib.list.bottomsheet.MediaListCollectionFilterBottomSheet
import com.revolgenx.anilib.list.bottomsheet.MediaListDisplaySelectorBottomSheet
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.list.viewmodel.MediaListCollectionVM
import com.revolgenx.anilib.list.viewmodel.MediaListContainerSharedVM
import com.revolgenx.anilib.list.bottomsheet.MediaListGroupSelectorBottomSheet
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.list.viewmodel.MediaListCollectionContainerCallback
import com.revolgenx.anilib.list.viewmodel.MediaListCollectionStoreVM
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

abstract class BaseMediaListCollectionFragment() :
    BaseLayoutFragment<MediaListCollectionFragmentBinding>() {
    abstract val mediaType: MediaType

    protected abstract val listCollectionStoreVM: MediaListCollectionStoreVM
    protected val viewModel by viewModel<MediaListCollectionVM> { parametersOf(listCollectionStoreVM) }

    private val containerSharedVM by viewModel<MediaListContainerSharedVM>(owner = {
        ViewModelOwner.from(
            this.parentFragment ?: this,
            this.parentFragment
        )
    })


    protected val isLoggedInUser by lazy { viewModel.field.userId == UserPreference.userId }

    private var adapter: Adapter? = null

    private val basePresenter: MediaListCollectionPresenter
        get() = MediaListCollectionPresenter(
            requireContext(),
            isLoggedInUser,
            mediaType,
            viewModel
        )

    private val errorPresenter: Presenter<Unit> by lazy {
        Presenter.forErrorIndicator(requireContext(), R.layout.error_layout)
    }

    private val emptyPresenter: Presenter<Unit> by lazy {
        Presenter.forEmptyIndicator(requireContext(), R.layout.empty_layout)
    }

    protected open val loadingPresenter: Presenter<Unit>
        get() = Presenter.forLoadingIndicator(requireContext(), R.layout.loading_layout)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): MediaListCollectionFragmentBinding =
        MediaListCollectionFragmentBinding.inflate(inflater, parent, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.type = mediaType
        containerSharedVM.userId?.let {
            viewModel.field.userId = it
        }
        binding.onBind()
    }

    private fun loadLayoutManager() {
        binding.alListRecyclerView.layoutManager = getLayoutManager()
    }

    private fun MediaListCollectionFragmentBinding.onBind() {
        loadLayoutManager()

        viewModel.sourceLiveData.observe(viewLifecycleOwner) {
            alListSwipeToRefresh.isRefreshing = false
            invalidateSource()
        }

        containerSharedVM.mediaListContainerCallback.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            if (it.second == mediaType.ordinal) {
                when (it.first) {
                    MediaListCollectionContainerCallback.SEARCH -> {
                        toggleSearch()
                    }
                    MediaListCollectionContainerCallback.GROUP -> {
                        viewModel.groupNamesWithCount.value
                            ?.toList()
                            ?.map { it to (it.first == viewModel.currentGroupNameHistory) }
                            ?.let {
                                MediaListGroupSelectorBottomSheet.newInstance(
                                    it
                                ) {
                                    if (context == null) return@newInstance

                                    if (it == viewModel.currentGroupNameHistory) return@newInstance

                                    if (isLoggedInUser) {
                                        if (mediaType == MediaType.ANIME) {
                                            animeListStatusHistory(requireContext(), it)
                                        } else {
                                            mangaListStatusHistory(requireContext(), it)
                                        }
                                    } else {
                                        viewModel.groupNameHistory = it
                                    }
                                    updateCurrentGroupWithCount()
                                    viewModel.filter()
                                }.show(requireContext())
                            }
                    }
                    MediaListCollectionContainerCallback.CURRENT_TAB -> {
                        updateCurrentGroupWithCount()
                    }
                    MediaListCollectionContainerCallback.FILTER -> {
                        MediaListCollectionFilterBottomSheet.newInstance(
                            viewModel.mediaListFilter.copy().also {
                                it.type = mediaType.ordinal
                            }) {
                            if (context == null) return@newInstance
                            with(viewModel.mediaListFilter) {
                                formatsIn = it.formatsIn
                                sort = it.sort
                                genre = it.genre
                                status = it.status
                            }
                            viewModel.filter()
                        }.show(requireContext())
                    }
                    MediaListCollectionContainerCallback.DISPLAY -> {
                        MediaListDisplaySelectorBottomSheet.newInstance(
                            if (isLoggedInUser) {
                                getUserMediaListCollectionDisplayMode(mediaType)
                            } else {
                                getGeneralMediaListCollectionDisplayMode(mediaType)
                            }.ordinal
                        ) {
                            if (context == null) return@newInstance

                            if (isLoggedInUser) {
                                setUserMediaListCollectionDisplayMode(mediaType, it)
                            } else {
                                setGeneralMediaListCollectionDisplayMode(mediaType, it)
                            }
                            loadLayoutManager()
                            invalidateSource()
                        }.show(requireContext())
                    }
                }

                containerSharedVM.mediaListContainerCallback.value = null
            }
        }

        viewModel.groupNamesWithCount.observe(viewLifecycleOwner) {
            updateCurrentGroupWithCount()
        }

        alListSwipeToRefresh.setOnRefreshListener {
            viewModel.getMediaList()
        }

        alListSearchEt.doOnTextChanged { text, _, _, _ ->
            viewModel.search = text?.toString() ?: ""
        }

        alListSearchEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId != EditorInfo.IME_ACTION_SEARCH) return@setOnEditorActionListener false
            viewModel.search = alListSearchEt.text?.toString() ?: ""
            true
        }

        alListClearSearchIv.setOnClickListener {
            alListSearchEt.setText("")
        }

        showAlListSearchView()
    }

    private fun invalidateSource() {
        val source = viewModel.sourceLiveData.value ?: return
        invalidateAdapter(basePresenter, source)
    }

    private fun updateCurrentGroupWithCount() {
        containerSharedVM.currentGroupNameWithCount.value = viewModel.groupNamesWithCount.value
            ?.get(viewModel.currentGroupNameHistory)
            ?.let { viewModel.currentGroupNameHistory!! to it }
    }


    private fun showAlListSearchView() {
        binding.alListSearchLayout.visibility =
            if (viewModel.searchViewVisible) View.VISIBLE else View.GONE
    }

    private fun MediaListCollectionFragmentBinding.showError() {
        errorLayout.errorLayout.visibility = View.VISIBLE
        emptyLayout.emptyLayout.visibility = View.GONE
    }

    private fun MediaListCollectionFragmentBinding.showEmpty() {
        emptyLayout.emptyLayout.visibility = View.VISIBLE
        errorLayout.errorLayout.visibility = View.GONE
    }


    private fun getLayoutManager(): GridLayoutManager {
        var span =
            if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
        when (if (isLoggedInUser) getUserMediaListCollectionDisplayMode(mediaType) else getGeneralMediaListCollectionDisplayMode(
            mediaType
        )) {
            MediaListDisplayMode.NORMAL, MediaListDisplayMode.MINIMAL_LIST -> span /= 2
            MediaListDisplayMode.CLASSIC, MediaListDisplayMode.MINIMAL -> span += 1
            else -> {
            }
        }
        return GridLayoutManager(this.context, span).also {
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

    override fun onResume() {
        super.onResume()
        if (!visibleToUser) {
            viewModel.getMediaList()
        }
        visibleToUser = true
    }


    protected fun notifyDataSetChanged() {
        adapter?.notifyDataSetChanged()
    }

    private fun invalidateAdapter(
        presenter: MediaListCollectionPresenter,
        source: Source<MediaListModel>
    ) {
        adapter =
            Adapter.builder(this)
                .setPager(NoPagesPager())
                .addSource(source)
                .addPresenter(presenter)
                .addPresenter(emptyPresenter)
                .addPresenter(errorPresenter)
                .addPresenter(loadingPresenter)
                .into(binding.alListRecyclerView)
    }

    private fun toggleSearch() {
        viewModel.searchViewVisible = !viewModel.searchViewVisible
        showAlListSearchView()
    }

}