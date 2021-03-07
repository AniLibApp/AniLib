package com.revolgenx.anilib.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager.widget.ViewPager
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.loadLegacyMediaBrowseTheme
import com.revolgenx.anilib.data.field.ToggleFavouriteField
import com.revolgenx.anilib.ui.fragment.EntryListEditorFragment
import com.revolgenx.anilib.common.ui.fragment.BaseFragment
import com.revolgenx.anilib.common.ui.fragment.ParcelableFragment
import com.revolgenx.anilib.ui.fragment.browse.*
import com.revolgenx.anilib.ui.fragment.review.ReviewComposerFragment
import com.revolgenx.anilib.data.meta.DraweeViewerMeta
import com.revolgenx.anilib.data.meta.ListEditorMeta
import com.revolgenx.anilib.data.meta.MediaBrowserMeta
import com.revolgenx.anilib.data.meta.ReviewComposerMeta
import com.revolgenx.anilib.data.model.MediaBrowseModel
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.data.model.EntryListEditorMediaModel
import com.revolgenx.anilib.databinding.ActivityMediaBrowserBinding
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.repository.util.Status.*
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.ui.view.drawable.DynamicBackgroundGradientDrawable
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.*
import com.revolgenx.anilib.ui.viewmodel.media.MediaBrowserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

//todo://handle review
class MediaBrowseActivity : BaseDynamicActivity<ActivityMediaBrowserBinding>() {
    companion object {
        const val MEDIA_BROWSER_META = "media_browser_meta"
    }

    private var isFavourite = false
    private var toggling = false
    private lateinit var mediaBrowserMeta: MediaBrowserMeta

    private var circularProgressDrawable: CircularProgressDrawable? = null

    private var browseMediaBrowseModel: MediaBrowseModel? = null

    private val seasons by lazy {
        resources.getStringArray(R.array.media_season)
    }

    private val viewModel by viewModel<MediaBrowserViewModel>()
    private val pageChangeListener by lazy {
        object :
            ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                if (isBindingEmpty()) return

                binding.apply {
                    if (position == 0) return

                    mediaBrowserCollapsingToolbar.updateLayoutParams {
                        if (this !is AppBarLayout.LayoutParams) return
                        scrollFlags =
                            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
                        appbarLayout.setExpanded(false)

                    }
                }
            }
        }
    }

    private var state = COLLAPSED

    private val offSetChangeListener =
        AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (isBindingEmpty()) return@OnOffsetChangedListener

            val appBarOffsetHeight =
                appBarLayout.totalScrollRange - binding.mediaBrowserToolbar.height

            val collapsingToolbarContentHideRange = appBarOffsetHeight / 1.4f
            val verticalOff = abs(verticalOffset)
            if (verticalOff > collapsingToolbarContentHideRange) {
                binding.collapsingBarContentLayout.visibility = View.INVISIBLE
            } else {
                binding.collapsingBarContentLayout.visibility = View.VISIBLE
            }

            if (verticalOffset == 0) {
                state = EXPANDED
                invalidateOptionsMenu()
            } else if (verticalOff >= appBarOffsetHeight) {
                state = COLLAPSED
                invalidateOptionsMenu()

                binding.mediaBrowserCollapsingToolbar.layoutParams.let {
                    if (it is AppBarLayout.LayoutParams) {
                        val flags =
                            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                        if (it.scrollFlags != flags) {
                            it.scrollFlags = flags
                        }

                    }
                }

            }
        }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ActivityMediaBrowserBinding {
        return ActivityMediaBrowserBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent == null) {
            return
        }
        mediaBrowserMeta = if (intent.action == Intent.ACTION_VIEW) {
            val data = intent.data ?: return
            val paths = data.pathSegments
            val type = if (paths[0].compareTo("anime", true) == 0) {
                MediaType.ANIME.ordinal
            } else {
                MediaType.MANGA.ordinal
            }
            val mediaId = paths[1].toIntOrNull() ?: return

            MediaBrowserMeta(
                mediaId = mediaId,
                type,
                null,
                null,
                null,
                null
            )
        } else {
            intent.getParcelableExtra(MEDIA_BROWSER_META) ?: return
        }

        viewModel.mediaLiveData.observe(this) {
            when (it.status) {
                SUCCESS -> {
                    browseMediaBrowseModel = it.data

                    if (mediaBrowserMeta.coverImage == null || mediaBrowserMeta.bannerImage == null) {
                        mediaBrowserMeta.coverImage = it.data?.coverImage?.large ?: ""
                        mediaBrowserMeta.coverImageLarge = it.data?.coverImage?.largeImage
                        mediaBrowserMeta.bannerImage =
                            it.data?.bannerImage ?: it.data?.coverImage?.largeImage
                        mediaBrowserMeta.title = it.data?.title?.romaji ?: ""
                    }
                    binding.updateView()
                }
                else -> {
                }
            }
        }


        if (savedInstanceState == null) {
            viewModel.getMediaInfo(mediaBrowserMeta.mediaId)
        }

        setSupportActionBar(binding.mediaBrowserToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.initListener()
        binding.initTabLayout()
        binding.updateTheme()
        binding.updateView()

        val animeBrowserList = listOf(
            MediaOverviewFragment().apply {
                arguments = bundleOf(MEDIA_BROWSER_META to mediaBrowserMeta)
            },
            MediaWatchFragment().apply {
                arguments = bundleOf(MEDIA_BROWSER_META to mediaBrowserMeta)
            },
            MediaCharacterFragment().apply {
                arguments = bundleOf(MEDIA_BROWSER_META to mediaBrowserMeta)
            },
            MediaStaffFragment().apply {
                arguments = bundleOf(MEDIA_BROWSER_META to mediaBrowserMeta)
            },
            MediaReviewFragment().apply {
                arguments = bundleOf(MEDIA_BROWSER_META to mediaBrowserMeta)
            },
            MediaStatsFragment().apply {
                arguments = bundleOf(MEDIA_BROWSER_META to mediaBrowserMeta)
            }
        )

        binding.browseMediaViewPager.addOnPageChangeListener(pageChangeListener)
        binding.browseMediaViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                binding.browseMediaTabLayout
            )
        )


        viewModel.isFavourite(mediaBrowserMeta.mediaId).observe(this, Observer {
            when (it.status) {
                SUCCESS -> {
                    isFavourite = it.data!!
                    binding.mediaFavButton.setImageResource(if (isFavourite) R.drawable.ic_favourite else R.drawable.ic_not_favourite)
                    invalidateOptionsMenu()
                }
                else -> {

                }
            }
        })

        viewModel.toggleFavMediaLiveData.observe(this, {
            toggling = when (it.status) {
                SUCCESS -> {
                    isFavourite = !isFavourite
                    viewModel.isFavourite(mediaBrowserMeta.mediaId).value =
                        Resource.success(isFavourite)
                    binding.mediaFavButton.setImageResource(if (isFavourite) R.drawable.ic_favourite else R.drawable.ic_not_favourite)
                    invalidateOptionsMenu()
                    false
                }
                ERROR -> {
                    makeToast(R.string.failed_to_toggle, icon = R.drawable.ic_error)
                    false
                }
                LOADING -> {
                    true
                }
            }
        })

        viewModel.saveMediaListEntryLiveData.observe(this) {
            if (it.status == SUCCESS) {
                val data = it.data ?: return@observe
                browseMediaBrowseModel?.mediaListStatus = data.status
                browseMediaBrowseModel?.mediaListStatus?.let {status->
                    if(loadLegacyMediaBrowseTheme()){
                        binding.updateLegacyListStatusView(status)
                    }else{
                        binding.updateListStatusView(status)
                    }
                }
            }
        }


        binding.browseMediaViewPager.adapter = MediaBrowserAdapter(animeBrowserList)
        binding.browseMediaViewPager.offscreenPageLimit = 5
        binding.browseMediaTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position ?: return
                binding.browseMediaViewPager.setCurrentItem(position, false)
                if (position == 0) {
                    return
                }
                binding.appbarLayout.setExpanded(false)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        binding.browseMediaViewPager.setCurrentItem(0, false)
        binding.browseMediaViewPager.post {
            pageChangeListener.onPageSelected(binding.browseMediaViewPager.currentItem)
        }

    }

    private fun ActivityMediaBrowserBinding.updateListStatusView(status: Int) {
        mediaAddButton.text =
            resources.getStringArray(R.array.media_list_status)[status]
    }
    private fun ActivityMediaBrowserBinding.updateLegacyListStatusView(it: Int) {
        legacyMediaAddButton.text =
            resources.getStringArray(R.array.media_list_status)[it]
    }


    private fun ActivityMediaBrowserBinding.updateTheme() {
        if (loadLegacyMediaBrowseTheme()) {
            mediaBrowseContentLayout.visibility = View.GONE
            legacyMediaBrowseContentLayout.visibility = View.VISIBLE
            legacyBlurLayout.background = DynamicBackgroundGradientDrawable(
                orientation = GradientDrawable.Orientation.LEFT_RIGHT,
                alpha = 180
            )
            setToolbarTheme()
        } else {
            mediaBrowseContentLayout.visibility = View.VISIBLE
            legacyMediaBrowseContentLayout.visibility = View.GONE
            collapsingContentBlur.background = DynamicBackgroundGradientDrawable(
                orientation = GradientDrawable.Orientation.BOTTOM_TOP,
                alpha = 255
            )
            mediaAddLayout.updateChildTheme()
        }
    }

    private fun ActivityMediaBrowserBinding.updateView() {
        supportActionBar!!.title = mediaBrowserMeta.title

        if (loadLegacyMediaBrowseTheme()) {

            legacyMediaTitleTv.text = mediaBrowserMeta.title
            legacyMediaBrowserCoverImage.setImageURI(mediaBrowserMeta.coverImage)
            legacyMediaBrowserBannerImage.setImageURI(mediaBrowserMeta.bannerImage)

            browseMediaBrowseModel?.mediaListStatus?.let {
                updateLegacyListStatusView(it)
            }
        } else {
            mediaTitleTv.text = mediaBrowserMeta.title
            mediaBrowserCoverImage.setImageURI(mediaBrowserMeta.coverImage)
            mediaBrowserBannerImage.setImageURI(mediaBrowserMeta.bannerImage)

            browseMediaBrowseModel?.let { model ->
                mediaPopularityTv.text =
                    model.popularity?.prettyNumberFormat().naText()
                mediaFavTv.text = model.favourites?.prettyNumberFormat().naText()

                if (model.type == MediaType.ANIME.ordinal) {
                    seasonYearTv.text =
                        getString(R.string.source_seasonyear_s).format(model.season?.let { seasons[it] }
                            ?: "?", model.seasonYear ?: "?")
                } else {
                    seasonYearTv.visibility = View.GONE
                }

                model.mediaListStatus?.let {
                    binding.updateListStatusView(it)
                }

                model.airingTimeModel?.let {
                    mediaAiringAtTv.text = getString(R.string.episode_airing_date).format(
                        it.episode,
                        it.airingAt!!.airingDateTime
                    )
                }
            }

        }

    }



    private fun ActivityMediaBrowserBinding.initTabLayout() {
        createTabLayout(R.string.overview, R.drawable.ic_fire)
        createTabLayout(R.string.watch, R.drawable.ic_watch)
        createTabLayout(R.string.character, R.drawable.ic_character)
        createTabLayout(R.string.staff, R.drawable.ic_staff)
        createTabLayout(R.string.review, R.drawable.ic_review)
        createTabLayout(R.string.stats, R.drawable.ic_chart)
    }

    private fun ActivityMediaBrowserBinding.createTabLayout(
        @StringRes tabText: Int,
        @DrawableRes tabIcon: Int
    ) {
        val newTab =
            browseMediaTabLayout.newTab().setText(tabText).setIcon(tabIcon).setText(tabText)
                .setIcon(tabIcon)
        val iconView = newTab.view.getChildAt(0)
        val layoutParams = iconView?.layoutParams as? LinearLayout.LayoutParams
        layoutParams?.bottomMargin = 0
        iconView.layoutParams = layoutParams
        browseMediaTabLayout.addTab(newTab)
    }

    private fun ActivityMediaBrowserBinding.initListener() {

        appbarLayout.addOnOffsetChangedListener(offSetChangeListener)

        ViewCompat.setOnApplyWindowInsetsListener(binding.appbarLayout) { _, insets ->
            // Instead of
            // toolbar.setPadding(0, insets.systemWindowInsetTop, 0, 0)
            (binding.mediaBrowserToolbar.layoutParams as ViewGroup.MarginLayoutParams).topMargin =
                insets.systemWindowInsetTop
            insets.consumeSystemWindowInsets()
        }

        if (loadLegacyMediaBrowseTheme()) {
            legacyMediaAddContainerLayout.setOnClickListener {
                openListEditor()
            }

            legacyMediaAddMoreIv.onPopupMenuClickListener = { _, position ->
                changeMediaListStatus(position)
            }

            legacyMediaReviewButton.setOnClickListener {
                openReviewWriter()
            }

            legacyMediaFavButton.setOnClickListener {
                toggleFav()
            }

            legacyMediaBrowserBannerImage.setOnClickListener {
                SimpleDraweeViewerActivity.openActivity(
                    this@MediaBrowseActivity,
                    DraweeViewerMeta(mediaBrowserMeta.bannerImage)
                )
            }

            legacyMediaBrowserCoverImage.setOnClickListener {
                SimpleDraweeViewerActivity.openActivity(
                    this@MediaBrowseActivity,
                    DraweeViewerMeta(mediaBrowserMeta.coverImageLarge)
                )
            }

            legacyMediaTitleTv.setOnLongClickListener {
                copyToClipBoard(mediaBrowserMeta.title)
                true
            }
        } else {
            mediaAddContainerLayout.setOnClickListener {
                openListEditor()
            }

            mediaAddMoreIv.onPopupMenuClickListener = { _, position ->
                changeMediaListStatus(position)
            }

            mediaReviewButton.setOnClickListener {
                openReviewWriter()
            }

            mediaFavButton.setOnClickListener {
                toggleFav()
            }

            mediaBrowserBannerImage.setOnClickListener {
                SimpleDraweeViewerActivity.openActivity(
                    this@MediaBrowseActivity,
                    DraweeViewerMeta(mediaBrowserMeta.bannerImage)
                )
            }

            mediaBrowserCoverImage.setOnClickListener {
                SimpleDraweeViewerActivity.openActivity(
                    this@MediaBrowseActivity,
                    DraweeViewerMeta(mediaBrowserMeta.coverImageLarge)
                )
            }

            mediaTitleTv.setOnLongClickListener {
                copyToClipBoard(mediaBrowserMeta.title)
                true
            }

        }


        /**problem with transition
         * {@link https://github.com/facebook/fresco/issues/1445}*/
        ActivityCompat.setExitSharedElementCallback(
            this@MediaBrowseActivity,
            object : SharedElementCallback() {
                override fun onSharedElementEnd(
                    sharedElementNames: List<String?>?,
                    sharedElements: List<View>,
                    sharedElementSnapshots: List<View?>?
                ) {
                    super.onSharedElementEnd(
                        sharedElementNames,
                        sharedElements,
                        sharedElementSnapshots
                    )
                    if (sharedElements.isEmpty()) {
                        return
                    }
                    for (view in sharedElements) {
                        if (view is SimpleDraweeView) {
                            view.drawable.setVisible(true, true)
                        }
                    }
                }
            })
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (state == EXPANDED) return true

        menuInflater.inflate(R.menu.media_browser_menu, menu)
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        if (isFavourite) {
            val drawable =
                ContextCompat.getDrawable(context, R.drawable.ic_favourite)
            menu?.findItem(R.id.toggleFavMenu)?.icon = drawable
        }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.addToListMenu -> {
                openListEditor()
                true
            }
            R.id.writeReviewMenu -> {
                openReviewWriter()
                true
            }
            R.id.toggleFavMenu -> {
                if (loggedIn())
                    makeToast(R.string.please_wait, icon = R.drawable.ic_hour_glass)

                toggleFav()
                true
            }
            else -> false
        }
    }

    private fun changeMediaListStatus(position: Int) {
        if (!::mediaBrowserMeta.isInitialized) return

        if (!loggedIn()) {
            makeToast(R.string.please_log_in, null, R.drawable.ic_person)
            return
        }


        viewModel.saveMediaListEntry(EntryListEditorMediaModel().also {
            it.mediaId = mediaBrowserMeta.mediaId
            it.status = position
        })

    }

    private fun openListEditor() {
        if (!::mediaBrowserMeta.isInitialized) return

        if (loggedIn()) {
            val meta = ListEditorMeta(
                mediaBrowserMeta.mediaId,
                mediaBrowserMeta.type,
                mediaBrowserMeta.title,
                mediaBrowserMeta.coverImage,
                mediaBrowserMeta.bannerImage
            )

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                binding.mediaBrowserCoverImage,
                ViewCompat.getTransitionName(binding.mediaBrowserCoverImage) ?: ""
            )

            ContainerActivity.openActivity(
                this,
                ParcelableFragment(
                    EntryListEditorFragment::class.java,
                    bundleOf(
                        EntryListEditorFragment.LIST_EDITOR_META_KEY to meta
                    )
                ), options
            )
        } else {
            makeToast(R.string.please_log_in, null, R.drawable.ic_person)
        }
    }

    private fun toggleFav() {
        if (toggling || !::mediaBrowserMeta.isInitialized) return

        if (!loggedIn()) {
            makeToast(R.string.please_log_in, null, R.drawable.ic_person)
            return
        }

        viewModel.toggleMediaFavourite(ToggleFavouriteField().also {
            when (mediaBrowserMeta.type) {
                MediaType.ANIME.ordinal -> {
                    it.animeId = mediaBrowserMeta.mediaId
                }
                MediaType.MANGA.ordinal -> {
                    it.mangaId = mediaBrowserMeta.mediaId
                }
            }
        })
    }

    private fun openReviewWriter() {
        if (!::mediaBrowserMeta.isInitialized) return
        if (loggedIn()) {
            ContainerActivity.openActivity(
                this,
                ParcelableFragment(
                    ReviewComposerFragment::class.java,
                    bundleOf(
                        ReviewComposerFragment.reviewComposerMetaKey to ReviewComposerMeta(
                            mediaBrowserMeta.mediaId
                        )
                    )
                )
            )
        } else {
            makeToast(R.string.please_log_in, null, R.drawable.ic_person)
        }
    }


    private fun setToolbarTheme() {
        binding.mediaBrowserCollapsingToolbar.setStatusBarScrimColor(
            DynamicTheme.getInstance().get().primaryColorDark
        )
        binding.mediaBrowserCollapsingToolbar.setContentScrimColor(
            DynamicTheme.getInstance().get().primaryColor
        )
        binding.mediaBrowserCollapsingToolbar.setCollapsedTitleTextColor(
            DynamicTheme.getInstance().get().tintPrimaryColor
        )
        binding.mediaBrowserCollapsingToolbar.setBackgroundColor(
            DynamicTheme.getInstance().get().backgroundColor
        )
        if (loadLegacyMediaBrowseTheme()) {
            binding.mediaBrowserToolbar.colorType = Theme.ColorType.PRIMARY
            binding.mediaBrowserToolbar.textColorType = Theme.ColorType.TINT_PRIMARY
        }
    }


    inner class MediaBrowserAdapter(private val fragmentList: List<BaseFragment>) :
        FragmentPagerAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }
    }


    override fun finishAfterTransition() {
        finish()
//        this.overridePendingTransition(0,android.R.anim.slide_out_right);
    }


    override fun onDestroy() {
        circularProgressDrawable?.stop()
        super.onDestroy()
    }

}

