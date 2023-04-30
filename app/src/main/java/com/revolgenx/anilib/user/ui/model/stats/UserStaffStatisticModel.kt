package com.revolgenx.anilib.user.ui.model.stats

import com.revolgenx.anilib.staff.ui.model.StaffModel

data class UserStaffStatisticModel(
    val staff: StaffModel? = null,
    override val count: Int = 0,
    override val meanScore: Double = 0.0,
    override val minutesWatched: Int = 0,
    override val chaptersRead: Int = 0,
    override val mediaIds: List<Int> = emptyList(),
) : BaseStatisticModel()