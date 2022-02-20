package com.revolgenx.anilib.common.event

import com.revolgenx.anilib.common.event.CommonEvent

data class SessionEvent(var loggedIn:Boolean) : CommonEvent()