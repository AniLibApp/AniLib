package com.revolgenx.anilib.view

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import com.pranavpandey.android.dynamic.support.widget.DynamicFrameLayout
import com.revolgenx.anilib.type.ScoreFormat

class MediaListEditorScoreLayout(context: Context, attributeSet: AttributeSet?, style: Int) :
    DynamicFrameLayout(context, attributeSet, style) {
    constructor(context: Context) : this(context, null)

    var scoreFormatType: Int = ScoreFormat.POINT_100.ordinal
        set(value) {
            field = value
            updateView()
        }

    var mediaListScore: Double = 0.0
        get() {
            return when (scoreFormatType) {
                ScoreFormat.POINT_3.ordinal -> {
                    smileyScoreLayout.smileyScore.toDouble()
                }
                else -> {
                    plusMinusScoreLayout.counterHolder
                }
            }
        }
        set(value) {
            field = value
            when (scoreFormatType) {
                ScoreFormat.POINT_3.ordinal -> {
                    smileyScoreLayout.smileyScore = field.toInt()
                }
                else -> {
                    plusMinusScoreLayout.counterHolder = field
                }
            }
        }

    private val smileyScoreLayout by lazy {
        MediaSmileyScoreLayout(context)
    }

    private val plusMinusScoreLayout by lazy {
        PlusMinusEditTextLayout(context)
    }

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {
        addView(smileyScoreLayout)
        addView(plusMinusScoreLayout)
        smileyScoreLayout.visibility = View.GONE
        updateView()
    }

    fun updateView() {
        when (scoreFormatType) {
            ScoreFormat.POINT_3.ordinal -> {
                plusMinusScoreLayout.visibility = View.GONE
                smileyScoreLayout.visibility = View.VISIBLE
            }
            ScoreFormat.POINT_5.ordinal -> {
                plusMinusScoreLayout.max = 5.0
            }
            ScoreFormat.POINT_10.ordinal -> {
                plusMinusScoreLayout.max = 10.0
            }
            ScoreFormat.POINT_10_DECIMAL.ordinal -> {
                plusMinusScoreLayout.max = 10.0
                plusMinusScoreLayout.dynamicInputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
                plusMinusScoreLayout.setDynamicText()
            }
            ScoreFormat.POINT_100.ordinal -> {
                plusMinusScoreLayout.max = 100.0
            }
        }
    }


}