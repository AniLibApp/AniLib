package com.revolgenx.anilib.radio.ui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.github.abdularis.buttonprogress.DownloadButtonProgress
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils
import com.revolgenx.anilib.R

class PausePlayButtonView : DownloadButtonProgress {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {

        Color.TRANSPARENT.also {
            finishBgColor = it
            idleBgColor = it
            indeterminateBgColor = it
            determinateBgColor = it
        }

        finishIcon = getDrawable(context, R.drawable.ic_stop_round_filled)
        idleIcon = getDrawable(context, R.drawable.ic_play_round_filled)

        progressIndeterminateColor = tintSurfaceColor
        isCancelable = false
        setIdle()


        /*
                app:finishBackgroundColor="@android:color/transparent"
                app:finishIconDrawable="@drawable/ic_stop_round_filled"
                app:finishIconHeight="32dp"
                app:finishIconWidth="32dp"
                app:idleBackgroundColor="@android:color/transparent"
                app:idleIconDrawable="@drawable/ic_play_round_filled"
                app:idleIconHeight="32dp"
                app:idleIconWidth="32dp"
                app:indeterminateBackgroundColor="@android:color/transparent"
                app:indeterminateBackgroundDrawable="@null"
                app:determinateBackgroundColor="@android:color/transparent"
                app:state="INDETERMINATE"
                app:cancelable="false"*/

    }

    private fun getDrawable(context: Context, @DrawableRes drawableRes: Int) =
        ContextCompat.getDrawable(context, drawableRes).let {
            DynamicDrawableUtils.colorizeDrawable(it, tintSurfaceColor)
        }

    private val tintSurfaceColor = DynamicTheme.getInstance().get().tintSurfaceColor

}