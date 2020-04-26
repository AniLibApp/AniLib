package com.revolgenx.anilib.view.drawable

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.view.Gravity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.util.dp

//todo:// use single bitmap
class VideoPlayBitmapDrawable(context: Context) : LayerDrawable(arrayOf(
    BitmapDrawable(
        context.resources,
        ColorDrawable(Color.BLACK).toBitmap(300, 100, Bitmap.Config.ARGB_8888)
    ), BitmapDrawable(
        context.resources, ContextCompat.getDrawable(context, R.drawable.ic_play_cirle)
            ?.toBitmap(dp(56f), dp(56f))
    ).apply {
        setTint(DynamicTheme.getInstance().get().accentColor)
        gravity = Gravity.CENTER
    })
)