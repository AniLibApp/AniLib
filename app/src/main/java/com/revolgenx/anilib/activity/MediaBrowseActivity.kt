package com.revolgenx.anilib.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.view.*
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager.widget.ViewPager
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.appbar.AppBarLayout
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
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
import com.revolgenx.anilib.databinding.SmartTabLayoutBinding
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.repository.util.Status.*
import com.revolgenx.anilib.type.MediaType
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
    private lateinit var tabColorStateList: ColorStateList

    private var circularProgressDrawable: CircularProgressDrawable? = null

    private var browseMediaBrowseModel: MediaBrowseModel? = null

    private val viewModel by viewModel<MediaBrowserViewModel>()
    private val pageChangeListener by lazy {
        object :
            ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                if (isBindingEmpty()) return

                binding.apply {
                    dynamicSmartTab.baseDynamicSmartTab.getTabs()
                        .forEach { it.findViewById<View>(R.id.tab_text_tv).visibility = View.GONE }
                    dynamicSmartTab.baseDynamicSmartTab.getTabAt(position)
                        .findViewById<View>(R.id.tab_text_tv).visibility = View.VISIBLE
                    if (position == 0) return
                    appbarLayout.setExpanded(false)
                }
            }
        }
    }

    private val accentColor by lazy {
        DynamicTheme.getInstance().get().accentColor
    }
    private val tintAccentColor by lazy {
        DynamicTheme.getInstance().get().tintAccentColor
    }

    private var state = COLLAPSED

    private val offSetChangeListener =
        AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (isBindingEmpty()) return@OnOffsetChangedListener
            if (verticalOffset == 0) {
                state = EXPANDED
                invalidateOptionsMenu()
            } else if (abs(verticalOffset) >= (appBarLayout.totalScrollRange - binding.mediaBrowserToolbar.height)) {
                state = COLLAPSED
                invalidateOptionsMenu()
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
//        media_add_button.setCompoundDrawablesRelativeWithIntrinsicBounds(
//            null,
//            null,
//            ContextCompat.getDrawable(this, R.drawable.ic_arrow_down)?.also {
//                it.setTint(DynamicTheme.getInstance().get().tintSurfaceColor)
//            },
//            null
//        )

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

                    browseMediaBrowseModel?.mediaListStatus?.let {
                        binding.mediaAddButton.text =
                            resources.getStringArray(R.array.media_list_status)[it]
                    }

                    if (mediaBrowserMeta.coverImage == null || mediaBrowserMeta.bannerImage == null) {
                        mediaBrowserMeta.coverImage = it.data?.coverImage?.large ?: ""
                        mediaBrowserMeta.coverImageLarge = it.data?.coverImage?.largeImage
                        mediaBrowserMeta.bannerImage =
                            it.data?.bannerImage ?: it.data?.coverImage?.largeImage
                        mediaBrowserMeta.title = it.data?.title?.romaji ?: ""
                        binding.updateView()
                    }
                }
                else -> {
                }
            }
        }


        if (savedInstanceState == null)
            viewModel.getMediaInfo(mediaBrowserMeta.mediaId)

        val colors = intArrayOf(
            DynamicTheme.getInstance().get().accentColor,
            DynamicTheme.getInstance().get().tintPrimaryColor
        )
        setSupportActionBar(binding.mediaBrowserToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.updateView()

        tabColorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_selected),
                intArrayOf(android.R.attr.state_enabled)
            ), colors
        )
        binding.dynamicSmartTab.baseDynamicSmartTab.setBackgroundColor(
            DynamicTheme.getInstance().get().primaryColor
        )
        statusBarColor = statusBarColor
        setToolbarTheme()


        initListener()

        val inflater = LayoutInflater.from(this)
        binding.dynamicSmartTab.baseDynamicSmartTab.setCustomTabView { container, position, _ ->
            val view = inflater.inflate(R.layout.smart_tab_layout, container, false)
            when (position) {
                0 -> {
                    createTabView(view, R.drawable.ic_fire, R.string.overview)
                }
                1 -> {
                    createTabView(view, R.drawable.ic_watch, R.string.watch)
                }
                2 -> {
                    createTabView(view, R.drawable.ic_character, R.string.character)
                }
                3 -> {
                    createTabView(view, R.drawable.ic_staff, R.string.staff)
                }
                4 -> {
                    createTabView(view, R.drawable.ic_review, R.string.review)
                }
                5 -> {
                    createTabView(view, R.drawable.ic_chart, R.string.stats)
                }
                else -> {
                    null
                }
            }
        }


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

        viewModel.saveMediaListEntryLiveData.observe(this){
            if(it.status == SUCCESS){
                val data = it.data?:return@observe
                browseMediaBrowseModel?.mediaListStatus = data.status
                browseMediaBrowseModel?.mediaListStatus?.let {
                    binding.mediaAddButton.text =
                        resources.getStringArray(R.array.media_list_status)[it]
                }
            }
        }


        binding.browseMediaViewPager.adapter = MediaBrowserAdapter(animeBrowserList)
        binding.browseMediaViewPager.offscreenPageLimit = 5
        binding.dynamicSmartTab.baseDynamicSmartTab.setViewPager(binding.browseMediaViewPager) {
            if (it == 0) return@setViewPager
            binding.appbarLayout.setExpanded(false)
        }
        binding.browseMediaViewPager.setCurrentItem(0, false)
        binding.browseMediaViewPager.post {
            pageChangeListener.onPageSelected(binding.browseMediaViewPager.currentItem)
        }

    }

    private fun ActivityMediaBrowserBinding.updateView() {
        supportActionBar!!.title = mediaBrowserMeta.title
        mediaTitleTv.text = mediaBrowserMeta.title
        binding.mediaBrowserCoverImage.setImageURI(mediaBrowserMeta.coverImage)
        mediaBrowserBannerImage.setImageURI(mediaBrowserMeta.bannerImage)
    }


    private fun initListener() {
        binding.appbarLayout.addOnOffsetChangedListener(offSetChangeListener)

        binding.mediaAddButton.setOnClickListener {
            openListEditor()
        }

        binding.mediaAddMoreIv.onPopupMenuClickListener = { _, position ->
            changeMediaListStatus(position)
        }

        binding.mediaReviewButton.setOnClickListener {
            openReviewWriter()
        }

        binding.mediaFavButton.setOnClickListener {
            toggleFav()
        }

        binding.mediaBrowserBannerImage.setOnClickListener {
            SimpleDraweeViewerActivity.openActivity(
                this,
                DraweeViewerMeta(mediaBrowserMeta.bannerImage)
            )
        }

        binding.mediaBrowserCoverImage.setOnClickListener {
            SimpleDraweeViewerActivity.openActivity(
                this,
                DraweeViewerMeta(mediaBrowserMeta.coverImageLarge)
            )
        }

        binding.mediaTitleTv.setOnLongClickListener {
            copyToClipBoard(mediaBrowserMeta.title)
            true
        }

        /**problem with transition
         * {@link https://github.com/facebook/fresco/issues/1445}*/
        ActivityCompat.setExitSharedElementCallback(this, object : SharedElementCallback() {
            override fun onSharedElementEnd(
                sharedElementNames: List<String?>?,
                sharedElements: List<View>,
                sharedElementSnapshots: List<View?>?
            ) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)
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

    private fun createTabView(view: View, @DrawableRes src: Int, @StringRes str: Int): View {
        val smartTab = SmartTabLayoutBinding.bind(view)
        smartTab.tabImageView.imageTintList = tabColorStateList
        smartTab.tabImageView.setImageResource(src)
        smartTab.tabTextTv.text = getString(str)
        smartTab.root.background =
            RippleDrawable(ColorStateList.valueOf(tintAccentColor), null, null)
        smartTab.tabTextTv.setTextColor(accentColor)
        return smartTab.root
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


    override fun setStatusBarColor(color: Int) {
        super.setStatusBarColor(color)
        setWindowStatusBarColor(statusBarColor);
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

