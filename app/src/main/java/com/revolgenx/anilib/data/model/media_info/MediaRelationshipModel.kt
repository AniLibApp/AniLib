package com.revolgenx.anilib.data.model.media_info

import com.revolgenx.anilib.data.model.CommonMediaModel

class MediaRelationshipModel : CommonMediaModel() {
    var relationshipType: Int? = null

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
