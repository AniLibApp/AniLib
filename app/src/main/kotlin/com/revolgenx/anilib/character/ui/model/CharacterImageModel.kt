package com.revolgenx.anilib.character.ui.model

import com.revolgenx.anilib.common.ui.model.BaseImageModel

data class CharacterImageModel(val medium: String?, val large: String?) :
    BaseImageModel(medium, large)