package com.revolgenx.anilib.common.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.dp
import timber.log.Timber

class KaomojiLoader : FrameLayout {
    private lateinit var kaomojiTextView: DynamicTextView

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        initLoader()
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        initLoader()
    }

    private fun initLoader() {
        kaomojiTextView = DynamicTextView(context).also {
            it.layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, dp(100f)).also {
                    it.gravity = Gravity.CENTER
                }
            it.text = loadingKoeMoji.random()
            it.textSize = 18f
            it.gravity = Gravity.CENTER
            it.includeFontPadding = false
        }
        kaomojiTextView.colorType = Theme.ColorType.ACCENT
        kaomojiTextView.typeface = ResourcesCompat.getFont(context, R.font.rubik_bold)
        kaomojiTextView.rotate()
        addView(kaomojiTextView)
    }

    private fun TextView.rotate() {
        val rotateAnimation = RotateAnimation(
            0f, 359f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.repeatCount = Animation.INFINITE
        rotateAnimation.duration = 1600
        rotateAnimation.interpolator = LinearInterpolator()
        startAnimation(rotateAnimation)
    }
}