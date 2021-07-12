package com.revolgenx.anilib.data.model.staff

import com.revolgenx.anilib.data.model.CommonMediaModel
import com.revolgenx.anilib.data.model.character.CharacterImageModel
import com.revolgenx.anilib.data.model.character.CharacterNameModel

class StaffMediaCharacterModel : CommonMediaModel() {
    var characterId: Int? = null
    var characterName: CharacterNameModel? = null
    var characterImageModel: CharacterImageModel? = null
    var mediaRole: Int? = null
}
