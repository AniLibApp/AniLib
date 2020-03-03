package com.revolgenx.anilib.event

import org.greenrobot.eventbus.EventBus

abstract class BaseEvent {
    val postEvent by lazy {
        EventBus.getDefault().post(this)
    }
    val postSticky by lazy {
        EventBus.getDefault().postSticky(this)
    }
}