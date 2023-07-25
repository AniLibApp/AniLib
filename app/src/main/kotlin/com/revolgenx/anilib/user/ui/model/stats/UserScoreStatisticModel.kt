package com.revolgenx.anilib.user.ui.model.stats

data class UserScoreStatisticModel(
    val score: Int,
    override val count: Int,
    override val meanScore: Double,
    override val minutesWatched: Int = 0,
    override val chaptersRead: Int = 0,
    override val mediaIds: List<Int> = emptyList(),
) : BaseStatisticModel(minutesWatched)