package com.revolgenx.anilib.field

import com.revolgenx.anilib.CharacterActorQuery

class CharacterVoiceActorField : BaseField<CharacterActorQuery> {
    var characterId: Int? = null
    override fun toQueryOrMutation(): CharacterActorQuery {
        return CharacterActorQuery.builder()
            .characterId(characterId)
            .build()
    }

}
