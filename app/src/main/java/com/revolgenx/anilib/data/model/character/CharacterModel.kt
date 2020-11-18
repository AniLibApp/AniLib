package com.revolgenx.anilib.data.model.character

import com.revolgenx.anilib.data.model.BaseModel
import com.revolgenx.anilib.data.model.CharacterImageModel

open class CharacterModel : BaseModel() {
    var characterId: Int = -1
        set(value) {
            field = value
            baseId = characterId
        }
    var name: CharacterNameModel? = null
    var isFavourite: Boolean = false
    var siteUrl: String? = null
    var favourites: Int? = null
    var description: String? = null
    var characterImageModel: CharacterImageModel? = null
}