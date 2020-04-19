package com.revolgenx.anilib.event

data class UserBrowseEvent(var userId: Int?, var userName: String? = null) : BaseEvent()