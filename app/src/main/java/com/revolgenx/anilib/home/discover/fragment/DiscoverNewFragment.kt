package com.revolgenx.anilib.home.discover.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.revolgenx.anilib.R
import com.revolgenx.anilib.home.discover.data.field.NewlyAddedMediaField
import com.revolgenx.anilib.app.setting.data.meta.DiscoverOrderType
import com.revolgenx.anilib.home.discover.data.meta.DiscoverOrderItem
import com.revolgenx.anilib.common.preference.getDiscoverOrderFromType
import com.revolgenx.anilib.common.preference.isDiscoverOrderEnabled
import com.revolgenx.anilib.common.event.OpenSearchEvent
import com.revolgenx.anilib.home.discover.presenter.MediaPresenter
import com.revolgenx.anilib.infrastructure.source.discover.DiscoverMediaSource
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.home.discover.bottomsheet.MediaFilterBottomSheetFragment
import com.revolgenx.anilib.ui.view.showcase.DiscoverMediaShowcaseLayout
import com.revolgenx.anilib.home.discover.viewmodel.DiscoverNewViewModel
import com.revolgenx.anilib.home.discover.viewmodel.ShowCaseViewModel
import com.revolgenx.anilib.search.data.model.filter.SearchFilterModel
import org.koin.androidx.viewmodel.ext.android.viewModel

open class DiscoverNewFragment : DiscoverPopularFragment() {

    private var discoverNewShowCaseLayout: DiscoverMediaShowcaseLayout? = null

    private val viewModel by viewModel<DiscoverNewViewModel>()
    private val showCaseViewModel by viewModel<ShowCaseViewModel>()

    private val presenter
        get() = MediaPresenter(requireContext()) { mod ->
            discoverNewShowCaseLayout?.bindShowCaseMedia(mod, viewLifecycleOwner, showCaseViewModel)
        }

    private val source: DiscoverMediaSource
        get() = viewModel.source ?: viewModel.createSource()

    private val order: Int
        get() = getDiscoverOrderFromType(requireContext(), DiscoverOrderType.NEWLY_ADDED)


    private val isSectionEnabled: Boolean
        get() = isDiscoverOrderEnabled(requireContext(), DiscoverOrderType.NEWLY_ADDED)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (isSectionEnabled) {
            discoverNewShowCaseLayout = DiscoverMediaShowcaseLayout(requireContext()).also { vi ->
                vi.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            orderedViewList.add(DiscoverOrderItem(
                discoverNewShowCaseLayout!!, order,
                getString(R.string.newly_added), R.drawable.ic_new, showSetting = true
            ) {
                handleClick(it)
            })
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isSectionEnabled) {
            discoverNewShowCaseLayout!!.showcaseRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            if (savedInstanceState == null)
                viewModel.field = NewlyAddedMediaField.create(requireContext()).also {
                    it.sort = MediaSort.ID_DESC.ordinal
                }

            invalidateAdapter()
        }
    }


    private fun renewAdapter() {
        viewModel.updateField(requireContext())
        viewModel.createSource()
        invalidateAdapter()
    }

    /** call this method to load into recyclerview*/
    private fun invalidateAdapter() {
        discoverNewShowCaseLayout?.showcaseRecyclerView?.createAdapter(source, presenter, true)
    }

    private fun handleClick(which: Int) {
        if (which == 0) {
            OpenSearchEvent(SearchFilterModel(sort = MediaSort.ID_DESC.ordinal)).postEvent
        } else if (which == 1) {
            showMediaFilterDialog(
                MediaFilterBottomSheetFragment.MediaFilterType.NEWLY_ADDED.ordinal
            ) {
                renewAdapter()
            }
        }
    }

}