package com.revolgenx.anilib.media.data.model

import com.revolgenx.anilib.airing.data.model.AiringTimeModel
import com.revolgenx.anilib.common.data.model.CommonMediaModel


class MediaOverviewModel : CommonMediaModel() {
    var meanScore: Int? = null
    var hashTag: String? = null  /*can be null*/
    var airingTimeModel: AiringTimeModel? = null
    var relationship: List<MediaRelationshipModel>? = null
    var externalLink: List<MediaExternalLinkModel>? = null /*empty */
    var tags: List<MediaTagModel>? = null
    var trailer: MediaTrailerModel? = null       /*can be null*/
    var characters: List<MediaEdgeModel>? = null
}
