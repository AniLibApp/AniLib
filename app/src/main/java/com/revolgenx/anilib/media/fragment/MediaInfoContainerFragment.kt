package com.revolgenx.anilib.media.fragment

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary
import com.revolgenx.anilib.common.preference.loadLegacyMediaBrowseTheme
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.common.ui.adapter.makeViewPagerAdapter2
import com.revolgenx.anilib.common.ui.adapter.registerOnPageChangeCallback
import com.revolgenx.anilib.common.ui.adapter.setupWithViewPager2
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.common.event.OpenImageEvent
import com.revolgenx.anilib.common.event.OpenMediaListEditorEvent
import com.revolgenx.anilib.common.event.OpenReviewComposerEvent
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.databinding.MediaInfoContainerFragmentLayoutBinding
import com.revolgenx.anilib.media.data.model.isAnime
import com.revolgenx.anilib.media.viewmodel.MediaInfoContainerSharedVM
import com.revolgenx.anilib.ui.view.drawable.DynamicBackgroundGradientDrawable
import com.revolgenx.anilib.ui.view.makeArrayPopupMenu
import com.revolgenx.anilib.ui.view.makeErrorToast
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class MediaInfoContainerFragment : BaseLayoutFragment<MediaInfoContainerFragmentLayoutBinding>(),
    EventBusListener {

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): MediaInfoContainerFragmentLayoutBinding {
        return MediaInfoContainerFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override val setHomeAsUp: Boolean = true
    override val menuRes: Int = R.menu.media_info_menu
    override fun getBaseToolbar(): Toolbar {
        return binding.mediaBrowserToolbar
    }

    companion object {
        const val MEDIA_INFO_META_KEY = "MEDIA_INFO_META_KEY"
        const val COLLAPSED = 0
        const val EXPANDED = 1

        fun newInstance(meta: MediaInfoMeta): MediaInfoContainerFragment =
            MediaInfoContainerFragment().also {
                it.arguments = bundleOf(MEDIA_INFO_META_KEY to meta)
            }
    }

    private var isFavourite = false
    private val mediaInfoMeta: MediaInfoMeta?
        get() = arguments?.getParcelable(
            MEDIA_INFO_META_KEY
        )


    private val seasons by lazy {
        resources.getStringArray(R.array.media_season)
    }

    private val viewModel by viewModel<MediaInfoContainerSharedVM>()
    private val mediaModel get() = viewModel.mediaModel

    private var state = COLLAPSED

    private var isCollapsingAnimationRunning = false
    private var isExpandingAnimationRunning = false

    private val offSetChangeListener =
        AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (isBindingEmpty()) return@OnOffsetChangedListener

            val appBarOffsetHeight =
                appBarLayout.totalScrollRange - binding.mediaBrowserToolbar.height

            val collapsingToolbarContentHideRange = appBarOffsetHeight / 1.4f
            val verticalOff = abs(verticalOffset)
            if (verticalOff > collapsingToolbarContentHideRange) {
                if (!isCollapsingAnimationRunning) {
                    binding.collapsingBarContentLayout.animate().alpha(0f)
                        .scaleX(0.6f)
                        .scaleY(0.6f)
                        .setDuration(300).withEndAction {
                            binding.collapsingBarContentLayout.visibility = View.INVISIBLE
                        }.setUpdateListener {
                            isCollapsingAnimationRunning = it.isRunning
                            isExpandingAnimationRunning = false
                        }.start()
                }

            } else {
                if (!isExpandingAnimationRunning) {
                    binding.collapsingBarContentLayout.animate().alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300).withStartAction {
                            binding.collapsingBarContentLayout.visibility = View.VISIBLE
                        }.setUpdateListener {
                            isExpandingAnimationRunning = it.isRunning
                            isCollapsingAnimationRunning = false
                        }.start()
                }
            }

            if (verticalOffset == 0) {
                if (state != EXPANDED) {
                    state = EXPANDED
                    updateToolbar()
                }
            } else if (verticalOff >= appBarOffsetHeight) {
                if (state != COLLAPSED) {
                    state = COLLAPSED
                    updateToolbar()
                }
            }
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mediaInfo = mediaInfoMeta ?: return

        viewModel.mediaLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.bind()
                }
                else -> {}
            }
        }

        binding.initListener()
        binding.initTabLayout()
        binding.updateTheme()
        binding.bindPossibleView()

        val mediaInfoList = mutableListOf(
            MediaOverviewFragment.newInstance(mediaInfo),
            MediaWatchFragment.newInstance(mediaInfo),
            MediaCharacterFragment.newInstance(mediaInfo),
            MediaStaffFragment.newInstance(mediaInfo),
            MediaReviewFragment.newInstance(mediaInfo),
            MediaStatsFragment.newInstance(mediaInfo)
        )

        loginContinue(false) {
            mediaInfoList.add(MediaSocialContainerFragment.newInstance(mediaInfo))
        }

        viewModel.onFavouriteChanged = {
            when (it) {
                is Resource.Success -> {
                    binding.bindFavourite()
                }
                is Resource.Error -> {
                    binding.bindFavourite()
                    makeErrorToast(R.string.failed_to_toggle)
                }
                else -> {

                }
            }
        }

        viewModel.onListEntryDataChanged = {
            when (it) {
                is Resource.Success -> {
                    updateMediaStatusView()
                }
                is Resource.Error -> {
                    makeErrorToast(R.string.operation_failed)
                }
                else -> {}
            }
        }

        binding.mediaInfoViewPager.adapter = makeViewPagerAdapter2(mediaInfoList)
        binding.mediaInfoViewPager.offscreenPageLimit = 5

        val tabs = mutableListOf(
            R.string.overview to R.drawable.ic_fire,
            R.string.watch to R.drawable.ic_watch,
            R.string.character to R.drawable.ic_character,
            R.string.staff to R.drawable.ic_staff,
            R.string.review to R.drawable.ic_review,
            R.string.stats to R.drawable.ic_chart
        )

        loginContinue(false) {
            tabs.add(R.string.social to R.drawable.ic_activity_union)
        }

        setupWithViewPager2(
            binding.mediaInfoTabLayout,
            binding.mediaInfoViewPager,
            tabs
        )

        registerOnPageChangeCallback(
            binding.mediaInfoViewPager,
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if (isBindingEmpty() || position == 0) return
                    binding.appbarLayout.setExpanded(false)
                }
            }
        )
    }


    override fun updateToolbar() {
        if (state == EXPANDED) {
            getBaseToolbar().menu.clear()
        } else {
            super.updateToolbar()
        }
    }


    private fun updateMediaStatusView() {
        val status = mediaModel?.mediaListEntry?.status ?: return
        if (loadLegacyMediaBrowseTheme()) {
            binding.updateLegacyListStatusView(status)
        } else {
            binding.updateListStatusView(status)
        }
    }

    private fun MediaInfoContainerFragmentLayoutBinding.updateListStatusView(status: Int) {
        val mediaListStatus =
            resources.getStringArray(if (isAnime(mediaModel)) R.array.anime_list_status else R.array.manga_list_status)
        mediaAddButton.text =
            mediaListStatus[status]
    }

    private fun MediaInfoContainerFragmentLayoutBinding.updateLegacyListStatusView(status: Int) {
        val mediaListStatus =
            resources.getStringArray(if (isAnime(mediaModel)) R.array.anime_list_status else R.array.manga_list_status)
        legacyMediaAddButton.text =
            mediaListStatus[status]
    }


    private fun MediaInfoContainerFragmentLayoutBinding.updateTheme() {
        if (loadLegacyMediaBrowseTheme()) {
            mediaBrowseContentLayout.visibility = View.GONE
            legacyMediaBrowseContentLayout.visibility = View.VISIBLE
            legacyBlurLayout.background = DynamicBackgroundGradientDrawable(
                orientation = GradientDrawable.Orientation.LEFT_RIGHT,
                alpha = 180
            )
        } else {
            binding.mediaBrowserCollapsingToolbar.setCollapsedTitleTextColor(
                dynamicTextColorPrimary
            )

            mediaBrowseContentLayout.visibility = View.VISIBLE
            legacyMediaBrowseContentLayout.visibility = View.GONE
            collapsingContentBlur.background = DynamicBackgroundGradientDrawable(
                orientation = GradientDrawable.Orientation.BOTTOM_TOP,
                alpha = 255
            )
            mediaAddLayout.updateChildTheme()
        }
        setToolbarTheme()
    }

    private fun MediaInfoContainerFragmentLayoutBinding.bindPossibleView() {
        if (mediaModel != null) return
        val media = mediaInfoMeta ?: return
        media.title?.let {
            val toolbar = getBaseToolbar()
            toolbar.title = it
        }
        media.let {
            if (loadLegacyMediaBrowseTheme()) {
                legacyMediaTitleTv.text = media.title ?: ""
                legacyMediaBrowserCoverImage.setImageURI(media.coverImage)
                legacyMediaBrowserBannerImage.setImageURI(media.bannerImage)
            } else {
                mediaTitleTv.text = media.title ?: ""
                mediaBrowserCoverImage.setImageURI(media.coverImage)
                mediaBrowserBannerImage.setImageURI(media.bannerImage)
            }
        }
    }

    private fun MediaInfoContainerFragmentLayoutBinding.bind() {
        val media = mediaModel ?: return
        if (loadLegacyMediaBrowseTheme()) {
            bindLegacyHeader()
        } else {
            bindHeader()
        }
        bindFavourite()
    }

    private fun MediaInfoContainerFragmentLayoutBinding.bindLegacyHeader() {
        val media = mediaModel ?: return
        val toolbarTitle = media.title?.romaji ?: media.title?.title().naText()

        legacyMediaTitleTv.text = toolbarTitle
        legacyMediaBrowserCoverImage.setImageURI(media.coverImage?.image())
        legacyMediaBrowserBannerImage.setImageURI(media.bannerImage)

        media.mediaListEntry?.status?.let {
            updateLegacyListStatusView(it)
        }
    }

    private fun MediaInfoContainerFragmentLayoutBinding.bindHeader() {
        val media = mediaModel ?: return
        val toolbarTitle = media.title?.romaji ?: media.title?.title().naText()

        mediaTitleTv.text = toolbarTitle
        mediaBrowserCoverImage.setImageURI(media.coverImage?.image())
        mediaBrowserBannerImage.setImageURI(media.bannerImage)

        mediaPopularityTv.text =
            media.popularity?.prettyNumberFormat().naText()
        mediaFavTv.text = media.favourites?.prettyNumberFormat().naText()

        if (isAnime(media)) {
            seasonYearTv.text =
                getString(R.string.source_seasonyear_s).format(media.season?.let { seasons[it] }
                    ?: "?", media.seasonYear ?: "?")
        } else {
            seasonYearTv.visibility = View.GONE
        }

        media.mediaListEntry?.status?.let {
            updateListStatusView(it)
        }

        media.nextAiringEpisode?.let {
            mediaAiringAtTv.text = getString(R.string.episode_airing_date).format(
                it.episode,
                it.airingAtModel!!.airingDateTime
            )
        }
    }

    private fun MediaInfoContainerFragmentLayoutBinding.bindFavourite() {
        val media = mediaModel ?: return
        mediaFavButton.setImageResource(if (media.isFavourite) R.drawable.ic_favourite else R.drawable.ic_not_favourite)
        updateFavouriteToolbar()
    }

    private fun MediaInfoContainerFragmentLayoutBinding.initTabLayout() {
        createTabLayout(R.string.overview, R.drawable.ic_fire)
        createTabLayout(R.string.watch, R.drawable.ic_watch)
        createTabLayout(R.string.character, R.drawable.ic_character)
        createTabLayout(R.string.staff, R.drawable.ic_staff)
        createTabLayout(R.string.review, R.drawable.ic_review)
        createTabLayout(R.string.stats, R.drawable.ic_chart)

        if (loggedIn()) {
            createTabLayout(R.string.social, R.drawable.ic_activity_union)
        }
    }

    private fun MediaInfoContainerFragmentLayoutBinding.createTabLayout(
        @StringRes tabText: Int,
        @DrawableRes tabIcon: Int
    ) {
        val newTab =
            mediaInfoTabLayout.newTab().setText(tabText).setIcon(tabIcon)
        mediaInfoTabLayout.addTab(newTab)
    }

    private fun MediaInfoContainerFragmentLayoutBinding.initListener() {
        appbarLayout.addOnOffsetChangedListener(offSetChangeListener)

        if (loadLegacyMediaBrowseTheme()) {
            legacyMediaAddContainerLayout.setOnClickListener {
                openListEditor()
            }

            legacyMediaAddMoreIv.setOnClickListener {
                val media = mediaModel ?: return@setOnClickListener
                val mediaListStatus =
                    if (media.isAnime) R.array.anime_list_status else R.array.manga_list_status
                makeArrayPopupMenu(
                    it,
                    resources.getStringArray(mediaListStatus)
                ) { _, _, position, _ ->
                    changeMediaListStatus(position)
                }
            }

            legacyMediaReviewButton.setOnClickListener {
                openReviewWriter()
            }

            legacyMediaFavButton.setOnClickListener {
                toggleFav()
            }

            legacyMediaBrowserBannerImage.setOnClickListener {
                val media = mediaModel ?: return@setOnClickListener
                OpenImageEvent(media.bannerImage).postEvent
            }

            legacyMediaBrowserCoverImage.setOnClickListener {
                val media = mediaModel ?: return@setOnClickListener
                OpenImageEvent(media.coverImage?.largeImage).postEvent
            }

            legacyMediaTitleTv.setOnLongClickListener {
                val title = mediaInfoMeta?.title ?: mediaModel?.title?.romaji
                ?: return@setOnLongClickListener false
                requireContext().copyToClipBoard(title)
                true
            }
        } else {
            mediaAddContainerLayout.setOnClickListener {
                openListEditor()
            }

            mediaAddMoreIv.setOnClickListener {
                val model = mediaModel ?: return@setOnClickListener
                val mediaListStatus =
                    if (model.isAnime) R.array.anime_list_status else R.array.manga_list_status
                makeArrayPopupMenu(
                    it,
                    resources.getStringArray(mediaListStatus)
                ) { _, _, position, _ ->
                    changeMediaListStatus(position)
                }
            }

            mediaReviewButton.setOnClickListener {
                openReviewWriter()
            }

            mediaFavButton.setOnClickListener {
                toggleFav()
            }

            mediaBrowserBannerImage.setOnClickListener {
                val media = mediaModel ?: return@setOnClickListener
                OpenImageEvent(media.bannerImage).postEvent
            }

            mediaBrowserCoverImage.setOnClickListener {
                val media = mediaModel ?: return@setOnClickListener
                OpenImageEvent(media.coverImage?.largeImage).postEvent
            }

            mediaTitleTv.setOnLongClickListener {
                val title = mediaInfoMeta?.title ?: mediaModel?.title?.romaji
                ?: return@setOnLongClickListener false
                requireContext().copyToClipBoard(title)
                true
            }

        }
    }


    @SuppressLint("RestrictedApi")
    override fun onToolbarInflated() {
        val toolbar = getBaseToolbar()
        toolbar.menu.let { menu ->
            if (menu is MenuBuilder) {
                menu.setOptionalIconsVisible(true)
            }
            val media = mediaModel ?: return
            toolbar.title = media.title?.romaji ?: media.title?.title().naText()
            updateFavouriteToolbar()
        }
    }

    private fun updateFavouriteToolbar() {
        val media = mediaModel ?: return
        val toolbar = getBaseToolbar()
        toolbar.menu.findItem(R.id.toggleFavMenu)
            ?.setIcon(if (media.isFavourite) R.drawable.ic_favourite else R.drawable.ic_not_favourite)
    }


    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addToListMenu -> {
                openListEditor()
                true
            }
            R.id.writeReviewMenu -> {
                openReviewWriter()
                true
            }
            R.id.toggleFavMenu -> {
                toggleFav()
                true
            }

            R.id.media_open_in_browser_menu -> {
                requireContext().openLink(mediaModel?.siteUrl)
                true
            }
            R.id.media_share_menu -> {
                shareText(mediaModel?.siteUrl)
                true
            }

            R.id.media_copy_menu -> {
                requireContext().copyToClipBoard(mediaModel?.siteUrl)
                true
            }
            else -> false
        }
    }

    private fun changeMediaListStatus(status: Int) {
        mediaModel ?: return
        loginContinue {
            viewModel.changeListEntryStatus(status)
        }

    }

    private fun openListEditor() {
        val media = mediaModel ?: return
        loginContinue {
            OpenMediaListEditorEvent(media.id).postEvent
        }
    }

    private fun toggleFav() {
        mediaModel ?: return
        loginContinue {
            viewModel.toggleFavourite()
        }
    }

    private fun openReviewWriter() {
        val media = mediaModel ?: return
        loginContinue {
            OpenReviewComposerEvent(media.id).postEvent
        }
    }


    private fun setToolbarTheme() {
        if (loadLegacyMediaBrowseTheme()) {
            binding.mediaBrowserCollapsingToolbar.setCollapsedTitleTextColor(
                dynamicTextColorPrimary
            )
            binding.mediaBrowserCollapsingToolbar.setBackgroundColor(
                dynamicBackgroundColor
            )
            binding.mediaBrowserCollapsingToolbar.setStatusBarScrimColor(
                dynamicBackgroundColor
            )
            binding.mediaBrowserCollapsingToolbar.setContentScrimColor(
                dynamicBackgroundColor
            )

            binding.mediaBrowserToolbar.colorType = Theme.ColorType.BACKGROUND
            binding.mediaBrowserToolbar.textColorType = Theme.ColorType.TEXT_PRIMARY
        } else {
            binding.mediaBrowserCollapsingToolbar.setCollapsedTitleTextColor(
                dynamicTextColorPrimary
            )
            binding.mediaBrowserCollapsingToolbar.setBackgroundColor(
                dynamicBackgroundColor
            )

            binding.mediaBrowserToolbar.colorType = Theme.ColorType.BACKGROUND
            binding.mediaBrowserToolbar.textColorType = Theme.ColorType.TEXT_PRIMARY
        }
    }

}