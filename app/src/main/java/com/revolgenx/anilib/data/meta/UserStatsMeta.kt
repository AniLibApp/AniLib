package com.revolgenx.anilib.data.meta

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserStatsMeta(var userMeta: UserMeta, var type: Int) : Parcelable