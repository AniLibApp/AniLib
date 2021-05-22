package com.revolgenx.anilib.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.core.app.ActivityOptionsCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicPrimaryColor
import com.revolgenx.anilib.common.preference.getApplicationLocale
import com.revolgenx.anilib.common.ui.fragment.BaseFragment
import com.revolgenx.anilib.common.ui.fragment.ParcelableFragment
import com.revolgenx.anilib.data.meta.*
import com.revolgenx.anilib.infrastructure.event.*
import com.revolgenx.anilib.ui.dialog.MediaViewDialog
import com.revolgenx.anilib.ui.fragment.EntryListEditorFragment
import com.revolgenx.anilib.ui.fragment.review.ReviewFragment
import com.revolgenx.anilib.ui.fragment.studio.StudioFragment
import com.revolgenx.anilib.util.EventBusListener
import com.revolgenx.anilib.util.openLink
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

abstract class BaseDynamicActivity<T : ViewBinding> : DynamicSystemActivity(), EventBusListener {

    private var _binding: T? = null
    protected val binding: T get() = _binding!!
    fun isBindingEmpty() = _binding == null

    abstract fun bindView(inflater: LayoutInflater, parent: ViewGroup? = null): T


    override fun getLocale(): Locale? {
        return Locale(getApplicationLocale())
    }

    lateinit var rootLayout: View


    override fun getContentView(): View {
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = bindView(layoutInflater)
        super.onCreate(savedInstanceState)
        contentView = binding.root
        rootLayout = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        rootLayout.setBackgroundColor(DynamicTheme.getInstance().get().backgroundColor)
        statusBarColor = dynamicPrimaryColor
    }

    override fun setStatusBarColor(@ColorInt color: Int) {
        super.setStatusBarColor(color)
        setWindowStatusBarColor(statusBarColor)
    }


    override fun onStart() {
        super.onStart()
        registerForEvent()
    }


    override fun onStop() {
        unRegisterForEvent()
        super.onStop()
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBaseEvent(event: CommonEvent) {
        when (event) {
            is BrowseMediaEvent -> {
                startActivity(Intent(this, MediaBrowseActivity::class.java).apply {
                    this.putExtra(MediaBrowseActivity.MEDIA_BROWSER_META, event.mediaBrowserMeta)
                })
            }
            is ListEditorEvent -> {

                if (event.openInWithSupportFragment) {
                    val meta = event.meta
                    addFragmentToContainer(EntryListEditorFragment().also {
                        it.arguments =
                            bundleOf(EntryListEditorFragment.LIST_EDITOR_META_KEY to meta)
                    }, true, event.containerId)
                } else {

                    var options: ActivityOptionsCompat? = null
                    if (event.sharedElement != null) {
                        options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            this,
                            event.sharedElement,
                            ViewCompat.getTransitionName(event.sharedElement) ?: ""
                        )
                    }
                    ContainerActivity.openActivity(
                        this,
                        ParcelableFragment(
                            EntryListEditorFragment::class.java,
                            bundleOf(
                                EntryListEditorFragment.LIST_EDITOR_META_KEY to event.meta
                            )
                        ), options
                    )
                }

            }
            is BrowseGenreEvent -> {
                SearchActivity.openActivity(
                    this, event.genre
                )
            }

            is BrowseTrendingEvent -> {
                SearchActivity.openActivity(
                    this, event.trending
                )
            }

            is BrowsePopularEvent -> {
                SearchActivity.openActivity(
                    this, event.popular
                )
            }

            is BrowseNewlyAddedEvent -> {
                SearchActivity.openActivity(
                    this, event.newlyAdded
                )
            }

            is BrowseEvent -> {
                SearchActivity.openActivity(this)
            }

            is ImageClickedEvent -> {
                SimpleDraweeViewerActivity.openActivity(this, DraweeViewerMeta(event.meta.url))
            }

            is YoutubeClickedEvent -> {
                openLink(event.meta.url)
            }

            is VideoClickedEvent -> {
                openLink(event.videoMeta.url)
            }

            is UserBrowseEvent -> {
                UserProfileActivity.openActivity(this, UserMeta(event.userId, null))
            }

            is BrowseCharacterEvent -> {
                ViewPagerContainerActivity.openActivity(
                    this,
                    ViewPagerContainerMeta(
                        ViewPagerContainerType.CHARACTER,
                        event.meta
                    )
                )
            }

            is BrowseStaffEvent -> {
                ViewPagerContainerActivity.openActivity(
                    this,
                    ViewPagerContainerMeta(
                        ViewPagerContainerType.STAFF,
                        event.meta
                    )
                )
            }

            is BrowseStudioEvent -> {
                ContainerActivity.openActivity(
                    this,
                    ParcelableFragment(
                        StudioFragment::class.java,
                        bundleOf(StudioFragment.STUDIO_META_KEY to event.meta)
                    )
                )
            }

            is BrowseTagEvent -> {
                SearchActivity.openActivity(
                    this, event.model
                )
            }

            is BrowseReviewEvent -> {
                ContainerActivity.openActivity(
                    this,
                    ParcelableFragment(
                        ReviewFragment::class.java,
                        bundleOf(
                            ReviewFragment.reviewMetaKey to ReviewMeta(
                                event.reviewId
                            )
                        )
                    )
                )
            }

            is BrowseSiteEvent -> {
                openLink(getString(R.string.site_url))
            }

            is BrowseMediaListEvent -> {
                MediaListActivity.openActivity(
                    this,
                    event.mediaListMeta
                )
            }

            is MediaViewDialogEvent -> {
                MediaViewDialog.newInstance(event.mediaIdsIn)
                    .show(supportFragmentManager, MediaViewDialog::class.java.simpleName)
            }

        }
    }


    private fun addFragmentToContainer(
        baseFragment: BaseFragment,
        slideAnimation: Boolean = false,
        containerId: Int
    ) {
        getTransactionWithAnimation(slideAnimation)
            .add(containerId, baseFragment)
            .addToBackStack(null).commit()
    }


    protected fun getTransactionWithAnimation(slideAnimation: Boolean): FragmentTransaction {
        return supportFragmentManager.beginTransaction().apply {
            if (slideAnimation) {
                setCustomAnimations(
                    R.anim.slide_in_up,
                    R.anim.slide_out_down,
                    R.anim.slide_in_up,
                    R.anim.slide_out_down
                )
            } else {
                setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            }

        }
            .setReorderingAllowed(true)
    }


}