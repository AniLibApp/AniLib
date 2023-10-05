package com.revolgenx.anilib.airing.ui.model

import android.os.Handler
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.mutableStateOf
import java.time.Instant

data class AiringScheduleTimer(
    val handler: Handler,
    val airingAt: Int,
    val timeUntilAiringModel: TimeUntilAiringModel
) : Runnable {
    private val timeLeftPolicy = object : SnapshotMutationPolicy<TimeUntilAiringModel> {
        override fun equivalent(a: TimeUntilAiringModel, b: TimeUntilAiringModel): Boolean {
            return if (a.oldTime != b.time) {
                a.oldTime = b.time
                false
            } else {
                true
            }
        }
    }
    val timeLeft = mutableStateOf(timeUntilAiringModel, policy = timeLeftPolicy)

    override fun run() {
        if (timeUntilAiringModel.alreadyAired) return
        timeUntilAiringModel.time -= 1
        timeLeft.value = timeUntilAiringModel
        handler.postDelayed(this, 1000)
    }

    fun start() {
        if (timeUntilAiringModel.alreadyAired) return
        timeUntilAiringModel.time = airingAt.minus(Instant.now().epochSecond)
        timeLeft.value = timeUntilAiringModel
        handler.postDelayed(this, 1000)
    }

    fun stop() {
        handler.removeCallbacksAndMessages(null)
    }
}