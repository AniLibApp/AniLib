package com.revolgenx.anilib.util

import android.os.Handler
import com.revolgenx.anilib.airing.data.model.TimeUntilAiringModel


typealias TimerCallback = (() -> Unit)?

open class CommonTimer(var handler: Handler?, val timeUntilAiringModel: TimeUntilAiringModel) : Runnable {

    init {
        start()
    }

    var timerCallback: TimerCallback = null



    companion object {
        const val commonRefreshTime = 1000L
        const val decreaseTimeBy = 1
    }

    override fun run() {
        if (handler == null) return
        if (timeUntilAiringModel.time <= 0) return

        timeUntilAiringModel.time -= decreaseTimeBy
        timerCallback?.invoke()
        handler!!.postDelayed(this, commonRefreshTime)
    }

    fun start() {
        if (handler == null) return

        handler!!.postDelayed(this, commonRefreshTime)
    }

    fun removeCallback() {
        timerCallback = null
    }

}