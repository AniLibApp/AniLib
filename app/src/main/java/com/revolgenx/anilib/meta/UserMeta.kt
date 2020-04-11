package com.revolgenx.anilib.meta

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserMeta(var userId: Int?, var userName: Int?, var loggedInUser: Boolean = false) :
    Parcelable