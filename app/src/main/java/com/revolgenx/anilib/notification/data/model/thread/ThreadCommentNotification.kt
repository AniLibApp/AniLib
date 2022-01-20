package com.revolgenx.anilib.notification.data.model.thread

import com.revolgenx.anilib.notification.data.model.ThreadCommentModel

open class ThreadCommentNotification : ThreadNotification() {
    var threadCommentModel: ThreadCommentModel? = null
}