package com.revolgenx.anilib.ui.fragment.home.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.revolgenx.anilib.R
import com.revolgenx.anilib.infrastructure.event.BrowseTrendingEvent
import com.revolgenx.anilib.data.field.home.NewlyAddedMediaField
import com.revolgenx.anilib.data.model.home.HomeOrderType
import com.revolgenx.anilib.data.model.home.OrderedViewModel
import com.revolgenx.anilib.data.model.search.filter.MediaSearchFilterModel
import com.revolgenx.anilib.common.preference.getHomeOrderFromType
import com.revolgenx.anilib.common.preference.isHomeOrderEnabled
import com.revolgenx.anilib.ui.presenter.home.MediaPresenter
import com.revolgenx.anilib.infrastructure.source.discover.DiscoverMediaSource
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.ui.bottomsheet.discover.MediaFilterBottomSheetFragment
import com.revolgenx.anilib.ui.view.showcase.DiscoverMediaShowcaseLayout
import com.revolgenx.anilib.ui.viewmodel.home.discover.DiscoverNewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

open class DiscoverNewFragment : DiscoverPopularFragment() {

    private var discoverNewShowCaseLayout: DiscoverMediaShowcaseLayout? = null

    private val viewModel by viewModel<DiscoverNewViewModel>()

    private val presenter
        get() = MediaPresenter(requireContext()) { mod ->
            discoverNewShowCaseLayout?.bindShowCaseMedia(mod)
        }

    private val source: DiscoverMediaSource
        get() = viewModel.source ?: viewModel.createSource()

    private val order: Int
        get() = getHomeOrderFromType(requireContext(), HomeOrderType.NEWLY_ADDED)


    private val isSectionEnabled: Boolean
        get() = isHomeOrderEnabled(requireContext(), HomeOrderType.NEWLY_ADDED)

    private var sectionVisibleToUser = false

    override fun onResume() {
        super.onResume()
        if (isSectionEnabled) {
            if (!sectionVisibleToUser) {
                invalidateAdapter()
            }
            sectionVisibleToUser = true
        }
    }

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

            orderedViewList.add(OrderedViewModel(
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
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (isSectionEnabled) {
            if (savedInstanceState == null)
                viewModel.field = NewlyAddedMediaField.create(requireContext()).also {
                    it.sort = MediaSort.ID_DESC.ordinal
                }
        }
        super.onActivityCreated(savedInstanceState)
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
            BrowseTrendingEvent(MediaSearchFilterModel().also {
                it.sort = MediaSort.ID_DESC.ordinal
            }).postEvent
        } else if (which == 1) {
            showMediaFilterDialog(
                MediaFilterBottomSheetFragment.MediaFilterType.NEWLY_ADDED.ordinal
            ) {
                renewAdapter()
            }
        }
    }

}