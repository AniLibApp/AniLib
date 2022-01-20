package com.revolgenx.anilib.staff.data.model

import com.revolgenx.anilib.common.data.model.CommonMediaModel
import com.revolgenx.anilib.character.data.model.CharacterImageModel
import com.revolgenx.anilib.character.data.model.CharacterNameModel

class StaffMediaCharacterModel : CommonMediaModel() {
    var characterId: Int? = null
    var characterName: CharacterNameModel? = null
    var characterImageModel: CharacterImageModel? = null
    var mediaRole: Int? = null
}
