package com.revolgenx.anilib.character.data.model

import com.revolgenx.anilib.common.data.model.BaseModel

//TODO
class VoiceActorModel: BaseModel() {
    var actorId: Int? = null
    var name: String? = null
    @Deprecated("use languageV2")
    var language: Int? = null
    var languageV2: String? = null
    var voiceActorImageModel: VoiceActorImageModel? = null
}
