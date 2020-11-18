package com.revolgenx.anilib.data.field.character

import com.revolgenx.anilib.CharacterActorQuery
import com.revolgenx.anilib.data.field.BaseField

class CharacterVoiceActorField :
    BaseField<CharacterActorQuery>() {
    var characterId: Int? = null
    override fun toQueryOrMutation(): CharacterActorQuery {
        return CharacterActorQuery.builder()
            .characterId(characterId)
            .build()
    }

}
