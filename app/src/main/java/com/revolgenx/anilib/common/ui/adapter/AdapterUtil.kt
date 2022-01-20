package com.revolgenx.anilib.common.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseFragment

fun AppCompatActivity.makePagerAdapter(fragments: List<BaseFragment>) =
    object : FragmentPagerAdapter(
        this@makePagerAdapter.supportFragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
        override fun getItem(position: Int) = fragments[position]
        override fun getCount(): Int = fragments.size
    }


fun Fragment.makePagerAdapter(fragments: List<BaseFragment>, titles: Array<String>? = null) =
    object : FragmentPagerAdapter(
        this@makePagerAdapter.childFragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
        override fun getItem(position: Int) = fragments[position]
        override fun getCount(): Int = fragments.size
        override fun getPageTitle(position: Int): CharSequence? = titles?.get(position)
    }

fun Fragment.makeViewPagerAdapter2(fragments: List<BaseFragment>) =
    object : FragmentStateAdapter(this) {
        override fun getItemCount(): Int = fragments.count()
        override fun createFragment(position: Int): Fragment = fragments[position]
    }

