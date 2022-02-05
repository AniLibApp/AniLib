package com.revolgenx.anilib.common.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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

fun AppCompatActivity.makeViewPagerAdapter2(fragments: List<BaseFragment>) =
    object : FragmentStateAdapter(this) {
        override fun getItemCount(): Int = fragments.count()
        override fun createFragment(position: Int): Fragment = fragments[position]
    }

fun Fragment.setupWithViewPager2(
    tabLayout: TabLayout,
    viewpager2: ViewPager2,
    titles: Array<String>
) = TabLayoutMediator(tabLayout, viewpager2) { tab, position ->
    tab.text = titles[position]
}.also { mediator ->
    mediator.attach()
    viewLifecycleOwnerLiveData.observe(viewLifecycleOwner) { owner ->
        when (owner.lifecycle.currentState) {
            Lifecycle.State.DESTROYED -> {
                mediator.detach()
            }
            else -> {}
        }
    }
}

fun Fragment.setupWithViewPager2(
    tabLayout: TabLayout,
    viewpager2: ViewPager2,
    tabMeta: List<Pair<Int, Int>>? = null
) = TabLayoutMediator(tabLayout, viewpager2) { tab, position ->
    val item = tabMeta?.get(position) ?: return@TabLayoutMediator
    tab.setText(item.first)
    tab.setIcon(item.second)
}.also { mediator ->
    mediator.attach()
    val lifecycleObserver = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            mediator.detach()
            viewLifecycleOwner.lifecycle.removeObserver(this)
        }

    }
    viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)
}

fun TabLayout.setupWithViewPager2(
    viewpager2: ViewPager2,
    titles: Array<String>
) = TabLayoutMediator(this, viewpager2) { tab, position ->
    tab.text = titles[position]
}.also { it.attach() }


fun Fragment.registerOnPageChangeCallback(
    viewpager2: ViewPager2,
    callback: ViewPager2.OnPageChangeCallback
) {
    viewpager2.registerOnPageChangeCallback(callback)

    val lifecycleObserver = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            viewpager2.unregisterOnPageChangeCallback(callback)
            viewLifecycleOwner.lifecycle.removeObserver(this)
        }

    }
    viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)
}