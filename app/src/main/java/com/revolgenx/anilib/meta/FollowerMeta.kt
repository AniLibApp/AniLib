package com.revolgenx.anilib.meta

import kotlinx.android.parcel.Parcelize

@Parcelize
data class FollowerMeta(var userId: Int?, var isFollowing: Boolean = false) : BaseMeta
