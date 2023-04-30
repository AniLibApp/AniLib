package com.revolgenx.anilib.common.data.event

import org.greenrobot.eventbus.EventBus

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


sealed class CommonEvent{

}