package com.revolgenx.anilib.data.model.media_info

import com.revolgenx.anilib.data.model.VoiceActorModel
import com.revolgenx.anilib.data.model.character.CharacterModel

class MediaCharacterModel:CharacterModel() {
    var role: Int? = null
    var voiceActor: VoiceActorModel? = null
    var type: Int? = null
}
