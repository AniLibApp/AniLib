package com.revolgenx.anilib.home.discover.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pranavpandey.android.dynamic.support.widget.DynamicViewPager2Layout
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.viewmodel.MainSharedVM
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.home.discover.dialog.DiscoverMediaListFilterDialog
import com.revolgenx.anilib.data.meta.MediaListMeta
import com.revolgenx.anilib.app.setting.data.meta.DiscoverOrderType
import com.revolgenx.anilib.home.discover.data.meta.DiscoverOrderItem
import com.revolgenx.anilib.home.event.ChangeViewPagerPageEvent
import com.revolgenx.anilib.home.event.MainActivityPage
import com.revolgenx.anilib.home.discover.presenter.MediaListPresenter
import com.revolgenx.anilib.infrastructure.source.media_list.MediaListSource
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.home.discover.viewmodel.DiscoverWatchingVM
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

open class DiscoverWatchingFragment : DiscoverAiringFragment() {
    private var watchingRecyclerView: RecyclerView? = null

    private val presenter
        get() = MediaListPresenter(
            requireContext(),
            MediaListMeta(UserPreference.userId),
            viewModel
        )

    private val source: MediaListSource
        get() = viewModel.source ?: viewModel.createSource()

    private val viewModel by viewModel<DiscoverWatchingVM>()
    private val mainSharedVM by sharedViewModel<MainSharedVM>()

    private val order: Int
        get() = getDiscoverOrderFromType(requireContext(), DiscoverOrderType.WATCHING)


    private val isSectionEnabled: Boolean
        get() = isDiscoverOrderEnabled(requireContext(), DiscoverOrderType.WATCHING)

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
                )
                it.isNestedScrollingEnabled = false
            }

            val recyclerViewContainer = DynamicViewPager2Layout(requireContext()).also {
                it.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            recyclerViewContainer.addView(watchingRecyclerView)

            orderedViewList.add(DiscoverOrderItem(
                recyclerViewContainer, order,
                getString(R.string.watching),
                R.drawable.ic_media, showSetting = true
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

            if (savedInstanceState == null) {
                viewModel.field.also { field ->
                    field.userId = UserPreference.userId
                    field.status = MediaListStatus.CURRENT.ordinal
                    field.type = MediaType.ANIME.ordinal
                    field.sort = getDiscoverMediaListSort(requireContext(), field.type!!)
                }
            }

            if (savedInstanceState != null) {
                childFragmentManager.findFragmentByTag(MEDIA_LIST_WATCHING_TAG)?.let {
                    (it as DiscoverMediaListFilterDialog).onDoneListener = {
                        renewAdapter()
                    }
                }
            }


            invalidateAdapter()
        }
    }


    private fun handleClick(which: Int) {
        when (which) {
            0 -> {
                ChangeViewPagerPageEvent(MainActivityPage.LIST).postEvent
                mainSharedVM.mediaListCurrentTab.value = MediaType.ANIME.ordinal
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



    override fun reloadContent() {
        super.reloadContent()
        if (isSectionEnabled) {
            viewModel.createSource()
            invalidateAdapter()
        }
    }

    private fun renewAdapter() {
        if (context == null) return
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