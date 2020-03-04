package com.revolgenx.anilib.event

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