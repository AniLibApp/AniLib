package com.revolgenx.anilib.common.data.field

import com.apollographql.apollo3.api.Optional
import com.revolgenx.anilib.ToggleFavouriteMutation

class ToggleFavouriteField:BaseField<ToggleFavouriteMutation>() {
    var animeId: Int? = null
    var mangaId: Int? = null
    var characterId: Int? = null
    var staffId: Int? = null
    var studioId: Int? = null

    override fun toQueryOrMutation(): ToggleFavouriteMutation {
        return ToggleFavouriteMutation(
            animeId = Optional.presentIfNotNull(animeId),
            mangaId = Optional.presentIfNotNull(mangaId),
            characterId = Optional.presentIfNotNull(characterId),
            staffId = Optional.presentIfNotNull(staffId),
            studioId = Optional.presentIfNotNull(studioId),
        )
    }
}