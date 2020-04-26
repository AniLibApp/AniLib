package com.revolgenx.anilib.meta

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserStatsMeta(var userMeta: UserMeta, var type: Int) : Parcelable