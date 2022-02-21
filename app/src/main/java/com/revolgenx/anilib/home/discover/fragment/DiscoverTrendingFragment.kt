package com.revolgenx.anilib.home.discover.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.revolgenx.anilib.R
import com.revolgenx.anilib.home.discover.data.field.TrendingMediaField
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
import com.revolgenx.anilib.home.discover.viewmodel.DiscoverTrendingViewModel
import com.revolgenx.anilib.home.discover.viewmodel.ShowCaseViewModel
import com.revolgenx.anilib.search.data.model.filter.SearchFilterModel
import org.koin.androidx.viewmodel.ext.android.viewModel

open class DiscoverTrendingFragment : DiscoverReadingFragment() {

    private var trendingShowCaseLayout: DiscoverMediaShowcaseLayout? = null

    private val presenter
        get() = MediaPresenter(requireContext()) { mod ->
            trendingShowCaseLayout?.bindShowCaseMedia(mod, viewLifecycleOwner, showCaseViewModel)
        }

    private val source: DiscoverMediaSource
        get() = viewModel.source ?: viewModel.createSource()


    private val viewModel by viewModel<DiscoverTrendingViewModel>()
    private val showCaseViewModel by viewModel<ShowCaseViewModel>()

    private val order: Int
        get() = getDiscoverOrderFromType(requireContext(), DiscoverOrderType.TRENDING)


    private val isSectionEnabled: Boolean
        get() = isDiscoverOrderEnabled(requireContext(), DiscoverOrderType.TRENDING)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (isSectionEnabled) {
            trendingShowCaseLayout = DiscoverMediaShowcaseLayout(requireContext()).also { vi ->
                vi.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            orderedViewList.add(DiscoverOrderItem(
                trendingShowCaseLayout!!, order,
                getString(R.string.trending), icon = R.drawable.ic_fire, showSetting = true
            ) {
                handleClick(it)
            })
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isSectionEnabled) {
            trendingShowCaseLayout!!.showcaseRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            if (savedInstanceState == null) {
                viewModel.field = TrendingMediaField.create(requireContext()).also {
                    it.sort = MediaSort.TRENDING_DESC.ordinal
                }
            }
            invalidateAdapter()
        }
    }


    private fun handleClick(which: Int) {
        if (which == 0) {
            OpenSearchEvent(SearchFilterModel(sort = MediaSort.TRENDING_DESC.ordinal)).postEvent
        } else if (which == 1) {
            showMediaFilterDialog(
                MediaFilterBottomSheetFragment.MediaFilterType.TRENDING.ordinal
            ) {
                renewAdapter()
            }
        }
    }

    private fun renewAdapter() {
        viewModel.updateField(requireContext())
        viewModel.createSource()
        invalidateAdapter()
    }

    /** call this method to load into recyclerview*/
    private fun invalidateAdapter() {
        trendingShowCaseLayout?.showcaseRecyclerView?.createAdapter(source, presenter, true)
    }
}