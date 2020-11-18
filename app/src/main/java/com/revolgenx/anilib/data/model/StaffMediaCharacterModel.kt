package com.revolgenx.anilib.data.model

import com.revolgenx.anilib.data.model.character.CharacterNameModel

class StaffMediaCharacterModel : CommonMediaModel() {
    var characterId: Int? = null
    var characterName: CharacterNameModel? = null
    var characterImageModel: CharacterImageModel? = null
    var mediaRole: Int? = null
}
