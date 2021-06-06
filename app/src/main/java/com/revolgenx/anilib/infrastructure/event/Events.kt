package com.revolgenx.anilib.infrastructure.event

import android.text.Spanned
import com.revolgenx.anilib.data.meta.ListEditorMeta
import com.revolgenx.anilib.data.meta.MediaInfoMeta
import com.revolgenx.anilib.data.meta.MediaListMeta
import com.revolgenx.anilib.data.model.search.filter.MediaSearchFilterModel

data class OpenUserProfileEvent(val userId: Int? = null, val username: String? = null) : BaseEvent()
data class OpenUserFriendEvent(val userId: Int? = null, val isFollower: Boolean = false) :
    BaseEvent()

data class OpenMediaInfoEvent(val meta: MediaInfoMeta) : BaseEvent()
data class OpenMediaListEditorEvent(val meta: ListEditorMeta) : BaseEvent()
data class OpenSettingEvent(val settingEventType: SettingEventTypes) : BaseEvent()
enum class SettingEventTypes {
    ABOUT, MEDIA_LIST, MEDIA_SETTING, APPLICATION, SETTING, THEME, CUSTOMIZE_FILTER, AIRING_WIDGET, TRANSLATION, NOTIFICATION, LANGUAGE_CHOOSER
}

data class OpenSearchEvent(val data: MediaSearchFilterModel? = null) : BaseEvent()
data class OpenReviewEvent(val reviewId: Int) : BaseEvent()
class OpenAllReviewEvent() : BaseEvent()
class OpenNotificationCenterEvent : BaseEvent()

data class OpenCharacterEvent(val characterId: Int) : BaseEvent()
data class OpenStaffEvent(val staffId: Int) : BaseEvent()
data class OpenStudioEvent(val studioId: Int) : BaseEvent()

data class OpenReviewComposerEvent(val mediaId: Int) : BaseEvent()

class OpenAiringScheduleEvent : BaseEvent()

data class OpenUserMediaListEvent(val meta: MediaListMeta) : BaseEvent()

class OpenAlSiteEvent() : CommonEvent()
data class OpenMediaListingEvent(val mediaIdsIn: List<Int>) : BaseEvent()
class OpenActivityComposer() : BaseEvent()
data class OpenSpoilerContentEvent(val spanned: Spanned) : BaseEvent()

data class OpenActivityInfoEvent(val activityId:Int):BaseEvent()