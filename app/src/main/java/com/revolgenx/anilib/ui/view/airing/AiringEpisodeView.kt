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
import com.revolgenx.anilib.data.model.airing.AiringTimeModel
import com.revolgenx.anilib.util.CommonTimer
import com.revolgenx.anilib.util.TimerCallback
import com.revolgenx.anilib.ui.view.header.CustomDynamicHeader
import com.revolgenx.anilib.util.dp

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
            it.title = 0.toString()
            it.subtitle = context.getString(R.string.day)
        }
    }

    private val hourHeader by lazy {
        makeDynamicHeaderLayout(context, attributeSet).also {
            it.title = 0.toString()
            it.subtitle = context.getString(R.string.hour)
        }
    }

    private val minHeader by lazy {
        makeDynamicHeaderLayout(context, attributeSet).also {
            it.title = 0.toString()
            it.subtitle = context.getString(R.string.min)
        }
    }

    private val secHeader by lazy {
        makeDynamicHeaderLayout(context, attributeSet).also {
            it.title = 0.toString()
            it.subtitle = context.getString(R.string.sec)
        }
    }

    private fun updateViews() {
        commonTimer?.timeUntilAiringModel?.apply {
            daysHeader.title = day.toString()
            hourHeader.title = hour.toString()
            minHeader.title = min.toString()
            secHeader.title = sec.toString()
        }
    }


    private fun makeDynamicTextView(context: Context, attributeSet: AttributeSet?) =
        DynamicTextView(context, attributeSet).also { tv ->
            tv.layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also { params ->
                    params.gravity = Gravity.CENTER
                    params.marginEnd = dp(6f)
                }
            tv.textSize = 14f
            tv.isAllCaps = true
            tv.gravity = View.TEXT_ALIGNMENT_CENTER
            tv.colorType = Theme.ColorType.ACCENT
            tv.contrastWithColorType = Theme.ColorType.BACKGROUND
        }


    private fun makeDynamicHeaderLayout(context: Context, attributeSet: AttributeSet?) =
        CustomDynamicHeader(
            context,
            attributeSet
        ).also { header ->
            header.layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also { params ->
                    params.gravity = Gravity.CENTER
                }
            (header.titleView as DynamicTextView).isRtlSupport = false
            header.titleView.textAlignment = TEXT_ALIGNMENT_CENTER
            header.titleView.textSize = 12f
            header.subtitleView.textSize = 12f
            header.isFillSpace = true
        }

    fun setTimer(airingTimeModel: AiringTimeModel) {
        airingTimeModel.let {
            commonTimer = it.commonTimer
            episode = it.episode
        }
    }

}