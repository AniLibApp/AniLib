package com.revolgenx.anilib.ui.view.score

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.view.setPadding
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.support.widget.WidgetDefaults
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.util.dp
import com.revolgenx.anilib.util.naText
import java.lang.Exception

class MediaScoreBadge : LinearLayout {

    private var scoreImage: DynamicImageView
    private var scoreTv: DynamicTextView
    private var isListScore = false


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {

        val a = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.MediaScoreBadge,
            defStyle,
            0
        )

        try {
            isListScore = a.getBoolean(R.styleable.MediaScoreBadge_isListScore, false);
        } catch (ex: Exception) {

        } finally {
            a.recycle()
        }

        orientation = HORIZONTAL
        setBackgroundResource(R.drawable.oval_background)

        setPadding(dp(5f), dp(3f), dp(5f), dp(3f))

        scoreImage = DynamicImageView(context, attributeSet, defStyle).also {
            it.layoutParams = LayoutParams(dp(12f), dp(12f)).also { params ->
                params.gravity = Gravity.CENTER
                params.marginEnd = dp(2f)
            }
            it.contrastWithColor = WidgetDefaults.ADS_COLOR_UNKNOWN
            it.color = Color.WHITE
            it.setImageResource(R.drawable.ic_star)
        }

        scoreTv = DynamicTextView(context, attributeSet, defStyle).also {
            it.layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also { params ->
                    params.gravity = Gravity.CENTER_VERTICAL
                }
            it.includeFontPadding = false
            it.setSingleLine()
            it.contrastWithColor = WidgetDefaults.ADS_COLOR_UNKNOWN
            it.color = Color.WHITE
            it.gravity = Gravity.CENTER_VERTICAL
            it.textSize = 10f
        }

        addView(scoreImage)
        addView(scoreTv)
    }


    var scoreTextVisibility:Int = View.VISIBLE
        set(value) {
            field = value
            scoreTv.visibility = visibility
            (scoreImage.layoutParams as LayoutParams).marginEnd = 0
        }


    var text: Int? = null
        set(value) {
            field = value
            if (!isListScore) {
                val drawable = when {
                    value == null -> {
                        R.drawable.ic_star
                    }
                    value >= 75 -> {
                        R.drawable.ic_score_smile
                    }
                    value > 60 -> {
                        R.drawable.ic_score_neutral
                    }
                    else -> {
                        R.drawable.ic_score_sad
                    }
                }
                scoreImage.setImageResource(drawable)
            }

            scoreTv.text = value?.toString()?.let {
                if(isListScore){
                    it
                }else{
                    it.plus("%")
                }
            }.naText()
        }

    fun setText(score:Double?){
        if (!isListScore) {
            val drawable = when {
                score == null -> {
                    R.drawable.ic_star
                }
                score >= 75 -> {
                    R.drawable.ic_score_smile
                }
                score > 60 -> {
                    R.drawable.ic_score_neutral
                }
                else -> {
                    R.drawable.ic_score_sad
                }
            }
            scoreImage.setImageResource(drawable)
        }
        scoreTv.text = score?.toString().naText()
    }


    fun setImageResource(@DrawableRes imageRes:Int){
        scoreImage.setImageResource(imageRes)
    }

}