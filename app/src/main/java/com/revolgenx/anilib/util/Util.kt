package com.revolgenx.anilib.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.snackbar.Snackbar
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.type.MediaSeason
import org.greenrobot.eventbus.EventBus

fun AppCompatActivity.makePagerAdapter(fragments: List<BaseFragment>) =
    object : FragmentPagerAdapter(
        this@makePagerAdapter.supportFragmentManager,
        FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
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


//view
fun TextView.naText(text: String?) {
    this.text = text?.takeIf { it.isNotEmpty() } ?: "NA"
}

fun String?.naText() = this.takeIf { it != null && it.isNotEmpty() } ?: "NA"

fun View.makeSnakeBar(msg: String) {
    Snackbar.make(this, msg, Snackbar.LENGTH_SHORT).show()
}

fun Fragment.makeToast(msg: String, @DrawableRes icon: Int? = null) {
    context?.makeToast(msg, icon)
}

fun Context.makeToast(msg: String,@DrawableRes  icon: Int? = null) {
    if(icon != null){
        val drawable = ContextCompat.getDrawable(this, icon)
        DynamicToast.make(this, msg, drawable).show()
    }else{
        DynamicToast.make(this, msg).show()
    }
}

fun View.string(@StringRes id: Int) = context.getString(id)



