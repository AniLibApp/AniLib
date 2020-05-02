package com.revolgenx.anilib.model


class MediaOverviewModel : CommonMediaModel() {
    var popularity: Int? = null
    var favourites: Int? = null
    var meanScore:Int? = null
    var description: String? = null
    var source: Int? = null
    var hashTag: String? = null  /*can be null*/
    var airingTimeModel:AiringTimeModel? = null
    var relationship: List<MediaRelationshipModel>? = null
    var externalLink: List<MediaExternalLinkModel>? = null /*empty */
    var tags: List<MediaTagsModel>? = null
    var trailer: MediaTrailerModel? = null       /*can be null*/
    var studios: List<MediaStudioModel>? = null        /*empty in manga*/
}
