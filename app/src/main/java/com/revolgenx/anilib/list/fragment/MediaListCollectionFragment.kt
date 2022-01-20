package com.revolgenx.anilib.list.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.map
import androidx.recyclerview.widget.GridLayoutManager
import com.revolgenx.anilib.common.preference.getAlListGridPresenter
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.constant.MediaListDisplayMode
import com.revolgenx.anilib.databinding.MediaListCollectionFragmentBinding
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.list.adapter.MediaListCollectionAdapter
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.list.viewmodel.MediaListCollectionVM
import com.revolgenx.anilib.home.list.viewmodel.AlMediaListCollectionSharedVM
import com.revolgenx.anilib.list.bottomsheet.MediaListGroupSelectorBottomSheet
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class MediaListCollectionFragment() : BaseLayoutFragment<MediaListCollectionFragmentBinding>() {
    abstract val mediaType: MediaType
    private val viewModel by viewModel<MediaListCollectionVM>()

    private val alListContainerVM by sharedViewModel<AlMediaListCollectionSharedVM>()
    private val adapter by lazy { MediaListCollectionAdapter(requireContext()) }
    private val recentGroupName = "All"

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): MediaListCollectionFragmentBinding =
        MediaListCollectionFragmentBinding.inflate(inflater, parent, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.type = mediaType
        binding.onBind()
    }

    private fun MediaListCollectionFragmentBinding.onBind() {
        alListRecyclerView.layoutManager = getLayoutManager()
        alListRecyclerView.adapter = adapter
        viewModel.filteredMediaListCollectionLiveData.observe(viewLifecycleOwner, {
            when (it?.status) {
                Status.SUCCESS -> {
                    alListSwipeToRefresh.isRefreshing = false
                    adapter.filteredMediaListGroupModel = it.data ?: return@observe
                    if (it.data.entries.isNullOrEmpty()) {
                        showEmpty()
                    }
                }
                Status.ERROR -> {
                    alListSwipeToRefresh.isRefreshing = false
                    showError()
                }
                Status.LOADING -> {
                    alListSwipeToRefresh.isRefreshing = true
                    emptyLayout.emptyLayout.visibility = View.GONE
                    errorLayout.errorLayout.visibility = View.GONE
                }
            }
        })

        alListContainerVM.toggleSearchView.observe(viewLifecycleOwner) {
            if (it == mediaType.ordinal) {
                toggleSearch()
                alListContainerVM.toggleSearchView.value = null
            }
        }

        alListContainerVM.showListGroupSelector.observe(viewLifecycleOwner) {
            if (it == mediaType.ordinal) {
                viewModel.groupNamesWithCount.value?.toList()?.map { it to (it.first == recentGroupName) }?.let {
                    MediaListGroupSelectorBottomSheet.newInstance(
                        it
                    ).show(requireContext())
                }
                alListContainerVM.showListGroupSelector.value = null
            }
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
        when (getAlListGridPresenter()) {
            MediaListDisplayMode.NORMAL, MediaListDisplayMode.MINIMAL_LIST -> span /= 2
            MediaListDisplayMode.CLASSIC, MediaListDisplayMode.MINIMAL -> span += 1
            else -> {
            }
        }
        return GridLayoutManager(this.context, span)
    }

    override fun onResume() {
        super.onResume()
        if (!visibleToUser) {
            viewModel.getMediaList()
        }
        visibleToUser = true
    }


    fun toggleSearch() {
        viewModel.searchViewVisible = !viewModel.searchViewVisible
        showAlListSearchView()
    }

}