package com.revolgenx.anilib.appwidget.infrastructure.event

import com.revolgenx.anilib.infrastructure.event.BaseEvent

sealed class AiringScheduleWidgetEvent(val widgetId: Int):BaseEvent() {
    class RefreshScheduleEvent(id:Int): AiringScheduleWidgetEvent(id)
    class ChangePageEvent(id:Int, val isNextPage:Boolean): AiringScheduleWidgetEvent(id)
    class FieldChangeEvent(id:Int): AiringScheduleWidgetEvent(id)
}