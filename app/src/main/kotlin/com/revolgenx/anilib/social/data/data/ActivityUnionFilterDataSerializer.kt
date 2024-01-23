package com.revolgenx.anilib.social.data.data

import com.revolgenx.anilib.common.data.store.BaseSerializer
import kotlinx.serialization.KSerializer

class ActivityUnionFilterDataSerializer(override val defaultValue: ActivityUnionFilterData = ActivityUnionFilterData()) :
    BaseSerializer<ActivityUnionFilterData>(defaultValue) {
    override fun serializer(): KSerializer<ActivityUnionFilterData> = ActivityUnionFilterData.serializer()
}