package com.revolgenx.anilib.user.data.meta

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserMeta(var userId: Int?, var userName: String?, var loggedInUser: Boolean = false) :
    Parcelable {
    companion object{
        const val userMetaKey = "userMetaKey"
    }
}