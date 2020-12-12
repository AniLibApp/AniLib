package com.revolgenx.anilib.ui.fragment.home.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.pranavpandey.android.dynamic.locale.DynamicLocaleUtils
import com.pranavpandey.android.dynamic.support.widget.DynamicRecyclerView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.ContainerActivity
import com.revolgenx.anilib.ui.fragment.airing.AiringFragment
import com.revolgenx.anilib.common.ui.fragment.ParcelableFragment
import com.revolgenx.anilib.data.model.home.HomeOrderType
import com.revolgenx.anilib.data.model.home.OrderedViewModel
import com.revolgenx.anilib.common.preference.getHomeOrderFromType
import com.revolgenx.anilib.databinding.DiscoverAiringFragmentLayoutBinding
import com.revolgenx.anilib.ui.presenter.home.discover.DiscoverAiringPresenter
import com.revolgenx.anilib.infrastructure.source.home.airing.AiringSource
import org.koin.androidx.viewmodel.ext.android.viewModel


open class DiscoverAiringFragment : BaseDiscoverFragment() {

    private val presenter
        get() = DiscoverAiringPresenter(requireContext())

    private val source: AiringSource
        get() = viewModel.source ?: viewModel.createSource()

    private var discoverAiringRecyclerView: DynamicRecyclerView? = null
    private val viewModel by viewModel<DiscoverAiringViewModel>()


    private val airingBinding: DiscoverAiringFragmentLayoutBinding get() = _airingBinding!!
    private var _airingBinding: DiscoverAiringFragmentLayoutBinding? = null

    private val order: Int
        get() = getHomeOrderFromType(requireContext(), HomeOrderType.AIRING)


    private val onDaySelectListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            if (tab == null || tab.position == viewModel.selectedDay) {
                return
            }
            viewModel.onNewDaySelected(tab.position)
            viewModel.createSource()
            invalidateAdapter()
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {}

        override fun onTabReselected(tab: TabLayout.Tab?) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _airingBinding = DiscoverAiringFragmentLayoutBinding.inflate(inflater, container, false)
        discoverAiringRecyclerView = airingBinding.discoverAiringRecyclerView

        orderedViewList.add(OrderedViewModel(
            airingBinding.root, order,
            getString(R.string.airing_schedules),R.drawable.ic_airing, showSetting = false
        ) {
            this@DiscoverAiringFragment.handleClick(it)
        })

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        discoverAiringRecyclerView!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        airingBinding.dslWeekTabLayout.addOnTabSelectedListener(onDaySelectListener)
        airingBinding.dslWeekTabLayout.getTabAt(viewModel.selectedDay)?.select()

        if(savedInstanceState == null){
            viewModel.createSource()
        }
        invalidateAdapter()
    }


    override fun reloadAll() {
        viewModel.createSource()
        invalidateAdapter()
    }

    private fun handleClick(which: Int) {
        if (which == 0) {
            ContainerActivity.openActivity(
                requireContext(),
                ParcelableFragment(
                    AiringFragment::class.java,
                    bundleOf()
                )
            )
        }
    }

    /** call this method to load into recyclerview*/
    private fun invalidateAdapter() {
        if (discoverAiringRecyclerView == null) return
        viewModel.adapter =
            discoverAiringRecyclerView!!.createAdapter(source, presenter, true)
    }

    override fun onDestroyView() {
        airingBinding.dslWeekTabLayout.removeOnTabSelectedListener(onDaySelectListener)
        _airingBinding = null
        super.onDestroyView()
    }
}