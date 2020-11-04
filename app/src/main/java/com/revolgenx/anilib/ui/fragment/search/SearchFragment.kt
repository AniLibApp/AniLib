package com.revolgenx.anilib.ui.fragment.search

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.constant.SearchTypes
import com.revolgenx.anilib.infrastructure.event.BrowseFilterAppliedEvent
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.data.model.BaseModel
import com.revolgenx.anilib.data.model.search.filter.SearchFilterModel
import com.revolgenx.anilib.data.model.search.filter.StudioSearchFilterModel
import com.revolgenx.anilib.ui.presenter.search.SearchPresenter
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import com.revolgenx.anilib.ui.viewmodel.search.SearchFragmentViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BasePresenterFragment<BaseModel>() {

    private val viewModel by viewModel<SearchFragmentViewModel>()

    override val basePresenter: Presenter<BaseModel>
        get() = SearchPresenter(requireContext(), viewLifecycleOwner)

    override val baseSource: Source<BaseModel>
        get() = viewModel.source ?: createSource()

    override fun createSource(): Source<BaseModel> {
        return viewModel.createSource()
    }


    override fun onStart() {
        super.onStart()
        registerForEvent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val span =
            if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 6 else 3
        layoutManager = GridLayoutManager(
            this.context,
            span
        ).also {
            it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapter?.getItemViewType(position)?.let {
                            it == SearchTypes.MANGA.ordinal
                                    || it == SearchTypes.ANIME.ordinal
                                    || it == SearchTypes.CHARACTER.ordinal
                                    || it == SearchTypes.STAFF.ordinal
                                    || it == SearchTypes.USER.ordinal
                        } == true) {
                        1
                    } else {
                        span
                    }
                }
            }
        }
    }

    private fun filterLayoutManager(searchFilterModel: SearchFilterModel): RecyclerView.LayoutManager {
        return when (searchFilterModel) {
            is StudioSearchFilterModel -> {
                if (layoutManager is LinearLayoutManager) {
                    layoutManager
                } else {
                    LinearLayoutManager(requireContext())
                }
            }
            else -> {
                if (layoutManager is GridLayoutManager) {
                    layoutManager
                } else {
                    val span =
                        if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 6 else 3
                    return GridLayoutManager(
                        this.context,
                        span
                    ).also {
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
            }
        }
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun search(event: BrowseFilterAppliedEvent) {
        EventBus.getDefault().removeStickyEvent(event)
        viewModel.field = event.filterModel.toField()

        layoutManager = filterLayoutManager(event.filterModel)
        baseRecyclerView.layoutManager = layoutManager

        createSource()
        invalidateAdapter()
        visibleToUser = true
    }

    override fun onStop() {
        unRegisterForEvent()
        super.onStop()
    }

}