package com.revolgenx.anilib.character.data.field

import com.revolgenx.anilib.CharacterActorQuery
import com.revolgenx.anilib.common.data.field.BaseField

class CharacterVoiceActorField :
    BaseField<CharacterActorQuery>() {
    var characterId: Int? = null
    override fun toQueryOrMutation(): CharacterActorQuery {
        return CharacterActorQuery.builder()
            .characterId(characterId)
            .build()
    }

}
