package com.revolgenx.anilib.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import com.revolgenx.anilib.fragment.base.BasePagerFragment
import com.revolgenx.anilib.type.MediaSeason
import org.greenrobot.eventbus.EventBus

fun AppCompatActivity.makePagerAdapter(fragments: List<BasePagerFragment>) =
    object : FragmentPagerAdapter(
        this@makePagerAdapter.supportFragmentManager,
        FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
        override fun getItem(position: Int) = fragments[position]
        override fun getCount(): Int = fragments.size
        override fun getPageTitle(position: Int): CharSequence? =
            fragments[position].title(this@makePagerAdapter)
    }

fun getSeasonFromMonth(monthOfYear: Int): MediaSeason {
    monthOfYear.let {
        return if (it == 12 || it == 1 || it == 2) {
            MediaSeason.WINTER
        } else if (it == 3 || it == 4 || it == 5) {
            MediaSeason.SPRING
        } else if (it == 6 || it == 7 || it == 8) {
            MediaSeason.SUMMER
        } else
            MediaSeason.FALL
    }
}


fun <T> T.registerForEvent() {
    val bus = EventBus.getDefault()
    if (!bus.isRegistered(this))
        bus.register(this)
}

fun <T> T.unRegisterForEvent() {
    val bus = EventBus.getDefault()
    if (bus.isRegistered(this)) {
        bus.unregister(this)
    }
}



