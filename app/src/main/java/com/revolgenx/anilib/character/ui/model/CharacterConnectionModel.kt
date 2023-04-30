package com.revolgenx.anilib.character.ui.model

data class CharacterConnectionModel(
    var edges: List<CharacterEdgeModel>? = null,
    var nodes: List<CharacterModel>? = null,
)