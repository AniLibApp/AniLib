package com.revolgenx.anilib.ui.view.score

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.widget.FrameLayout
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.ui.view.ListEntrySmileyScoreLayout
import com.revolgenx.anilib.ui.view.widgets.AlCardView
import com.revolgenx.anilib.ui.view.widgets.AlCountEditTextLayout

class AlListEditorScoreLayout : FrameLayout {
    var onScoreChangeListener: ((score: Double) -> Unit)? = null
    var scoreFormatType = ScoreFormat.POINT_100
        set(value) {
            field = value
            when (value) {
                ScoreFormat.POINT_100 -> {
                    smileyContainer.visibility = GONE
                    alCountEditTextLayout.inputType = InputType.TYPE_CLASS_NUMBER
                    alCountEditTextLayout.max = 100
                }
                ScoreFormat.POINT_10_DECIMAL -> {
                    smileyContainer.visibility = GONE
                    alCountEditTextLayout.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
                    alCountEditTextLayout.max = 10
                }
                ScoreFormat.POINT_10 -> {
                    smileyContainer.visibility = GONE
                    alCountEditTextLayout.inputType = InputType.TYPE_CLASS_NUMBER
                    alCountEditTextLayout.max = 10
                }
                ScoreFormat.POINT_5 -> {
                    smileyContainer.visibility = GONE
                    alCountEditTextLayout.inputType = InputType.TYPE_CLASS_NUMBER
                    alCountEditTextLayout.max = 5
                }
                ScoreFormat.POINT_3 -> {
                    alCountEditTextLayout.visibility = GONE
                    smileyContainer.visibility = VISIBLE
                }
                else -> {}
            }
        }

    private lateinit var alCountEditTextLayout: AlCountEditTextLayout
    private lateinit var alSmileyScoreLayout: ListEntrySmileyScoreLayout
    private lateinit var smileyContainer: AlCardView

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        initView()
    }

    private fun initView() {
        alCountEditTextLayout = AlCountEditTextLayout(context)
        alSmileyScoreLayout = ListEntrySmileyScoreLayout(context)
        smileyContainer = AlCardView(context)
        smileyContainer.addView(alSmileyScoreLayout)
        addView(alCountEditTextLayout)
        addView(smileyContainer)
        initListener()
    }

    private fun initListener() {
        alCountEditTextLayout.onCountChangeListener = {
            onScoreChangeListener?.invoke(it)
        }

        alSmileyScoreLayout.onSmileyScoreChange {
            onScoreChangeListener?.invoke(it.toDouble())
        }
    }

    fun updateScore(score: Double) {
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