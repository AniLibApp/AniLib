package com.revolgenx.anilib.data.field.list

import com.revolgenx.anilib.AlMediaListCollectionQuery
import com.revolgenx.anilib.common.data.field.BaseSourceUserField
import com.revolgenx.anilib.data.field.MediaListCollectionFilterField
import com.revolgenx.anilib.type.MediaListSort
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.MediaListPageQuery
import com.revolgenx.anilib.MediaListCollectionIdQuery


class MediaListCollectionField : BaseSourceUserField<AlMediaListCollectionQuery>() {
    var type: Int? = null
    var mediaListStatus: Int? = null
    var filter = MediaListCollectionFilterField()

    override fun toQueryOrMutation(): AlMediaListCollectionQuery {
        return AlMediaListCollectionQuery()
    }
}


class MediaListCollectionIdsField : BaseSourceUserField<MediaListCollectionIdQuery>() {
    var type: Int? = null
    var mediaListStatus: List<Int>? = null

    override fun toQueryOrMutation(): MediaListCollectionIdQuery {
        val mType =
            type?.let {
                MediaType.values()[it]
            }
        val listStatus = mediaListStatus?.let {
            it.map { MediaListStatus.values()[it] }
        }

        return MediaListCollectionIdQuery(
            userId = nn(userId),
            userName = nn(userName),
            type = nn(mType),
            status_in = nn(listStatus)
        )
    }


}

class MediaListField : BaseSourceUserField<MediaListPageQuery>() {
    var type: Int? = null
    var status: Int? = null
    var sort: Int? = null

    override fun toQueryOrMutation(): MediaListPageQuery {
        val mType = type?.let {
            MediaType.values()[it]
        }

        val mStatus = status?.let {
            MediaListStatus.values()[it]
        }

        val mSort = sort?.let {
            listOf(MediaListSort.values()[it])
        }
        return MediaListPageQuery(
            page = nn(page),
            perPage = nn(perPage),
            userId = nn(userId),
            userName = nn(userName),
            status = nn(mStatus),
            sort = nn(mSort),
            type = nn(mType)
        )
    }
}

