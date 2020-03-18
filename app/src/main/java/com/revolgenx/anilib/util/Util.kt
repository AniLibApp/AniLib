package com.revolgenx.anilib.util

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.snackbar.Snackbar
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.type.MediaSeason
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.greenrobot.eventbus.EventBus


const val COLLAPSED = 0
const val EXPANDED = 1


fun AppCompatActivity.makePagerAdapter(fragments: List<BaseFragment>) =
    object : FragmentPagerAdapter(
        this@makePagerAdapter.supportFragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
        override fun getItem(position: Int) = fragments[position]
        override fun getCount(): Int = fragments.size
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

fun getAverageScore(avg: Int?) = avg?.div(10f)?.let {
    String.format("%.1f", it)
} ?: "?"


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

suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> = coroutineScope {
    map { async { f(it) } }.awaitAll()
}


//view
fun dp(dp: Float) = DynamicUnitUtils.convertDpToPixels(dp)

fun TextView.naText(text: String?) {
    this.text = text?.takeIf { it.isNotEmpty() } ?: "?"
}

fun naText(na: String?) = na.takeIf { it != null && it.isNotEmpty() } ?: "?"

fun View.makeSnakeBar(@StringRes str: Int? = null, msg: String? = null) {
    Snackbar.make(this, str?.let { context.getString(str) } ?: msg ?: "", Snackbar.LENGTH_SHORT)
        .show()
}

fun Fragment.makeToast(@StringRes str: Int? = null, msg: String? = null, @DrawableRes icon: Int? = null) {
    context?.makeToast(str, msg, icon)
}

fun Context.makeToast(@StringRes str: Int? = null, msg: String? = null, @DrawableRes icon: Int? = null) {
    if (icon != null) {
        val drawable = ContextCompat.getDrawable(this, icon)
        DynamicToast.make(this, str?.let { getString(it) } ?: msg, drawable).show()
    } else {
        DynamicToast.make(this, str?.let { getString(it) } ?: msg).show()
    }
}

fun Context.openLink(url: String?) {
    try {
        if (url != null)
            startActivity(Intent(Intent.ACTION_VIEW, url.trim().toUri()))
    } catch (e: Exception) {
        makeToast(R.string.no_app_found_to_open)
    }
}

fun View.string(@StringRes id: Int) = context.getString(id)



