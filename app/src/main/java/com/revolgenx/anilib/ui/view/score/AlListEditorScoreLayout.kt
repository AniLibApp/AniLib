package com.revolgenx.anilib.ui.view.score

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.widget.FrameLayout
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.ui.view.MediaSmileyScoreLayout
import com.revolgenx.anilib.ui.view.widgets.AlCountEditTextLayout

class AlListEditorScoreLayout : FrameLayout {
    var onScoreChangeListener: ((score:Double)->Unit)? = null
    var scoreFormatType = ScoreFormat.POINT_100
        set(value) {
            field = value
            when (value) {
                ScoreFormat.POINT_100 -> {
                    alSmileyScoreLayout.visibility = GONE
                    alCountEditTextLayout.inputType = InputType.TYPE_CLASS_NUMBER
                    alCountEditTextLayout.max = 100
                }
                ScoreFormat.POINT_10_DECIMAL -> {
                    alSmileyScoreLayout.visibility = GONE
                    alCountEditTextLayout.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
                    alCountEditTextLayout.max = 10
                }
                ScoreFormat.POINT_10 -> {
                    alSmileyScoreLayout.visibility = GONE
                    alCountEditTextLayout.inputType = InputType.TYPE_CLASS_NUMBER
                    alCountEditTextLayout.max = 10
                }
                ScoreFormat.POINT_5 -> {
                    alSmileyScoreLayout.visibility = GONE
                    alCountEditTextLayout.inputType = InputType.TYPE_CLASS_NUMBER
                    alCountEditTextLayout.max = 5
                }
                ScoreFormat.POINT_3->{
                    alCountEditTextLayout.visibility = GONE
                    alSmileyScoreLayout.visibility = VISIBLE
                }
                else -> {}
            }
        }

    var mediaListScore = 0.0

    private val alCountEditTextLayout: AlCountEditTextLayout
    private val alSmileyScoreLayout: MediaSmileyScoreLayout

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        alCountEditTextLayout = AlCountEditTextLayout(context, attributeSet)
        alSmileyScoreLayout = MediaSmileyScoreLayout(context, attributeSet)
        addView(alCountEditTextLayout)
        addView(alSmileyScoreLayout)
        initListener()
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        alCountEditTextLayout = AlCountEditTextLayout(context, attributeSet, defStyle)
        alSmileyScoreLayout = MediaSmileyScoreLayout(context, attributeSet, defStyle)
        addView(alCountEditTextLayout)
        addView(alSmileyScoreLayout)
        initListener()
    }

    private fun initListener(){
        alCountEditTextLayout.onCountChangeListener = {
            onScoreChangeListener?.invoke(it)
        }

        alSmileyScoreLayout.onSmileyScoreChange {
            onScoreChangeListener?.invoke(it.toDouble())
        }
    }

    fun updateScore(score: Double) {
        mediaListScore = score
        when (scoreFormatType) {
            ScoreFormat.POINT_3 -> {
                alSmileyScoreLayout.smileyScore = score.toInt()
            }
            else -> {
                alCountEditTextLayout.count = score
                alCountEditTextLayout.updateView()
            }
        }
    }

}