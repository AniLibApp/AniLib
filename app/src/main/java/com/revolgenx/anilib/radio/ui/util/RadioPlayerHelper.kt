package com.revolgenx.anilib.radio.ui.util

import android.content.Context
import android.content.Intent
import com.revolgenx.anilib.radio.service.RadioService

object RadioPlayerHelper {

    fun pausePlay(context: Context, id:Long) {
        context.startService(
            Intent(
                context,
                RadioService::class.java
            ).also {
                it.action = RadioService.PLAY_ACTION_KEY
                it.putExtra(RadioService.MEDIA_ID_EXTRA_KEY, id)
            })
    }
}