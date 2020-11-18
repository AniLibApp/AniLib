package com.revolgenx.anilib.data.meta

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserMeta(var userId: Int?, var userName: String?, var loggedInUser: Boolean = false) :
    Parcelable{
    companion object{
        const val userMetaKey = "userMetaKey"
    }
}
