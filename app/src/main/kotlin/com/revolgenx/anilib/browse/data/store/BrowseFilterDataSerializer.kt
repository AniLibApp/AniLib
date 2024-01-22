package com.revolgenx.anilib.browse.data.store

import com.revolgenx.anilib.common.data.store.BaseSerializer
import kotlinx.serialization.KSerializer

class BrowseFilterDataSerializer(override val defaultValue: BrowseFilterData = BrowseFilterData()) :
    BaseSerializer<BrowseFilterData>(defaultValue) {
    override fun serializer(): KSerializer<BrowseFilterData> = BrowseFilterData.serializer()
}
