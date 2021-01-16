package com.revolgenx.anilib.ui.fragment.home.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.revolgenx.anilib.R
import com.revolgenx.anilib.infrastructure.event.BrowseTrendingEvent
import com.revolgenx.anilib.data.field.home.PopularMediaField
import com.revolgenx.anilib.data.model.home.HomeOrderType
import com.revolgenx.anilib.data.model.home.OrderedViewModel
import com.revolgenx.anilib.data.model.search.filter.MediaSearchFilterModel
import com.revolgenx.anilib.common.preference.getHomeOrderFromType
import com.revolgenx.anilib.ui.presenter.home.MediaPresenter
import com.revolgenx.anilib.infrastructure.source.discover.DiscoverMediaSource
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.ui.bottomsheet.discover.MediaFilterBottomSheetFragment
import com.revolgenx.anilib.ui.view.showcase.DiscoverMediaShowcaseLayout
import com.revolgenx.anilib.ui.viewmodel.home.discover.DiscoverPopularViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

open class DiscoverPopularFragment : DiscoverTrendingFragment() {

    private var popularShowCaseLayout: DiscoverMediaShowcaseLayout? = null

    private val presenter
        get() = MediaPresenter(requireContext()) { mod ->
            popularShowCaseLayout?.bindShowCaseMedia(mod)
        }

    private val source: DiscoverMediaSource
        get() = viewModel.source ?: viewModel.createSource()

    private val viewModel by viewModel<DiscoverPopularViewModel>()

    private val order: Int
        get() = getHomeOrderFromType(requireContext(), HomeOrderType.POPULAR)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        popularShowCaseLayout = DiscoverMediaShowcaseLayout(requireContext()).also { vi ->
            vi.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        orderedViewList.add(OrderedViewModel(
            popularShowCaseLayout!!, order,
            getString(R.string.popular),R.drawable.ic_popular, showSetting = true
        ) {
            handleClick(it)
        })
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        popularShowCaseLayout!!.showcaseRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (savedInstanceState == null)
            viewModel.field = PopularMediaField.create(requireContext()).also {
                it.sort = MediaSort.POPULARITY_DESC.ordinal
            }
        super.onActivityCreated(savedInstanceState)
        invalidateAdapter()
    }


    private fun handleClick(which: Int) {
        if (which == 0) {
            BrowseTrendingEvent(MediaSearchFilterModel().also {
                it.sort = MediaSort.POPULARITY_DESC.ordinal
            }).postEvent
        } else if (which == 1) {
            showMediaFilterDialog(
                MediaFilterBottomSheetFragment.MediaFilterType.POPULAR.ordinal
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
        popularShowCaseLayout?.showcaseRecyclerView?.createAdapter(source, presenter, true)
    }
}