package com.revolgenx.anilib.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.pranavpandey.android.dynamic.support.popup.DynamicArrayPopup
import com.pranavpandey.android.dynamic.support.popup.DynamicPopup
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.toasts.internal.ToastCompat
import com.revolgenx.anilib.R
import com.revolgenx.anilib.databinding.DynamicToastViewLayoutBinding

private val tintSurface by lazy {
    DynamicTheme.getInstance().get().tintSurfaceColor
}

fun Fragment.makeToast(
    @StringRes str: Int? = null,
    msg: String? = null,
    @DrawableRes icon: Int? = null
) {
    requireContext().makeToast(str, msg, icon)
}

fun Context.makeToast(
    @StringRes str: Int? = null,
    msg: String? = null,
    @DrawableRes icon: Int? = null
) {
    makeDynamicToastView(this, str?.let { getString(it) } ?: msg, icon)
}


fun makeDynamicToastView(
    context: Context,
    msg: String? = null,
    @DrawableRes drawable: Int? = null
) {
    val toastBinding = DynamicToastViewLayoutBinding.inflate(
        LayoutInflater.from(context), LinearLayout(
            context
        ), false
    )

    drawable?.let {
        toastBinding.toastIconIv.visibility = View.VISIBLE
        toastBinding.toastIconIv.setImageResource(drawable)
        toastBinding.toastIconIv.setColorFilter(tintSurface)
    }
    toastBinding.toastIconTv.text = msg

    ToastCompat(context, Toast(context)).apply {
        view = toastBinding.root
        duration = Toast.LENGTH_SHORT
        show()
    }

}

@SuppressLint("RestrictedApi")
fun makePopupMenu(
    @MenuRes res: Int,
    view: View,
    gravity: Int = Gravity.NO_GRAVITY,
    listener: PopupMenu.OnMenuItemClickListener
): PopupMenu {
    return PopupMenu(view.context, view, gravity).apply {
        inflate(res)
        if (menu is MenuBuilder) {
            (menu as MenuBuilder).setOptionalIconsVisible(true)
        }
        setOnMenuItemClickListener(listener)
    }
}

fun makeArrayPopupMenu(
    anchor: View,
    entries: Array<String>,
    icons: IntArray? = null,
    selectedPosition: Int = -1,
    viewType: Int = DynamicPopup.Type.LIST,
    onItemClickListener: AdapterView.OnItemClickListener
): DynamicArrayPopup {
    return DynamicArrayPopup(
        anchor,
        entries,
        icons,
        onItemClickListener
    ).also { popup ->
        popup.selectedPosition = selectedPosition
        popup.viewType = viewType
        popup.build().show()
    }
}

fun makeSpinnerAdapter(context: Context, items: List<DynamicSpinnerItem>) =
    DynamicSpinnerImageAdapter(
        context,
        R.layout.ads_layout_spinner_item,
        R.id.ads_spinner_item_icon,
        R.id.ads_spinner_item_text, items
    )
