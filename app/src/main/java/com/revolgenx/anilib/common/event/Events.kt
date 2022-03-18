package com.revolgenx.anilib.common.event

import android.text.Spanned
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.data.meta.MediaListMeta
import com.revolgenx.anilib.app.setting.data.meta.TagFilterSettingMeta
import com.revolgenx.anilib.search.data.model.SearchFilterEventModel

data class OpenUserProfileEvent(val userId: Int? = null, val username: String? = null) : BaseEvent()
data class OpenUserFriendEvent(val userId: Int? = null, val isFollower: Boolean = false) :
    BaseEvent()

data class OpenMediaInfoEvent(val meta: MediaInfoMeta) : BaseEvent()
data class OpenMediaListEditorEvent(val mediaId:Int) : BaseEvent()
data class OpenSettingEvent(val settingEventType: SettingEventTypes, val data:SettingEventData?= null) : BaseEvent()
enum class SettingEventTypes {
    ABOUT, MEDIA_LIST, MEDIA_SETTING, APPLICATION, SETTING, THEME, CUSTOMIZE_FILTER, ADD_REMOVE_TAG_FILTER, AIRING_WIDGET, TRANSLATION, NOTIFICATION, LANGUAGE_CHOOSER
}
interface SettingEventData
data class TagSettingEventMeta(val meta: TagFilterSettingMeta):SettingEventData

class OpenSearchEvent(val data: SearchFilterEventModel? = null) : BaseEvent()
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
class OpenActivityTextComposer() : BaseEvent()
data class OpenActivityMessageComposer(val recipientId:Int) : BaseEvent()
data class OpenActivityReplyComposer(val activityId: Int) : BaseEvent()
data class OpenSpoilerContentEvent(val spanned: Spanned) : BaseEvent()

data class OpenActivityInfoEvent(val activityId:Int): BaseEvent()
data class OnActivityInfoUpdateEvent(val activityId: Int): BaseEvent()
data class OpenImageEvent(var url:String?) : CommonEvent()