package com.revolgenx.anilib.user.data.meta

import android.os.Parcelable
import com.revolgenx.anilib.user.data.meta.UserMeta
import kotlinx.parcelize.Parcelize

@Parcelize
class UserStatsMeta(var userMeta: UserMeta, var type: Int) : Parcelable