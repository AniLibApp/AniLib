package com.revolgenx.anilib.ui.fragment.home.discover

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.data.model.home.HomeOrderType
import com.revolgenx.anilib.data.model.home.OrderedViewModel
import com.revolgenx.anilib.databinding.DiscoverAiringFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenAiringScheduleEvent
import com.revolgenx.anilib.ui.presenter.home.discover.DiscoverAiringPresenter
import com.revolgenx.anilib.infrastructure.source.home.airing.AiringSource
import com.revolgenx.anilib.ui.dialog.DiscoverAiringFilterDialog
import com.revolgenx.anilib.ui.viewmodel.home.discover.DiscoverAiringViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


open class DiscoverAiringFragment : BaseDiscoverFragment() {

    private val presenter
        get() = DiscoverAiringPresenter(requireContext())

    private val source: AiringSource
        get() = viewModel.source ?: viewModel.createSource()

    private var discoverAiringRecyclerView: RecyclerView? = null
    private val viewModel by viewModel<DiscoverAiringViewModel>()


    private val airingBinding: DiscoverAiringFragmentLayoutBinding get() = _airingBinding!!
    private var _airingBinding: DiscoverAiringFragmentLayoutBinding? = null

    private val order: Int
        get() = getHomeOrderFromType(requireContext(), HomeOrderType.AIRING)

    private val isSectionEnabled: Boolean
        get() = isHomeOrderEnabled(requireContext(), HomeOrderType.AIRING)

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
        if (isSectionEnabled) {

            _airingBinding = DiscoverAiringFragmentLayoutBinding.inflate(inflater, container, false)
            discoverAiringRecyclerView = airingBinding.discoverAiringRecyclerView

            orderedViewList.add(OrderedViewModel(
                airingBinding.root, order,
                getString(R.string.airing_schedules), R.drawable.ic_airing, showSetting = true
            ) {
                this@DiscoverAiringFragment.handleClick(it)
            })
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isSectionEnabled) {

            discoverAiringRecyclerView!!.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isSectionEnabled) {

            airingBinding.dslWeekTabLayout.addOnTabSelectedListener(onDaySelectListener)
            airingBinding.dslWeekTabLayout.getTabAt(viewModel.selectedDay)?.select()


            if (requireContext().loggedIn()) {
                with(viewModel.field) {
                    userId = requireContext().userId()
                }
            }

            if (savedInstanceState == null) {
                viewModel.updateField(requireContext())
                viewModel.createSource()
            }

            invalidateAdapter()

        }
    }


    override fun reloadAll() {
        viewModel.createSource()
        invalidateAdapter()
    }

    private fun handleClick(which: Int) {
        when (which) {
            0 -> {
                OpenAiringScheduleEvent().postEvent
            }
            1 -> {
                val airingDialog = DiscoverAiringFilterDialog()
                airingDialog.onButtonClickedListener = { _, w ->
                    when (w) {
                        AlertDialog.BUTTON_POSITIVE -> {
                            renewAdapter()
                        }
                    }
                }
                airingDialog.show(childFragmentManager, "Airing_Filter_Dialog")
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
        if (discoverAiringRecyclerView == null) return
        viewModel.adapter =
            discoverAiringRecyclerView!!.createAdapter(source, presenter, true)
    }

    override fun onDestroyView() {
        _airingBinding?.dslWeekTabLayout?.removeOnTabSelectedListener(onDaySelectListener)
        _airingBinding = null
        super.onDestroyView()
    }
}