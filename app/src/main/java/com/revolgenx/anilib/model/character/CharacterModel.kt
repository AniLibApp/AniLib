package com.revolgenx.anilib.model.character

import com.revolgenx.anilib.model.CharacterImageModel

class CharacterModel {
    var characterId: Int = -1
    var name: CharacterNameModel? = null
    var isFavourite: Boolean = false
    var siteUrl: String? = null
    var favourites: Int? = null
    var descrition: String? = null
    var characterImageModel:CharacterImageModel? = null
}