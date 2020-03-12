package com.revolgenx.anilib.model

import com.revolgenx.anilib.type.MediaFormat

class MediaRelationshipModel : BaseMediaModel() {
    var relationshipType: Int? = null
    var title: TitleModel? = null
    var format: Int? = MediaFormat.`$UNKNOWN`?.ordinal
    var status: Int? = null
}
