package com.revolgenx.anilib.infrastructure.event

data class UserBrowseEvent(var userId: Int?, var userName: String? = null) : CommonEvent()