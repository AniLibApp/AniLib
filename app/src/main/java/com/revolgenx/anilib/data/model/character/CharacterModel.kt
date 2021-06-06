package com.revolgenx.anilib.data.model.character

import com.revolgenx.anilib.data.model.BaseModel

open class CharacterModel : BaseModel() {
    var name: CharacterNameModel? = null
    var isFavourite: Boolean = false
    var siteUrl: String? = null
    var favourites: Int? = null
    var description: String? = null
    var image: CharacterImageModel? = null
}