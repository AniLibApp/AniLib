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
import com.revolgenx.anilib.home.discover.dialog.DiscoverMediaListFilterDialog
import com.revolgenx.anilib.data.meta.MediaListMeta
import com.revolgenx.anilib.app.setting.data.meta.DiscoverOrderType
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.home.discover.data.meta.DiscoverOrderItem
import com.revolgenx.anilib.home.discover.presenter.MediaListPresenter
import com.revolgenx.anilib.infrastructure.source.media_list.MediaListSource
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.home.discover.viewmodel.DiscoverReadingVM
import com.revolgenx.anilib.home.event.ChangeViewPagerPageEvent
import com.revolgenx.anilib.home.event.MainActivityPage
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

open class DiscoverReadingFragment : DiscoverWatchingFragment() {
    private var dRecyclerView: RecyclerView? = null

    private val presenter
        get() = MediaListPresenter(
            requireContext(),
            MediaListMeta(UserPreference.userId),
            viewModel
        )

    private val source: MediaListSource
        get() = viewModel.source ?: viewModel.createSource()

    private val viewModel by viewModel<DiscoverReadingVM>()
    private val mainSharedVM by sharedViewModel<MainSharedVM>()

    private val order: Int
        get() = getDiscoverOrderFromType(requireContext(), DiscoverOrderType.READING)

    private val isSectionEnabled: Boolean
        get() = isDiscoverOrderEnabled(requireContext(), DiscoverOrderType.READING)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (requireContext().loggedIn() && isSectionEnabled) {
            dRecyclerView = RecyclerView(requireContext()).also {
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
            recyclerViewContainer.addView(dRecyclerView)

            orderedViewList.add(DiscoverOrderItem(
                recyclerViewContainer, order,
                getString(R.string.reading),
                R.drawable.ic_manga, showSetting = true
            ) {
                handleClick(it)
            })
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireContext().loggedIn() && isSectionEnabled) {
            dRecyclerView!!.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            viewModel.field.also { field ->
                field.userId = UserPreference.userId
                field.status = MediaListStatus.CURRENT.ordinal
                field.type = MediaType.MANGA.ordinal
                field.sort = getDiscoverMediaListSort(requireContext(), field.type!!)
            }

            if (savedInstanceState != null) {
                childFragmentManager.findFragmentByTag(MEDIA_LIST_READING_TAG)?.let {
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
                mainSharedVM.mediaListCurrentTab.value = MediaType.MANGA.ordinal
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


    override fun reloadContent() {
        super.reloadContent()
        if (isSectionEnabled) {
            viewModel.createSource()
            invalidateAdapter()
        }
    }

    private fun renewAdapter() {
        context ?: return
        viewModel.updateField(requireContext())
        viewModel.createSource()
        invalidateAdapter()
    }


    /** call this method to load into recyclerview*/
    private fun invalidateAdapter() {
        dRecyclerView ?: return
        dRecyclerView!!.createAdapter(source, presenter)
    }
}