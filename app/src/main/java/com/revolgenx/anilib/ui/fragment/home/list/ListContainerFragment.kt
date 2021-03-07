package com.revolgenx.anilib.ui.fragment.home.list

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
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.getMediaListGridPresenter
import com.revolgenx.anilib.common.preference.recentAnimeListStatus
import com.revolgenx.anilib.common.preference.recentMangaListStatus
import com.revolgenx.anilib.common.preference.setMediaListGridPresenter
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.data.model.list.MediaListCountTypeModel
import com.revolgenx.anilib.databinding.ListContainerFragmentBinding
import com.revolgenx.anilib.infrastructure.event.*
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.ui.bottomsheet.list.MediaListFilterBottomSheetFragment
import com.revolgenx.anilib.ui.view.makeArrayPopupMenu
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.ui.viewmodel.list.ListContainerViewModel
import com.revolgenx.anilib.util.EventBusListener
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListContainerFragment : BaseLayoutFragment<ListContainerFragmentBinding>(), EventBusListener {

    private lateinit var adapter: FragmentPagerAdapter

    private var currentItem: Int? = null

    private val currentListPage get() = binding.listViewPager.currentItem

    private val animeListStatus by lazy {
        requireContext().resources.getStringArray(R.array.anime_list_status)
    }

    private val animeListStatusWithCount = mutableMapOf<Int, String>()
    private val mangaListStatusWithCount = mutableMapOf<Int, String>()

    private val mangaListStatus by lazy {
        requireContext().resources.getStringArray(R.array.manga_list_status)
    }

    private val viewModel by viewModel<ListContainerViewModel>()

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

    override fun onResume() {
        super.onResume()
        if (!visibleToUser) {
            visibleToUser = true
            viewModel.getListStatusCount()
        }
    }

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

            viewModel.listCountLiveData.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        val data = it.data!!
                        prepareListStatusString(data)
                        updateFabText()
                    }
                    Status.ERROR -> {
                        makeToast(R.string.failed_to_load_list_count)
                    }
                    Status.LOADING -> {
                    }
                }
            }
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

        listNotificationIv.setOnClickListener {
            BrowseNotificationEvent().postEvent
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
            getViewPagerFragment(listViewPager.currentItem) {
                searchQuery(text?.toString()?:"")
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


    private fun ListContainerFragmentBinding.showSearchET(b: Boolean? = null) {
        if (b == null) {
            val searchLayoutIsVisible = mediaListSearchLayout.isVisible
            mediaListSearchLayout.visibility =
                if (searchLayoutIsVisible) {
                    mediaListSearchEt.setText("")
                    mediaListSearchEt.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
                    View.GONE
                } else {
                    mediaListSearchEt.setText("")
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
                mediaListSearchEt.setText("")
                mediaListSearchEt.requestFocus()
                (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
                    mediaListSearchEt,
                    0
                )
            } else {
                mediaListSearchEt.setText("")
                mediaListSearchEt.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
                mediaListSearchLayout.visibility = View.GONE
                return
            }
        }
    }

    private fun getViewPagerFragment(pos: Int, func:MediaListContainerFragment.()->Unit) =
        (childFragmentManager.findFragmentByTag("android:switcher:${R.id.list_view_pager}:$pos") as? MediaListContainerFragment)?.func()



    private fun ListContainerFragmentBinding.updateTheme(){
        listExtendedFab.isAllowExtended = true
        listExtendedFab.isExtended = true
        mediaListSearchEt.typeface =
            ResourcesCompat.getFont(requireContext(), R.font.cabin_regular)

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


    private fun prepareListStatusString(listStatusCountModel: List<MediaListCountTypeModel>?) {

        val statusWithCountFormat = getString(R.string.status_with_count_format)

        animeListStatus.forEachIndexed { i, post ->
            animeListStatusWithCount[i] =
                statusWithCountFormat.format(post, 0)
        }
        mangaListStatus.forEachIndexed { i, post ->
            mangaListStatusWithCount[i] =
                statusWithCountFormat.format(post, 0)
        }

        val animeListStatusCountModel =
            listStatusCountModel?.firstOrNull { it.type == MediaType.ANIME.ordinal }

        val mangaListStatusCountModel =
            listStatusCountModel?.firstOrNull { it.type == MediaType.MANGA.ordinal }

        animeListStatusCountModel?.mediaListCountModels?.forEach {
            animeListStatusWithCount[it.status] =
                statusWithCountFormat.format(animeListStatus[it.status], it.count)
        }

        mangaListStatusCountModel?.mediaListCountModels?.forEach {
            mangaListStatusWithCount[it.status] =
                statusWithCountFormat.format(mangaListStatus[it.status], it.count)
        }
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
