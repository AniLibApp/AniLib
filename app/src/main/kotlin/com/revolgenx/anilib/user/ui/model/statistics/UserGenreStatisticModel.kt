package com.revolgenx.anilib.user.ui.model.statistics

data class UserGenreStatisticModel(
    val genre: String? = null,
    override var count: Int = 0,
    override val meanScore: Double = 0.0,
    override val minutesWatched: Int = 0,
    override val chaptersRead: Int = 0,
    override val mediaIds: List<Int> = emptyList(),
) : BaseStatisticModel(minutesWatched)