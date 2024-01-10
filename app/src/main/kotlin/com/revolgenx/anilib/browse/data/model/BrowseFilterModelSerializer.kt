package com.revolgenx.anilib.browse.data.model

import com.revolgenx.anilib.common.data.store.BaseSerializer
import kotlinx.serialization.KSerializer

class BrowseFilterModelSerializer(override val defaultValue: BrowseFilterModel = BrowseFilterModel()) :
    BaseSerializer<BrowseFilterModel>(defaultValue) {
    override fun serializer(): KSerializer<BrowseFilterModel> = BrowseFilterModel.serializer()
}
