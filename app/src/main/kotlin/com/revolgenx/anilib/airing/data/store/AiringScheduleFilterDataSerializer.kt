package com.revolgenx.anilib.airing.data.store

import com.revolgenx.anilib.common.data.store.BaseSerializer
import kotlinx.serialization.KSerializer

class AiringScheduleFilterDataSerializer(override val defaultValue: AiringScheduleFilterData = AiringScheduleFilterData()) :
    BaseSerializer<AiringScheduleFilterData>(defaultValue) {
    override fun serializer(): KSerializer<AiringScheduleFilterData> = AiringScheduleFilterData.serializer()
}