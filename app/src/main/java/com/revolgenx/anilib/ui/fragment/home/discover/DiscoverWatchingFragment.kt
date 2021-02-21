package com.revolgenx.anilib.ui.fragment.home.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.setMargins
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pranavpandey.android.dynamic.support.widget.DynamicRecyclerView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.ui.dialog.DiscoverMediaListFilterDialog
import com.revolgenx.anilib.infrastructure.event.BrowseMediaListEvent
import com.revolgenx.anilib.data.meta.MediaListMeta
import com.revolgenx.anilib.data.model.home.HomeOrderType
import com.revolgenx.anilib.data.model.home.OrderedViewModel
import com.revolgenx.anilib.infrastructure.event.ChangeViewPagerPageEvent
import com.revolgenx.anilib.infrastructure.event.ListContainerFragmentPage
import com.revolgenx.anilib.infrastructure.event.MainActivityPage
import com.revolgenx.anilib.ui.presenter.home.discover.MediaListPresenter
import com.revolgenx.anilib.infrastructure.source.media_list.MediaListSource
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.ui.viewmodel.home.discover.DiscoverWatchingViewModel
import com.revolgenx.anilib.util.dp
import org.koin.androidx.viewmodel.ext.android.viewModel

open class DiscoverWatchingFragment : DiscoverAiringFragment() {
    private var watchingRecyclerView: RecyclerView? = null

    private val presenter
        get() = MediaListPresenter(
            requireContext(),
            MediaListMeta(requireContext().userId()),
            viewModel
        )

    private val source: MediaListSource
        get() = viewModel.source ?: viewModel.createSource()

    private val viewModel by viewModel<DiscoverWatchingViewModel>()

    private val order: Int
        get() = getHomeOrderFromType(requireContext(), HomeOrderType.WATCHING)


    private val isSectionEnabled: Boolean
        get() = isHomeOrderEnabled(requireContext(), HomeOrderType.WATCHING)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (requireContext().loggedIn() && isSectionEnabled) {
            watchingRecyclerView = RecyclerView(requireContext()).also {
                it.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).also {
                    it.setMargins(dp(10f))
                }
                it.isNestedScrollingEnabled = false
            }

            orderedViewList.add(OrderedViewModel(
                watchingRecyclerView!!, order,
                getString(R.string.watching),
                R.drawable.ic_media
                , showSetting = true
            ) {
                handleClick(it)
            })
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (requireContext().loggedIn() && isSectionEnabled) {
            watchingRecyclerView!!.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun reloadAll() {
        super.reloadAll()

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (requireContext().loggedIn() && isSectionEnabled) {
            if (savedInstanceState == null) {
                viewModel.field.also { field ->
                    field.userId = requireContext().userId()
                    field.status = MediaListStatus.CURRENT.ordinal
                    field.type = MediaType.ANIME.ordinal
                    field.sort = getDiscoverMediaListSort(requireContext(), field.type!!)
                }
            }

            super.onActivityCreated(savedInstanceState)

            if (savedInstanceState != null) {
                childFragmentManager.findFragmentByTag(MEDIA_LIST_WATCHING_TAG)?.let {
                    (it as DiscoverMediaListFilterDialog).onDoneListener = {
                        renewAdapter()
                    }
                }
            }


            invalidateAdapter()
        }
        else{
            super.onActivityCreated(savedInstanceState)
        }
    }


    private fun handleClick(which: Int) {
        when (which) {
            0 -> {
                ChangeViewPagerPageEvent(MainActivityPage.LIST).postEvent
                ChangeViewPagerPageEvent(ListContainerFragmentPage.ANIME).postEvent
            }
            1 -> {
                showMediaListFilterDialog(
                    MediaType.ANIME.ordinal,
                    MEDIA_LIST_WATCHING_TAG
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
        if (watchingRecyclerView == null) return
        watchingRecyclerView!!.createAdapter(source, presenter)
    }
}