package com.revolgenx.anilib.ui.view.airing

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.pranavpandey.android.dynamic.support.widget.DynamicLinearLayout
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.model.AiringTimeModel
import com.revolgenx.anilib.util.CommonTimer
import com.revolgenx.anilib.util.TimerCallback
import com.revolgenx.anilib.ui.view.header.CustomDynamicHeader

class AiringEpisodeView(context: Context, private val attributeSet: AttributeSet?, attstyle: Int) :
    DynamicLinearLayout(context, attributeSet, attstyle), TimerCallback {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {
        orientation = LinearLayout.HORIZONTAL
        addView(episodeTv)
        addView(daysHeader)
        addView(hourHeader)
        addView(minHeader)
        addView(secHeader)
    }

    override fun invoke() {
        updateViews()
    }

    private var commonTimer: CommonTimer? = null
        set(value) {
            value?.removeCallback()
            value?.timerCallback = this
            field = value
        }

    private var episode: Int? = null
        set(value) {
            field = value
            value?.let {
                episodeTv.text = context.getString(R.string.ep_s).format(it)
            } ?: let {
                visibility = View.GONE
            }
        }


    private val episodeTv by lazy {
        makeDynamicTextView(context, attributeSet).also {
            it.text = context.getString(R.string.ep_s)
        }
    }

    private val daysHeader by lazy {
        makeDynamicHeaderLayout(context, attributeSet).also {
            it.title = context.getString(R.string.day)
            it.subtitle = 0.toString()
        }
    }

    private val hourHeader by lazy {
        makeDynamicHeaderLayout(context, attributeSet).also {
            it.title = context.getString(R.string.hour)
            it.subtitle = 0.toString()
        }
    }

    private val minHeader by lazy {
        makeDynamicHeaderLayout(context, attributeSet).also {
            it.title = context.getString(R.string.min)
            it.subtitle = 0.toString()
        }
    }

    private val secHeader by lazy {
        makeDynamicHeaderLayout(context, attributeSet).also {
            it.title = context.getString(R.string.sec)
            it.subtitle = 0.toString()
        }
    }

    private fun updateViews() {
        commonTimer?.airingTime?.apply {
            daysHeader.subtitle = day.toString()
            hourHeader.subtitle = hour.toString()
            minHeader.subtitle = min.toString()
            secHeader.subtitle = sec.toString()
        }
    }


    private fun makeDynamicTextView(context: Context, attributeSet: AttributeSet?) =
        DynamicTextView(context, attributeSet).also { tv ->
            tv.layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also { params ->
                    params.weight = 1f
                    params.gravity = Gravity.CENTER
                }
            tv.textSize = 14f
            tv.gravity = View.TEXT_ALIGNMENT_CENTER
            tv.colorType = Theme.ColorType.TINT_ACCENT
        }


    private fun makeDynamicHeaderLayout(context: Context, attributeSet: AttributeSet?) =
        CustomDynamicHeader(
            context,
            attributeSet
        ).also { header ->
            header.layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also { params ->
                    params.weight = 1f
                    params.gravity = Gravity.CENTER
                }
            header.isFillSpace = true
        }

    fun setTimer(airingTimeModel: AiringTimeModel) {
        airingTimeModel.let {
            commonTimer = it.commonTimer
            episode = it.episode
        }
    }

}