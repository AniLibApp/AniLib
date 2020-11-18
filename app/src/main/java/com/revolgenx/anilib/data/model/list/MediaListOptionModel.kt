package com.revolgenx.anilib.data.model.list

import com.revolgenx.anilib.data.model.BaseModel

class MediaListOptionModel : BaseModel() {
    var scoreFormat:Int? = null
    var animeList:MediaListOptionTypeModel? = null
    var mangaList:MediaListOptionTypeModel? = null
}