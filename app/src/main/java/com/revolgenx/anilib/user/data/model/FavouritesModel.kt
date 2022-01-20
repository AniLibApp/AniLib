package com.revolgenx.anilib.user.data.model

import com.revolgenx.anilib.character.data.model.CharacterConnectionModel
import com.revolgenx.anilib.media.data.model.MediaConnectionModel
import com.revolgenx.anilib.staff.data.model.StaffConnectionModel
import com.revolgenx.anilib.studio.data.model.StudioConnectionModel

class FavouritesModel {
    var anime:MediaConnectionModel? = null
    var manga:MediaConnectionModel? = null
    var characters:CharacterConnectionModel? = null
    var staff:StaffConnectionModel? = null
    var studios:StudioConnectionModel? = null
}