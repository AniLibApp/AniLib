package com.revolgenx.anilib.event

import org.greenrobot.eventbus.EventBus

abstract class BaseEvent {
    fun postEvent() {
        EventBus.getDefault().post(this)
    }

    fun postSticky(){
        EventBus.getDefault().postSticky(this)
    }
}