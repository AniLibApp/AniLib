package com.revolgenx.anilib.ui.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
import android.graphics.Rect
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.badge.BadgeDrawable
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.pranavpandey.android.dynamic.support.popup.DynamicMenuPopup
import com.pranavpandey.android.dynamic.support.popup.DynamicPopup
import com.pranavpandey.android.dynamic.toasts.internal.ToastCompat
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary
import com.revolgenx.anilib.databinding.DynamicToastViewLayoutBinding
import com.revolgenx.anilib.ui.dialog.MessageDialog

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
        toastBinding.toastIconIv.setColorFilter(dynamicTextColorPrimary)
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
): DynamicMenuPopup {
    return DynamicMenuPopup(
        anchor,
        icons,
        entries,
        onItemClickListener
    ).also { popup ->
        popup.selectedPosition = selectedPosition
        popup.viewType = viewType
        popup.build().show()
    }
}

fun makeSpinnerAdapter(context: Context, items: List<DynamicMenu>) =
    DynamicSpinnerImageAdapter(
        context,
        R.layout.al_spinner_item_layout,
        R.id.al_spinner_item_icon,
        R.id.al_spinner_item_text, items
    )


fun makeSpinnerAdapter(context: Context, @ArrayRes itemsRes:Int)=
    makeSpinnerAdapter(context, context.resources.getStringArray(itemsRes).map{DynamicMenu(null, it)})



inline fun is29(func: () -> Unit) {
    if (DynamicSdkUtils.is29()) {
        func()
    }
}

inline fun isGestureNavigationEnabled(contentResolver: ContentResolver, func: () -> Unit) {
    if (Settings.Secure.getInt(contentResolver, "navigation_mode", 0) == 2) {
        func()
    }
}


fun makeConfirmationDialog(
    ctx: Context,
    titleRes: Int = R.string.confirmation,
    messageRes: Int = R.string.are_you_sure,
    message: String? = null,
    positiveRes: Int = R.string.yes,
    negativeRes: Int = R.string.no,
    onConfirmListener: () -> Unit
) {
    MessageDialog.Companion.Builder().let { it ->
        it.titleRes = titleRes
        it.messageRes = messageRes
        it.message = message
        it.positiveTextRes = positiveRes
        it.negativeTextRes = negativeRes
        it.build().let {
            it.onButtonClickedListener = { _, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    onConfirmListener.invoke()
                }
            }
            when (ctx) {
                is FragmentActivity -> it.show(
                    ctx.supportFragmentManager,
                    "ConfirmationDialogTag"
                )
                is AppCompatActivity -> it.show(
                    ctx.supportFragmentManager,
                    "ConfirmationDialogTag"
                )
                is Fragment -> it.show(ctx.childFragmentManager, "ConfirmationDialogTag")
            }
        }
    }
}


fun BadgeDrawable.setBoundsFor(anchor: View, parent: FrameLayout){
    val rect = Rect()
    parent.getDrawingRect(rect)
    this.bounds = rect
    this.updateBadgeCoordinates(anchor, parent)
}