package com.revolgenx.anilib.model.character

import com.revolgenx.anilib.model.BaseModel
import com.revolgenx.anilib.model.CharacterImageModel

open class CharacterModel : BaseModel() {
    var characterId: Int = -1
    var name: CharacterNameModel? = null
    var isFavourite: Boolean = false
    var siteUrl: String? = null
    var favourites: Int? = null
    var description: String? = null
    var characterImageModel: CharacterImageModel? = null
}