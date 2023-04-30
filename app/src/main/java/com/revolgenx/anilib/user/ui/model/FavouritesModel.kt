package com.revolgenx.anilib.user.ui.model

import com.revolgenx.anilib.character.ui.model.CharacterConnectionModel
import com.revolgenx.anilib.media.ui.model.MediaConnectionModel
import com.revolgenx.anilib.staff.ui.model.StaffConnectionModel
import com.revolgenx.anilib.studios.ui.model.StudioConnectionModel

data class FavouritesModel(
    var anime: MediaConnectionModel? = null,
    var manga: MediaConnectionModel? = null,
    var characters: CharacterConnectionModel? = null,
    var staff: StaffConnectionModel? = null,
    var studios: StudioConnectionModel? = null
)