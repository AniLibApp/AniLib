package com.revolgenx.anilib.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import androidx.core.os.bundleOf
import androidx.core.view.forEachIndexed
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseFragment
import com.revolgenx.anilib.common.ui.fragment.ViewPagerParcelableFragments
import com.revolgenx.anilib.ui.fragment.character.CharacterActorFragment
import com.revolgenx.anilib.ui.fragment.character.CharacterFragment
import com.revolgenx.anilib.ui.fragment.character.CharacterMediaFragment
import com.revolgenx.anilib.ui.fragment.staff.StaffFragment
import com.revolgenx.anilib.ui.fragment.staff.StaffMediaCharacterFragment
import com.revolgenx.anilib.ui.fragment.staff.StaffMediaRoleFragment
import com.revolgenx.anilib.ui.fragment.user.*
import com.revolgenx.anilib.data.meta.ViewPagerContainerMeta
import com.revolgenx.anilib.data.meta.ViewPagerContainerType
import com.revolgenx.anilib.util.unRegisterForEvent
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.viewpager_container_activity.*

class ViewPagerContainerActivity : BaseDynamicActivity() {

    companion object {
        const val viewPagerContainerKey = "viewpager_activity_container_key"
        const val viewPagerContainerMetaKey = "view_pager_container_meta_key"

        fun openActivity(
            activity: Context,
            parcelableFragments: ViewPagerParcelableFragments? = null
        ) {
            activity.startActivity(Intent(activity, ViewPagerContainerActivity::class.java).also {
                it.putExtra(viewPagerContainerKey, parcelableFragments)
            })
        }

        fun <T : Parcelable> openActivity(
            context: Context,
            meta: ViewPagerContainerMeta<T>
        ) {
            context.startActivity(Intent(context, ViewPagerContainerActivity::class.java).also {
                it.putExtra(viewPagerContainerMetaKey, meta)
            })
        }
    }

    private lateinit var viewPagerParcelableFragments: ViewPagerParcelableFragments
    private lateinit var viewPagerMeta: ViewPagerContainerMeta<Parcelable>
    override val layoutRes: Int = R.layout.viewpager_container_activity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewPagerContainerLayout.setBackgroundColor(DynamicTheme.getInstance().get().backgroundColor)
        setSupportActionBar(dynamicToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        statusBarColor = statusBarColor
        themeBottomNavigation()

//        viewPagerParcelableFragments = intent.getParcelableExtra(viewPagerContainerKey) ?: return

        //go through intent here

        viewPagerMeta = intent.getParcelableExtra(viewPagerContainerMetaKey) ?: return
        prepareViews(viewPagerMeta)


        containerBottomNav.setOnNavigationItemSelectedListener {
            containerBottomNav.menu.forEachIndexed { index, item ->
                if (it == item) {
                    containerViewPager.setCurrentItem(index, true)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }


        containerViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                containerBottomNav.menu.iterator().forEach {
                    it.isChecked = false
                }
                containerBottomNav.menu.getItem(position).isChecked = true
            }
        })

        containerViewPager.offscreenPageLimit = viewPagerParcelableFragments.clzzes.size - 1
        containerViewPager.adapter = ViewPagerContainerAdapter()
//        containerViewPager.post {
//            ActivityCompat.startPostponedEnterTransition(this)
//        }
    }


    private fun prepareViews(viewPagerMeta: ViewPagerContainerMeta<Parcelable>) {
        when (viewPagerMeta.containerType) {
            ViewPagerContainerType.CHARACTER -> {
                supportActionBar?.title = getString(R.string.character)
                containerBottomNav.inflateMenu(R.menu.character_nav_menu)
                viewPagerParcelableFragments = ViewPagerParcelableFragments(
                    listOf(
                        CharacterFragment::class.java.name,
                        CharacterMediaFragment::class.java.name,
                        CharacterActorFragment::class.java.name
                    ),
                    listOf(
                        bundleOf(
                            CharacterFragment.CHARACTER_META_KEY to viewPagerMeta.data
                        ),
                        bundleOf(
                            CharacterFragment.CHARACTER_META_KEY to viewPagerMeta.data
                        ),
                        bundleOf(
                            CharacterFragment.CHARACTER_META_KEY to viewPagerMeta.data
                        )
                    )
                )
            }

            ViewPagerContainerType.STAFF -> {
                supportActionBar?.title = getString(R.string.staff)
                containerBottomNav.inflateMenu(R.menu.staff_nav_menu)
                viewPagerParcelableFragments = ViewPagerParcelableFragments(
                    listOf(
                        StaffFragment::class.java.name,
                        StaffMediaCharacterFragment::class.java.name,
                        StaffMediaRoleFragment::class.java.name
                    ),
                    listOf(
                        bundleOf(
                            StaffFragment.STAFF_META_KEY to viewPagerMeta.data
                        ),
                        bundleOf(
                            StaffFragment.STAFF_META_KEY to viewPagerMeta.data
                        ),
                        bundleOf(
                            StaffFragment.STAFF_META_KEY to viewPagerMeta.data
                        )
                    )
                )
            }

            ViewPagerContainerType.FAVOURITE -> {
                supportActionBar?.title = getString(R.string.favourites)
                containerBottomNav.inflateMenu(R.menu.favourite_nav_menu)
                viewPagerParcelableFragments = ViewPagerParcelableFragments(
                    listOf(
                        AnimeFavouriteFragment::class.java.name,
                        MangaFavouriteFragment::class.java.name,
                        CharacterFavouriteFragment::class.java.name,
                        StaffFavouriteFragment::class.java.name,
                        StudioFavouriteFragment::class.java.name
                    ),
                    listOf(
                        bundleOf(
                            UserFavouriteFragment.USER_FAVOURITE_PARCELABLE_KEY to viewPagerMeta.data
                        ),
                        bundleOf(
                            UserFavouriteFragment.USER_FAVOURITE_PARCELABLE_KEY to viewPagerMeta.data
                        ),
                        bundleOf(
                            UserFavouriteFragment.USER_FAVOURITE_PARCELABLE_KEY to viewPagerMeta.data
                        ),
                        bundleOf(
                            UserFavouriteFragment.USER_FAVOURITE_PARCELABLE_KEY to viewPagerMeta.data
                        ),
                        bundleOf(
                            UserFavouriteFragment.USER_FAVOURITE_PARCELABLE_KEY to viewPagerMeta.data
                        )
                    )
                )
            }
        }
    }

    private fun themeBottomNavigation() {
        containerBottomNav.color = DynamicTheme.getInstance().get().primaryColor
        containerBottomNav.textColor = DynamicTheme.getInstance().get().accentColor
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


    override fun onStop() {
        unRegisterForEvent()
        super.onStop()
    }

    inner class ViewPagerContainerAdapter :
        FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return (Class.forName(viewPagerParcelableFragments.clzzes[position]).newInstance() as BaseFragment).also {
                it.arguments = viewPagerParcelableFragments.bundles[position]
            }
        }

        override fun getCount(): Int {
            return viewPagerParcelableFragments.clzzes.size
        }
    }


}