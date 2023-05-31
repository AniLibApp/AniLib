package com.revolgenx.anilib.common.data.event

import android.text.Spanned
import com.revolgenx.anilib.type.MediaType
import org.greenrobot.eventbus.EventBus

interface EventBusListener


fun <T : EventBusListener> T.registerForEvent() {
    val bus = EventBus.getDefault()
    if (!bus.isRegistered(this))
        bus.register(this)
}

fun <T : EventBusListener> T.unRegisterForEvent() {
    val bus = EventBus.getDefault()
    if (bus.isRegistered(this)) {
        bus.unregister(this)
    }
}

abstract class BaseEvent {
    val postEvent: Unit
        get() {
            EventBus.getDefault().post(this)
        }
    val postSticky: Unit
        get() {
            EventBus.getDefault().postSticky(this)
        }
}


sealed class CommonEvent : BaseEvent()

data class OpenImageEvent(val imageUrl: String?) : CommonEvent()
data class OpenSpoilerEvent(val spanned: Spanned) : CommonEvent()
data class OpenUserScreenEvent(val userId: Int? = null, val username: String? = null) : CommonEvent()
data class OpenMediaScreenEvent(val mediaId: Int, val type: MediaType) : CommonEvent()
data class OpenCharacterScreenEvent(val characterId: Int) : CommonEvent()


