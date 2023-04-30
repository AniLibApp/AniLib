package com.revolgenx.anilib.user.ui.model


data class MediaListOptionModel(
    var scoreFormat:Int? = null,
    var rowOrder:Int? = null,
    var animeList: MediaListOptionTypeModel? = null,
    var mangaList: MediaListOptionTypeModel? = null,
)
