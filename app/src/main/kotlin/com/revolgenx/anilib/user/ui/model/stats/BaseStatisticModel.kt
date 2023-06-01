package com.revolgenx.anilib.user.ui.model.stats

import java.util.concurrent.TimeUnit

@Suppress("LeakingThis")
abstract class BaseStatisticModel : StatisticModel {
    //conversion
    val hoursWatched: Int = minutesWatched.takeIf { it != 0 }?.div(60) ?: 0
    val day: Long = TimeUnit.MINUTES.toDays(minutesWatched.toLong())
    val hour: Long = TimeUnit.MINUTES.toHours(minutesWatched.toLong()) - TimeUnit.DAYS.toHours(
        TimeUnit.MINUTES.toDays(minutesWatched.toLong())
    )
}

interface StatisticModel {
    val count: Int
    val meanScore: Double
    val minutesWatched: Int
    val chaptersRead: Int?
    val mediaIds: List<Int>?
}