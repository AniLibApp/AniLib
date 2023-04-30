package com.revolgenx.anilib.studios.ui.model

data class StudioEdgeModel(
    //    The order the character should be displayed from the users favourites
    var favouriteOrder: Int? = null,
    //    The id of the connection
    var id: Int? = null,
    //    If the studio is the main animation studio of the anime
    var isMain: Boolean = false,
    var node: StudioModel? = null,
)