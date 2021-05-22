package com.revolgenx.anilib.infrastructure.event

import androidx.annotation.IdRes


abstract class CommonEvent:BaseEvent()

data class SettingEvent(val settingEventType: SettingEventTypes):BaseEvent()
enum class SettingEventTypes{
    ABOUT, MEDIA_LIST, MEDIA_SETTING, APPLICATION, SETTING, THEME, CUSTOMIZE_FILTER, AIRING_WIDGET, TRANSLATION, NOTIFICATION
}

class BrowseAllReviewsEvent:CommonEvent()
data class BrowseReviewEvent(val reviewId: Int?) : CommonEvent()

