package com.revolgenx.anilib.user.ui.model.stats

import com.revolgenx.anilib.common.ui.model.DateModel

data class UserActivityHistoryModel(
    val amount: Int,
    val date: DateModel,
    val level: Int,
    val alpha: Float
)