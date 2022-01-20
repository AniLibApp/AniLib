package com.revolgenx.anilib.ui.fragment.home.list

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicAccentColor
import com.revolgenx.anilib.common.preference.getMediaListGridPresenter
import com.revolgenx.anilib.common.preference.recentAnimeListStatus
import com.revolgenx.anilib.common.preference.recentMangaListStatus
import com.revolgenx.anilib.common.preference.setMediaListGridPresenter
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.data.model.list.MediaListCountTypeModel
import com.revolgenx.anilib.databinding.ListContainerFragmentBinding
import com.revolgenx.anilib.infrastructure.event.*
import com.revolgenx.anilib.ui.bottomsheet.list.MediaListFilterBottomSheetFragment
import com.revolgenx.anilib.ui.view.makeArrayPopupMenu
import com.revolgenx.anilib.ui.view.setBoundsFor
import com.revolgenx.anilib.notification.viewmodel.NotificationStoreViewModel
import com.revolgenx.anilib.util.EventBusListener
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class ListContainerFragment : BaseLayoutFragment<ListContainerFragmentBinding>(), EventBusListener {

    private lateinit var adapter: FragmentPagerAdapter

    private var currentItem: Int? = null

    private val currentListPage get() = binding.listViewPager.currentItem

    private val animeListStatus by lazy {
        requireContext().resources.getStringArray(R.array.anime_list_status)
    }

    private val animeListStatusWithCount = mutableMapOf<Int, String>()
    private val mangaListStatusWithCount = mutableMapOf<Int, String>()

    private var scrollState: Int = ViewPager.SCROLL_STATE_IDLE

    private val mangaListStatus by lazy {
        requireContext().resources.getStringArray(R.array.manga_list_status)
    }

    private val notificationStoreVM by sharedViewModel<NotificationStoreViewModel>()

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ListContainerFragmentBinding {
        return ListContainerFragmentBinding.inflate(inflater, parent, false)
    }

    private val listContainerFragments by lazy {
        listOf(
            AnimeListContainerFragment(),
            MangaListContainerFragment()
        )
    }


    private val onPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.showSearchET(false)
            binding.updateFabText()
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            scrollState = state
        }
    }

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }

    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }

    @Subscribe()
    fun onEvent(event: MediaListCollectionFilterEvent) {
        ListEvent.ListFilterChangedEvent(currentListPage, event.meta).postEvent
    }

//    override fun onResume() {
//        super.onResume()
//        if (!visibleToUser) {
//            visibleToUser = true
//            viewModel.getListStatusCount()
//        }
//    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            adapter = makePagerAdapter(
                listContainerFragments,
                resources.getStringArray(R.array.list_tab_menu)
            )

            listViewPager.adapter = adapter

            initListener()

            currentItem?.let {
                listViewPager.currentItem = it
                currentItem = null
            }

            prepareListStatusString(null)
            updateFabText()
            updateTheme()


        }
    }

    private fun ListContainerFragmentBinding.initFabListener() {
        listExtendedFab.setOnLongClickListener {
            MediaListFilterBottomSheetFragment().show(requireContext(), currentListPage) {
            }
            true
        }

        listExtendedFab.setOnClickListener {
            makeArrayPopupMenu(
                it,
                getCurrentListStatus(),
                icons = intArrayOf(
                    R.drawable.ic_watching,
                    R.drawable.ic_completed,
                    R.drawable.ic_paused_filled,
                    R.drawable.ic_dropped,
                    R.drawable.ic_planning,
                    R.drawable.ic_rewatching
                ),
                selectedPosition = getRecentListStatus()
            ) { _, _, position, _ ->
                updateCurrentListStatus(position)
                updateFabText()
                sendListStatusChangedEvent(position)
            }
        }
    }

    private fun sendListStatusChangedEvent(position: Int) {
        ListEvent.ListStatusChangedEvent(currentListPage, position).postEvent
    }

    private fun getCurrentListStatus(): Array<String> {
        return if (currentListPage == 0) {
            animeListStatusWithCount.values.toTypedArray()
        } else {
            mangaListStatusWithCount.values.toTypedArray()
        }
    }

    private fun updateCurrentListStatus(currentStatus: Int) {
        binding.showSearchET(false)

        if (currentListPage == 0) {
            recentAnimeListStatus(requireContext(), currentStatus)
        } else {
            recentMangaListStatus(requireContext(), currentStatus)
        }

    }

    private fun ListContainerFragmentBinding.initListener() {
        listViewPager.addOnPageChangeListener(onPageChangeListener)
        listTabLayout.setupWithViewPager(listViewPager)

        listSearchIv.setOnClickListener {
            showSearchET()
        }

        initFabListener();


        initNotificationBadge()

        listNotificationIv.setOnClickListener {
            notificationStoreVM.setUnreadNotificationCount(0)
            OpenNotificationCenterEvent().postEvent
        }

        listMoreIv.onPopupMenuClickListener = { _, position ->
            when (position) {
                0 -> {
                    makeArrayPopupMenu(
                        listMoreIv,
                        requireContext().resources.getStringArray(R.array.list_display_modes),
                        selectedPosition = getMediaListGridPresenter().ordinal
                    ) { _, _, index, _ ->
                        setMediaListGridPresenter(index)
                        DisplayModeChangedEvent(DisplayTypes.MEDIA_LIST).postEvent
                    }
                }
                1 -> {
                    MediaListFilterBottomSheetFragment().show(
                        requireContext(),
                        currentListPage
                    ) {
                    }
                }
            }
        }


        mediaListSearchEt.doOnTextChanged { text, _, _, _ ->
            if (scrollState == ViewPager.SCROLL_STATE_DRAGGING || scrollState == ViewPager.SCROLL_STATE_SETTLING) {
                getViewPagerFragment(abs(listViewPager.currentItem - 1)) {
                    searchQuery(text?.toString() ?: "")
                }

            } else {
                getViewPagerFragment(listViewPager.currentItem) {
                    searchQuery(text?.toString() ?: "")
                }
            }

        }

        clearText.setOnClickListener {
            val mediaSearchEt = mediaListSearchEt
            if (mediaSearchEt.text.isNullOrEmpty()) {
                showSearchET(false)
            } else {
                mediaSearchEt.setText("")
                mediaSearchEt.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
            }
        }

        mediaListSearchEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId != EditorInfo.IME_ACTION_SEARCH) return@setOnEditorActionListener false

            getViewPagerFragment(listViewPager.currentItem) {
                searchQuery(mediaListSearchEt.text?.toString() ?: "")
            }
            true
        }

    }

    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    private fun initNotificationBadge() {
        binding.listNotificationIv.post {
            val badgeDrawable = BadgeDrawable.create(requireContext())

            badgeDrawable.badgeGravity = BadgeDrawable.TOP_END
            badgeDrawable.backgroundColor = dynamicAccentColor
            badgeDrawable.verticalOffset = 4
            badgeDrawable.horizontalOffset = 4
            badgeDrawable.setBoundsFor(binding.listNotificationIv, binding.listNotifLayout)
            notificationStoreVM.unreadNotificationCount.observe(viewLifecycleOwner, {
                if (it > 0) {
                    BadgeUtils.attachBadgeDrawable(
                        badgeDrawable,
                        binding.listNotificationIv,
                        binding.listNotifLayout
                    )
                } else {
                    BadgeUtils.detachBadgeDrawable(
                        badgeDrawable,
                        binding.listNotificationIv
                    )
                }
            })
        }
    }


    private fun ListContainerFragmentBinding.showSearchET(b: Boolean? = null) {
        if (b == null) {
            val searchLayoutIsVisible = mediaListSearchLayout.isVisible
            mediaListSearchLayout.visibility =
                if (searchLayoutIsVisible) {
                    if (!mediaListSearchEt.text.isNullOrBlank()) {
                        mediaListSearchEt.setText("")
                    }
                    View.GONE
                } else {
                    if (!mediaListSearchEt.text.isNullOrBlank()) {
                        mediaListSearchEt.setText("")
                    }
                    mediaListSearchEt.requestFocus()
                    (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
                        mediaListSearchEt,
                        0
                    )
                    View.VISIBLE
                }
        } else {
            if (b == true) {
                mediaListSearchLayout.visibility = View.VISIBLE
                if (!mediaListSearchEt.text.isNullOrBlank()) {
                    mediaListSearchEt.setText("")
                }
                mediaListSearchEt.requestFocus()
                (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
                    mediaListSearchEt,
                    0
                )
            } else {
                if (!mediaListSearchEt.text.isNullOrBlank()) {
                    mediaListSearchEt.setText("")
                }
                mediaListSearchLayout.visibility = View.GONE
                return
            }
        }
    }

    private fun getViewPagerFragment(pos: Int, func: MediaListContainerFragment.() -> Unit) =
        (childFragmentManager.findFragmentByTag("android:switcher:${R.id.list_view_pager}:$pos") as? MediaListContainerFragment)?.func()


    private fun ListContainerFragmentBinding.updateTheme() {
        listExtendedFab.isAllowExtended = true
        listExtendedFab.isExtended = true
        mediaListSearchEt.typeface =
            ResourcesCompat.getFont(requireContext(), R.font.rubik_regular)

    }

    private fun ListContainerFragmentBinding.updateFabText() {
        val status = if (currentListPage == 0) {
            val recentStatus = recentAnimeListStatus(requireContext())
            animeListStatusWithCount[recentStatus]
        } else {
            val recentStatus = recentMangaListStatus(requireContext())
            mangaListStatusWithCount[recentStatus]
        }
        listExtendedFab.text = status
    }


    // uncomment it if api endpoint is fixed
    private fun prepareListStatusString(listStatusCountModel: List<MediaListCountTypeModel>?) {
        animeListStatus.forEachIndexed { i, post ->
            animeListStatusWithCount[i] =
                post
        }
        mangaListStatus.forEachIndexed { i, post ->
            mangaListStatusWithCount[i] = post
        }
//        val statusWithCountFormat = getString(R.string.status_with_count_format)

//        animeListStatus.forEachIndexed { i, post ->
//            animeListStatusWithCount[i] =
//                statusWithCountFormat.format(post, 0)
//        }
//        mangaListStatus.forEachIndexed { i, post ->
//            mangaListStatusWithCount[i] =
//                statusWithCountFormat.format(post, 0)
//        }

//        val animeListStatusCountModel =
//            listStatusCountModel?.firstOrNull { it.type == MediaType.ANIME.ordinal }
//
//        val mangaListStatusCountModel =
//            listStatusCountModel?.firstOrNull { it.type == MediaType.MANGA.ordinal }
//
//        animeListStatusCountModel?.mediaListCountModels?.forEach {
//            animeListStatusWithCount[it.status] =
//                statusWithCountFormat.format(animeListStatus[it.status], it.count)
//        }
//
//        mangaListStatusCountModel?.mediaListCountModels?.forEach {
//            mangaListStatusWithCount[it.status] =
//                statusWithCountFormat.format(mangaListStatus[it.status], it.count)
//        }
    }

    private fun getRecentListStatus() =
        if (currentListPage == 0) recentAnimeListStatus(requireContext()) else recentMangaListStatus(
            requireContext()
        )

    @Subscribe(sticky = true)
    fun getToListPage(event: ChangeViewPagerPageEvent) {
        if (event.data is ListContainerFragmentPage) {
            EventBus.getDefault().removeStickyEvent(event)
            val listOrderPage = event.data.ordinal
            if (::adapter.isInitialized) {
                binding.listViewPager.currentItem = listOrderPage
            } else {
                currentItem = listOrderPage
            }
        }
    }

    override fun onDestroyView() {
        binding.listViewPager.clearOnPageChangeListeners()
        super.onDestroyView()
    }
}
