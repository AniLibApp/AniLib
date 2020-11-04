package com.revolgenx.anilib.ui.view.drawable

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.Gravity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.revolgenx.anilib.R
import com.revolgenx.anilib.util.dp

class YoutubePlayBitmapDrawable(context: Context) : BitmapDrawable(
    context.resources, ContextCompat.getDrawable(context, R.drawable.ic_youtube)?.toBitmap(
        dp(56f), dp(56f)
    )
) {
    init {
        gravity = Gravity.CENTER
    }
}