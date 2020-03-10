package com.revolgenx.anilib.model

import com.revolgenx.anilib.type.MediaSource

class MediaOverviewModel :CommonMediaModel() {
    var popularity: Int = 0
    var favourites: Int = 0
    var season:Int? = null      /*can be null*/
    var seasonYear:Int? = null  /*can be null*/
    var description:String? = null
    var source:Int = MediaSource.`$UNKNOWN`.ordinal
    var hashTag:String? = null  /*can be null*/
    var relationship:MediaRelationshipModel? = null
    var externalLink:List<MediaExternalLinkModel>? = null /*empty */
    var tags:List<MediaTagsModel>? = null
    var trailer:MediaTrailerModel? = null       /*can be null*/
    var studios:MediaStudioModel? = null        /*empty in manga*/
}
