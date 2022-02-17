package com.revolgenx.anilib.user.viewmodel

import com.revolgenx.anilib.common.viewmodel.BaseViewModel

class UserStatsContainerSharedVM : BaseViewModel() {
    var userId: Int? = null
    var userName: String? = null

    val hasUserData get() = takeIf { userId != null || userName != null }
}