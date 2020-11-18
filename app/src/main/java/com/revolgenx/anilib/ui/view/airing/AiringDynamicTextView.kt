package com.revolgenx.anilib.ui.view.airing

import android.content.Context
import android.util.AttributeSet
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.model.AiringTimeModel
import com.revolgenx.anilib.util.TimerCallback
import com.revolgenx.anilib.util.naText

class AiringDynamicTextView : DynamicTextView, TimerCallback {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(
        context,
        attributeSet,
        def
    ) {

    }

    private var airingTime: AiringTimeModel? = null

    fun setAiringText(airingTime: AiringTimeModel?) {
        this.airingTime?.commonTimer?.timerCallback = null
        this.airingTime = airingTime
        airingTime?.commonTimer?.timerCallback = this
        updateView()
    }

    override fun invoke() {
        updateView()
    }

    private fun updateView() {
        val time = airingTime?.airingTime
        text = if (time != null)
            context.getString(R.string.airing_time_e_s_s).format(
                airingTime?.episode?.toString().naText(),
                time.day,
                time.hour,
                time.min,
                time.sec
            )
        else
            context.getString(R.string.airing_time_e_s_s).format(
                airingTime?.episode?.toString().naText(),
                0,
                0,
                0,
                0
            )
    }

}