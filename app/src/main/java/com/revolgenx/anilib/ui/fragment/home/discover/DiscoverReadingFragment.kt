package com.revolgenx.anilib.ui.fragment.home.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.setMargins
import androidx.recyclerview.widget.LinearLayoutManager
import com.pranavpandey.android.dynamic.support.widget.DynamicRecyclerView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.ui.dialog.DiscoverMediaListFilterDialog
import com.revolgenx.anilib.infrastructure.event.BrowseMediaListEvent
import com.revolgenx.anilib.data.meta.MediaListMeta
import com.revolgenx.anilib.data.model.home.HomeOrderType
import com.revolgenx.anilib.data.model.home.OrderedViewModel
import com.revolgenx.anilib.common.preference.getHomeOrderFromType
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.ui.presenter.list.MediaListPresenter
import com.revolgenx.anilib.infrastructure.source.media_list.MediaListSource
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.ui.viewmodel.home.discover.DiscoverReadingViewModel
import com.revolgenx.anilib.util.dp
import org.koin.androidx.viewmodel.ext.android.viewModel

open class DiscoverReadingFragment : DiscoverWatchingFragment() {
    private var dRecyclerView: DynamicRecyclerView? = null

    private val presenter
        get() = MediaListPresenter(
            requireContext(),
            MediaListMeta(requireContext().userId()),
            viewModel
        )

    private val source: MediaListSource
        get() = viewModel.source ?: viewModel.createSource()

    private val viewModel by viewModel<DiscoverReadingViewModel>()

    private val order: Int
        get() = getHomeOrderFromType(requireContext(), HomeOrderType.READING)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (requireContext().loggedIn()) {
            dRecyclerView = DynamicRecyclerView(requireContext()).also {
                it.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).also {
                    it.setMargins(dp(10f))
                }
                it.isNestedScrollingEnabled = false
            }

            orderedViewList.add(OrderedViewModel(
                dRecyclerView!!, order,
                getString(R.string.reading),
                R.drawable.ic_manga
                , showSetting = true
            ) {
                handleClick(it)
            })
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (requireContext().loggedIn()) {
            dRecyclerView!!.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun reloadAll() {
        super.reloadAll()

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if(requireContext().loggedIn()) {
            if (savedInstanceState == null) {
                viewModel.field.also { field ->
                    field.userId = requireContext().userId()
                    field.status = MediaListStatus.CURRENT.ordinal
                    field.type = MediaType.MANGA.ordinal
                }
            }
            super.onActivityCreated(savedInstanceState)

            if (savedInstanceState != null) {
                childFragmentManager.findFragmentByTag(MEDIA_LIST_READING_TAG)?.let {
                    (it as DiscoverMediaListFilterDialog).onDoneListener = {
                        renewAdapter()
                    }
                }
            }
            invalidateAdapter()
        }else{
            super.onActivityCreated(savedInstanceState)
        }
    }


    private fun handleClick(which: Int) {
        when (which) {
            0 -> {
                BrowseMediaListEvent(
                    MediaListMeta(
                        requireContext().userId(),
                        null,
                        MediaType.MANGA.ordinal
                    )
                ).postEvent
            }
            1 -> {
                showMediaListFilterDialog(
                    MediaType.MANGA.ordinal,
                    MEDIA_LIST_READING_TAG
                ) {
                    renewAdapter()
                }
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
        if (dRecyclerView == null) return
        dRecyclerView!!.createAdapter(source, presenter)
    }
}