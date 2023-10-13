package com.revolgenx.anilib.user.ui.model.statistics

import com.revolgenx.anilib.type.MediaFormat


data class UserFormatStatisticModel(
    val format: MediaFormat? = null,
    override val count: Int = 0,
    override val meanScore: Double = 0.0,
    override val minutesWatched: Int = 0,
    override val chaptersRead: Int = 0,
    override val mediaIds: List<Int> = emptyList(),
) : BaseStatisticModel(minutesWatched)



