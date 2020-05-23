package com.revolgenx.anilib.meta

import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterMeta(var characterId: Int, var characterUrl: String?) : BaseMeta
