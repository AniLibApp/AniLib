package com.revolgenx.anilib.character.ui.model

data class CharacterNameModel(
    val full: String?,
    val native: String? = null,
    val alternative: List<String>? = null,
    val alternativeSpoiler: List<String>? = null
)
