package com.revolgenx.anilib.common.data.state

data class UserState(val userId: Int? = null) {
    val isLoggedIn = userId != null
}