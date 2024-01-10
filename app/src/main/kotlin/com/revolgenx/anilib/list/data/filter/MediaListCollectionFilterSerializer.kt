package com.revolgenx.anilib.list.data.filter

import com.revolgenx.anilib.common.data.store.BaseSerializer
import kotlinx.serialization.KSerializer

class MediaListCollectionFilterSerializer(
    override val defaultValue: MediaListCollectionFilter = MediaListCollectionFilter()
) : BaseSerializer<MediaListCollectionFilter>(defaultValue) {
    override fun serializer(): KSerializer<MediaListCollectionFilter> =
        MediaListCollectionFilter.serializer()
}

