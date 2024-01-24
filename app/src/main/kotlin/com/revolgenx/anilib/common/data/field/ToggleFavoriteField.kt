package com.revolgenx.anilib.common.data.field

import com.revolgenx.anilib.ToggleFavouriteMutation

data class ToggleFavoriteField(
    var animeId: Int? = null,
    var mangaId: Int? = null,
    var characterId: Int? = null,
    var staffId: Int? = null,
    var studioId: Int? = null
) : BaseField<ToggleFavouriteMutation>() {

    override fun toQueryOrMutation(): ToggleFavouriteMutation {
        return ToggleFavouriteMutation(
            animeId = nn(animeId),
            mangaId = nn(mangaId),
            characterId = nn(characterId),
            staffId = nn(staffId),
            studioId = nn(studioId),
        )
    }
}