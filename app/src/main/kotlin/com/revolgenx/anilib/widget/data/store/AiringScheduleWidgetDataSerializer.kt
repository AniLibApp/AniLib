package com.revolgenx.anilib.widget.data.store

import com.revolgenx.anilib.common.data.store.BaseSerializer
import kotlinx.serialization.KSerializer

class AiringScheduleWidgetDataSerializer(override val defaultValue: AiringScheduleWidgetData = AiringScheduleWidgetData(null)) :
    BaseSerializer<AiringScheduleWidgetData>(defaultValue) {
    override fun serializer(): KSerializer<AiringScheduleWidgetData> = AiringScheduleWidgetData.serializer()
}