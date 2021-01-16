package com.revolgenx.anilib.ui.fragment.home.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.revolgenx.anilib.R
import com.revolgenx.anilib.infrastructure.event.BrowseTrendingEvent
import com.revolgenx.anilib.data.field.home.TrendingMediaField
import com.revolgenx.anilib.data.model.home.HomeOrderType
import com.revolgenx.anilib.data.model.home.OrderedViewModel
import com.revolgenx.anilib.data.model.search.filter.MediaSearchFilterModel
import com.revolgenx.anilib.common.preference.getHomeOrderFromType
import com.revolgenx.anilib.ui.presenter.home.MediaPresenter
import com.revolgenx.anilib.infrastructure.source.discover.DiscoverMediaSource
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.ui.bottomsheet.discover.MediaFilterBottomSheetFragment
import com.revolgenx.anilib.ui.view.showcase.DiscoverMediaShowcaseLayout
import com.revolgenx.anilib.ui.viewmodel.home.discover.DiscoverTrendingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

open class DiscoverTrendingFragment : DiscoverReadingFragment() {

    private var trendingShowCaseLayout: DiscoverMediaShowcaseLayout? = null

    private val presenter
        get() = MediaPresenter(requireContext()) { mod ->
            trendingShowCaseLayout?.bindShowCaseMedia(mod)
        }

    private val source: DiscoverMediaSource
        get() = viewModel.source ?: viewModel.createSource()


    private val viewModel by viewModel<DiscoverTrendingViewModel>()

    private val order: Int
        get() = getHomeOrderFromType(requireContext(), HomeOrderType.TRENDING)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        trendingShowCaseLayout= DiscoverMediaShowcaseLayout(requireContext()).also {vi->
            vi.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        orderedViewList.add(OrderedViewModel(
            trendingShowCaseLayout!!, order,
            getString(R.string.trending), icon = R.drawable.ic_fire,showSetting = true
        ) {
            handleClick(it)
        })

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trendingShowCaseLayout!!.showcaseRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            viewModel.field = TrendingMediaField.create(requireContext()).also {
                it.sort = MediaSort.TRENDING_DESC.ordinal
            }
        }

        invalidateAdapter()
    }


    private fun handleClick(which: Int) {
        if (which == 0) {
            BrowseTrendingEvent(MediaSearchFilterModel().also {
                it.sort = MediaSort.TRENDING_DESC.ordinal
            }).postEvent
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