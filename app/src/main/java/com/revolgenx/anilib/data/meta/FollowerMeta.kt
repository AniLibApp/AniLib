package com.revolgenx.anilib.data.meta

import kotlinx.android.parcel.Parcelize

@Parcelize
data class FollowerMeta(var userId: Int?, var isFollowing: Boolean = false) : BaseMeta
