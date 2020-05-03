package com.revolgenx.anilib.model.notification.thread

import com.revolgenx.anilib.model.thread.ThreadCommentModel

open class ThreadCommentNotification : ThreadNotification() {
    var threadCommentModel: ThreadCommentModel? = null
}