package com.revolgenx.anilib.model

import com.revolgenx.anilib.type.MediaFormat

class MediaRelationshipModel : BaseMediaModel() {
    var relationshipType: Int? = null
    var title: TitleModel? = null
    var format: Int? = MediaFormat.`$UNKNOWN`?.ordinal
    var status: Int? = null
    var averageScore: Int? = null
    var coverImageModel: CoverImageModel? = null
    var bannerImage:String? = null
    var type:Int? = null
    var seasonYear:Int? = null

    override fun equals(other: Any?): Boolean {
        return if (other is MediaRelationshipModel) {
            this.mediaId == other.mediaId
        } else false
    }

    override fun hashCode(): Int {
        var result = relationshipType ?: 0
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (format ?: 0)
        result = 31 * result + (status ?: 0)
        return result
    }
}
