package com.revolgenx.anilib.data.meta

import kotlinx.parcelize.Parcelize

@Parcelize
data class CharacterMeta(var characterId: Int, var characterUrl: String?) : BaseMeta
