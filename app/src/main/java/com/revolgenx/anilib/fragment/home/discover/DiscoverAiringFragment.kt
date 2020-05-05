package com.revolgenx.anilib.fragment.home.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.pagers.PageSizePager
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.ContainerActivity
import com.revolgenx.anilib.fragment.airing.AiringFragment
import com.revolgenx.anilib.fragment.base.ParcelableFragment
import com.revolgenx.anilib.presenter.home.discover.DiscoverAiringPresenter
import com.revolgenx.anilib.source.home.airing.AiringSource
import com.revolgenx.anilib.type.AiringSort
import kotlinx.android.synthetic.main.discover_airing_fragment_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel


open class DiscoverAiringFragment : BaseDiscoverFragment() {

    private val presenter
        get() = DiscoverAiringPresenter(requireContext())

    private val source: AiringSource
        get() = viewModel.source ?: viewModel.createSource(viewModel.field)

    private val viewModel by viewModel<DiscoverAiringViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        addView(
            inflater.inflate(R.layout.discover_airing_fragment_layout, container, false),
            getString(R.string.airing_schedules)
        ) {
            handleGarlandClick(it)
        }
        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.field.sort = AiringSort.TIME.ordinal
        discoverAiringRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        invalidateAdapter()

    }


    override fun reloadAll() {
        invalidateAdapter()
    }

    private fun handleGarlandClick(it: Int) {
        if (it == 0) {
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
        viewModel.adapter = Adapter.builder(this, 10)
            .setPager(PageSizePager(10))
            .addSource(source)
            .addPresenter(presenter)
            .addPresenter(loadingPresenter)
            .addPresenter(errorPresenter)
            .addPresenter(emptyPresenter)
            .into(discoverAiringRecyclerView)
    }
}