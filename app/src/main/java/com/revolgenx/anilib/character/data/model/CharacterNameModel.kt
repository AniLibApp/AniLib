package com.revolgenx.anilib.character.data.model

import com.revolgenx.anilib.common.data.model.BaseNameModel

class CharacterNameModel(full: String?, native: String? = null, alternative: List<String>? = null) :
    BaseNameModel(full = full, native = native, alternative = alternative) {
    var alternativeSpoiler: List<String>? = null
}

