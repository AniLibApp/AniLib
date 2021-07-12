package com.revolgenx.anilib.ui.fragment.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.getMediaListGridPresenter
import com.revolgenx.anilib.common.preference.setMediaListGridPresenter
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.data.meta.MediaListMeta
import com.revolgenx.anilib.databinding.UserMediaListContainerFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.event.DisplayModeChangedEvent
import com.revolgenx.anilib.infrastructure.event.DisplayTypes
import com.revolgenx.anilib.infrastructure.event.MediaListCollectionFilterEvent
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.ui.bottomsheet.list.MediaListFilterBottomSheetFragment
import com.revolgenx.anilib.ui.view.makeArrayPopupMenu
import com.revolgenx.anilib.ui.viewmodel.list.UserMediaListContainerViewModel
import com.revolgenx.anilib.util.EventBusListener
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserMediaListContainerFragment :
    BaseLayoutFragment<UserMediaListContainerFragmentLayoutBinding>(), EventBusListener {

    companion object {
        private const val MEDIA_LIST_META_KEY = "MEDIA_LIST_META_KEY"
        fun newInstance(meta: MediaListMeta) = UserMediaListContainerFragment().also {
            it.arguments = bundleOf(MEDIA_LIST_META_KEY to meta)
        }
    }

    private val mediaListMeta get() = arguments?.getParcelable<MediaListMeta>(MEDIA_LIST_META_KEY)
    override val setHomeAsUp: Boolean = true
    override val menuRes: Int = R.menu.list_activity_menu

    private var menuItem: MenuItem? = null

    private val listFragments by lazy {
        listOf(
            WatchingFragment.newInstance(mediaListMeta!!),
            CompletedFragment.newInstance(mediaListMeta!!),
            PausedFragment.newInstance(mediaListMeta!!),
            DroppedFragment.newInstance(mediaListMeta!!),
            PlanningFragment.newInstance(mediaListMeta!!),
            RepeatingFragment.newInstance(mediaListMeta!!)
        )
    }

    private val viewModel by viewModel<UserMediaListContainerViewModel>()

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): UserMediaListContainerFragmentLayoutBinding {
        return UserMediaListContainerFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }

    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val listMeta = mediaListMeta ?: return

        binding.initTabLayout()
        binding.mediaListViewPager.adapter = makePagerAdapter(listFragments)
        binding.mediaListViewPager.offscreenPageLimit = 5
        binding.mediaListViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                binding.mediaListTabLayout
            )
        )

        binding.mediaListTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.mediaListViewPager.setCurrentItem(tab.position, false)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        getBaseToolbar().title = when (listMeta.type) {
            MediaType.ANIME.ordinal -> {
                getString(R.string.anime_list)
            }
            else -> {
                getString(R.string.manga_list)
            }
        }


    }

    @SuppressLint("RestrictedApi")
    override fun onToolbarInflated() {
        val menu = getBaseToolbar().menu

        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }

        menu?.findItem(R.id.listSearchMenu)?.let { item ->
            menuItem = item
            (item.actionView as SearchView).also {
                it.setOnQueryTextListener(object :
                    SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        viewModel.mediaListFilterField.search = newText
                        filterMediaList()
                        return true
                    }
                })
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MediaListCollectionFilterEvent) {
        event.meta.let {
            with(viewModel.mediaListFilterField) {
                formatsIn = it.formatsIn
                status = it.status
                genre = it.genres
                listSort = it.mediaListSort
            }

        }
        filterMediaList()
    }


    private fun filterMediaList() {
        val count = binding.mediaListViewPager.adapter?.count ?: return

        for (i in 0 until count){
            getViewPagerFragment(i)?.saveCurrentFilter(viewModel.mediaListFilterField)
        }
        binding.mediaListViewPager.let {
            getViewPagerFragment(it.currentItem)?.filter()
        }
    }

    private fun getViewPagerFragment(pos: Int) =
        childFragmentManager.findFragmentByTag("android:switcher:${R.id.mediaListViewPager}:$pos") as? MediaListFragment


    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.listSearchMenu -> {
                true
            }
            R.id.listFilterMenu -> {
                if (mediaListMeta != null) {
                    MediaListFilterBottomSheetFragment().show(
                        requireContext(),
                        mediaListMeta!!.type
                    ) {
                        arguments =
                            bundleOf(MediaListFilterBottomSheetFragment.LIST_FILTER_PARCEL_KEY to viewModel.mediaListFilterField)
                    }
                }
                true
            }
            R.id.listDisplayModeMenu -> {
                makeArrayPopupMenu(
                    getBaseToolbar().findViewById(R.id.listSearchMenu),
                    resources.getStringArray(R.array.list_display_modes),
                    selectedPosition = getMediaListGridPresenter().ordinal
                ) { _, _, index, _ ->
                    setMediaListGridPresenter(index)
                    DisplayModeChangedEvent(DisplayTypes.MEDIA_LIST).postEvent
                }
                true
            }
            else -> false
        }
    }

    private fun UserMediaListContainerFragmentLayoutBinding.initTabLayout() {
        createTabLayout(R.string.watching, R.drawable.ic_watching)
        createTabLayout(R.string.completed, R.drawable.ic_completed)
        createTabLayout(R.string.paused, R.drawable.ic_paused_filled)
        createTabLayout(R.string.dropped, R.drawable.ic_dropped)
        createTabLayout(R.string.planning, R.drawable.ic_planning)
        createTabLayout(R.string.rewatching, R.drawable.ic_rewatching)
    }


    private fun UserMediaListContainerFragmentLayoutBinding.createTabLayout(
        @StringRes tabText: Int,
        @DrawableRes tabIcon: Int
    ) {
        val newTab = mediaListTabLayout.newTab().setText(tabText).setIcon(tabIcon)
        val iconView = newTab.view.getChildAt(0)!!

        val lp = iconView.layoutParams as ViewGroup.MarginLayoutParams
        lp.bottomMargin = 0;
        iconView.layoutParams = lp
        iconView.requestLayout()
        mediaListTabLayout.addTab(newTab)
    }

    override fun getBaseToolbar(): Toolbar {
        return binding.mediaListToolbar.dynamicToolbar
    }

}