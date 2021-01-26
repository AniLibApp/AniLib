package com.revolgenx.anilib.data.model

import com.revolgenx.anilib.data.model.airing.AiringTimeModel


class MediaOverviewModel : CommonMediaModel() {
    var meanScore:Int? = null
    var source: Int? = null
    var hashTag: String? = null  /*can be null*/
    var airingTimeModel: AiringTimeModel? = null
    var relationship: List<MediaRelationshipModel>? = null
    var externalLink: List<MediaExternalLinkModel>? = null /*empty */
    var tags: List<MediaTagsModel>? = null
    var trailer: MediaTrailerModel? = null       /*can be null*/
    var studios: List<MediaStudioModel>? = null        /*empty in manga*/
}
