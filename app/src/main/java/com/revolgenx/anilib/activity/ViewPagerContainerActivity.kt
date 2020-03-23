package com.revolgenx.anilib.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.forEachIndexed
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.AppController
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.fragment.base.ViewPagerParcelableFragments
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.viewpager_container_activity.*
import java.util.*

class ViewPagerContainerActivity : DynamicSystemActivity() {

    companion object {
        const val viewPagerContainerKey = "viewpager_activity_container_key"
        fun openActivity(
            activity: Context,
            parcelableFragments: ViewPagerParcelableFragments,
            option: ActivityOptionsCompat? = null
        ) {
            activity.startActivity(Intent(activity, ViewPagerContainerActivity::class.java).also {
                it.putExtra(viewPagerContainerKey, parcelableFragments)
            }, option?.toBundle())
        }
    }

    private lateinit var viewPagerParcelableFragments: ViewPagerParcelableFragments

    override fun getLocale(): Locale? {
        return null
    }


    override fun getThemeRes(): Int {
        return ThemeController.appStyle
    }

    override fun onCustomiseTheme() {
        ThemeController.setLocalTheme()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewpager_container_activity)
        viewPagerContainerLayout.setBackgroundColor(DynamicTheme.getInstance().get().backgroundColor)
        setSupportActionBar(dynamicToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        statusBarColor = statusBarColor
        themeBottomNavigation()

        viewPagerParcelableFragments =
            intent.getParcelableExtra(viewPagerContainerKey) ?: return


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

    override fun setStatusBarColor(color: Int) {
        super.setStatusBarColor(color)
        setWindowStatusBarColor(statusBarColor);
    }

    override fun setNavigationBarTheme(): Boolean {
        return AppController.instance.isThemeNavigationBar
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