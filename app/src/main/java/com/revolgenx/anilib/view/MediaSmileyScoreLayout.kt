package com.revolgenx.anilib.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView
import com.pranavpandey.android.dynamic.support.widget.DynamicLinearLayout
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils
import com.revolgenx.anilib.R

class MediaSmileyScoreLayout(context: Context, attributeSet: AttributeSet?, style: Int) :
    DynamicLinearLayout(context, attributeSet, style) {

    val accentColor by lazy {
        DynamicTheme.getInstance().get().accentColor
    }
    val tintPrimary by lazy {
        DynamicTheme.getInstance().get().tintPrimaryColor
    }

    var smileyScore: Int = 0
        set(value) {
            field = value
            updateIcon()
        }

    val smileySadImageButton by lazy {
        DynamicImageView(context, attributeSet).apply {
            layoutParams = LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT).also {
                it.weight = 1f
                it.gravity = Gravity.CENTER
            }
            this.setPadding(DynamicUnitUtils.convertDpToPixels(10f))
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_score_sad))
        }
    }


    val smileyNeutralImageButton by lazy {
        DynamicImageView(context, attributeSet).apply {
            layoutParams = LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT).also {
                it.weight = 1f
                it.gravity = Gravity.CENTER
            }
            this.setPadding(DynamicUnitUtils.convertDpToPixels(10f))
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_score_neutral))
        }
    }


    val smileySmileImageButton by lazy {
        DynamicImageView(context, attributeSet).apply {
            layoutParams = LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT).also {
                it.weight = 1f
                it.gravity = Gravity.CENTER
            }
            this.setPadding(DynamicUnitUtils.convertDpToPixels(10f))
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_score_smile))
        }
    }


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {
        addView(smileySadImageButton)
        addView(smileyNeutralImageButton)
        addView(smileySmileImageButton)

        smileySmileImageButton.setOnClickListener {
            smileyScore = 3
        }

        smileyNeutralImageButton.setOnClickListener {
            smileyScore = 2
        }

        smileySadImageButton.setOnClickListener {
            smileyScore = 1
        }

        updateIcon()
    }


    private fun updateIcon() {
        neutralizeSmile()
        when (smileyScore) {
            1 -> {
                smileySadImageButton.color = accentColor
            }
            2 -> {
                smileyNeutralImageButton.color = accentColor
            }
            3 -> {
                smileySmileImageButton.color = accentColor
            }
        }
    }

    private fun neutralizeSmile() {
        smileySadImageButton.color = tintPrimary
        smileyNeutralImageButton.color = tintPrimary
        smileySmileImageButton.color = tintPrimary
    }

}

