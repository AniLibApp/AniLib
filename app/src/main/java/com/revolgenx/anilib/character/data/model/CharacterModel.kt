package com.revolgenx.anilib.character.data.model

import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.common.data.model.FuzzyDateModel
import com.revolgenx.anilib.media.data.model.MediaConnectionModel


class CharacterModel:BaseModel(){
    var bloodType: String? = null
    var dateOfBirth: FuzzyDateModel? = null
    var age: String? = null
    var gender: String? = null

    var name: CharacterNameModel? = null
    var isFavourite: Boolean = false
    // If the character is blocked from being added to favourites
    var isFavouriteBlocked: Boolean = false
    var media: MediaConnectionModel? = null

    var siteUrl: String? = null
    var favourites: Int? = null
    var description: String? = null
    var image: CharacterImageModel? = null
}


