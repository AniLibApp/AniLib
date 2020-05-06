package com.revolgenx.anilib.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.constant.BrowseTypes
import com.revolgenx.anilib.event.BrowseFilterAppliedEvent
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.BaseModel
import com.revolgenx.anilib.model.search.filter.*
import com.revolgenx.anilib.presenter.search.BrowsePresenter
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import com.revolgenx.anilib.viewmodel.BrowseFragmentViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel

class BrowseFragment : BasePresenterFragment<BaseModel>() {

    private val viewModel by viewModel<BrowseFragmentViewModel>()

    override val basePresenter: Presenter<BaseModel>
        get() = BrowsePresenter(requireContext(), viewLifecycleOwner)

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
                    return if (adapter?.elementAt(position)?.element?.type?.let {
                            it == BrowseTypes.MANGA.ordinal
                                    || it == BrowseTypes.ANIME.ordinal
                                    || it == BrowseTypes.CHARACTER.ordinal
                                    || it == BrowseTypes.STAFF.ordinal
                        } == true) {
                        1
                    } else {
                        span
                    }
                }
            }
        }
    }

    private fun filterLayoutManager(browseFilterModel: BrowseFilterModel): RecyclerView.LayoutManager {
        return when (browseFilterModel) {
            is StudioBrowseFilterModel -> {
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
                                return if (adapter?.elementAt(position)?.element?.type == 0) {
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