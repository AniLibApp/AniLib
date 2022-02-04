package com.revolgenx.anilib.ui.view.airing

import android.content.Context
import android.util.AttributeSet
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.airing.data.model.AiringScheduleModel
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

    private var showEpisode = true

    private var airingTime: AiringScheduleModel? = null

    fun setAiringText(airingTime: AiringScheduleModel?, showEpisode: Boolean = true) {
        this.showEpisode = showEpisode
        this.airingTime?.commonTimer?.timerCallback = null
        this.airingTime = airingTime
        airingTime?.commonTimer?.timerCallback = this
        updateView()
    }

    override fun invoke() {
        updateView()
    }

    private fun updateView() {
        val time = airingTime?.timeUntilAiringModel
        text = if (time != null) {
            if (showEpisode) {
                context.getString(R.string.airing_time_e_s_s).format(
                    airingTime?.episode?.toString().naText(),
                    time.day,
                    time.hour,
                    time.min,
                    time.sec
                )
            } else {
                context.getString(R.string.airing_time_eta_s_s).format(
                    time.day,
                    time.hour,
                    time.min,
                    time.sec
                )
            }
        } else {
            if (showEpisode) {
                context.getString(R.string.airing_time_e_s_s).format(
                    airingTime?.episode?.toString().naText(),
                    0,
                    0,
                    0,
                    0
                )
            }else{
                context.getString(R.string.airing_time_eta_s_s).format(
                    0,
                    0,
                    0,
                    0
                )
            }

        }
    }

}