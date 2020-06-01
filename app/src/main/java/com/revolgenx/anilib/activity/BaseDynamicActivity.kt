package com.revolgenx.anilib.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.AppController
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.event.*
import com.revolgenx.anilib.fragment.EntryListEditorFragment
import com.revolgenx.anilib.fragment.base.ParcelableFragment
import com.revolgenx.anilib.fragment.review.AllReviewFragment
import com.revolgenx.anilib.fragment.review.ReviewFragment
import com.revolgenx.anilib.fragment.studio.StudioFragment
import com.revolgenx.anilib.meta.*
import com.revolgenx.anilib.util.openLink
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

abstract class BaseDynamicActivity : DynamicSystemActivity() {

    override fun getLocale(): Locale? {
        return null
    }

    override fun getThemeRes(): Int {
        return ThemeController.appStyle
    }

    override fun onCustomiseTheme() {
        ThemeController.setLocalTheme()
    }

    override fun setStatusBarColor(color: Int) {
        super.setStatusBarColor(color)
        setWindowStatusBarColor(statusBarColor);
    }

    override fun setNavigationBarTheme(): Boolean {
        return AppController.instance.isThemeNavigationBar
    }

    abstract val layoutRes: Int
    lateinit var rootLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        rootLayout = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        rootLayout.setBackgroundColor(DynamicTheme.getInstance().get().backgroundColor)
        statusBarColor = statusBarColor
    }


    override fun onStart() {
        super.onStart()
        registerForEvent()
    }


    override fun onStop() {
        unRegisterForEvent()
        super.onStop()
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
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    event.sharedElement,
                    ViewCompat.getTransitionName(event.sharedElement) ?: ""
                )
                ContainerActivity.openActivity(
                    this,
                    ParcelableFragment(
                        EntryListEditorFragment::class.java,
                        bundleOf(
                            EntryListEditorFragment.LIST_EDITOR_META_KEY to event.meta
                        )
                    )
                    , options
                )
            }
            is BrowseGenreEvent -> {
                BrowseActivity.openActivity(
                    this, event.genre
                )
            }

            is BrowseTrendingEvent -> {
                BrowseActivity.openActivity(
                    this, event.trending
                )
            }

            is BrowsePopularEvent -> {
                BrowseActivity.openActivity(
                    this, event.popular
                )
            }

            is BrowseNewlyAddedEvent -> {
                BrowseActivity.openActivity(
                    this, event.newlyAdded
                )
            }

            is BrowseEvent -> {
                BrowseActivity.openActivity(this)
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
                BrowseActivity.openActivity(
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

            is BrowseAllReviewsEvent -> {
                ToolbarContainerActivity.openActivity(
                    this,
                    ParcelableFragment(
                        AllReviewFragment::class.java,
                        null
                    )
                )
            }

            is BrowseSiteEvent -> {
                openLink(getString(R.string.site_url))
            }
        }
    }

}