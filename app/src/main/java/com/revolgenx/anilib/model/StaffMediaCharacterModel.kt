package com.revolgenx.anilib.model

import com.revolgenx.anilib.model.character.CharacterNameModel

class StaffMediaCharacterModel : CommonMediaModel() {
    var characterId: Int? = null
    var characterName: CharacterNameModel? = null
    var characterImageModel: CharacterImageModel? = null
    var mediaRole: Int? = null
}
