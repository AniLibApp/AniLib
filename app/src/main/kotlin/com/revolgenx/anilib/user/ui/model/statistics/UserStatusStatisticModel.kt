package com.revolgenx.anilib.user.ui.model.statistics

import com.revolgenx.anilib.type.MediaListStatus

data class UserStatusStatisticModel(
    val status: MediaListStatus? = null,
    override val count: Int = 0,
    override val meanScore: Double = 0.0,
    override val minutesWatched: Int = 0,
    override val chaptersRead: Int = 0,
    override val mediaIds: List<Int> = emptyList(),
) : BaseStatisticModel(minutesWatched)
