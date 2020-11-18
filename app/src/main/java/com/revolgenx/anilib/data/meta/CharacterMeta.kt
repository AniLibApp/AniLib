package com.revolgenx.anilib.data.meta

import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterMeta(var characterId: Int, var characterUrl: String?) : BaseMeta
