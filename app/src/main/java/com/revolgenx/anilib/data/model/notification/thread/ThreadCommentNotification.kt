package com.revolgenx.anilib.data.model.notification.thread

import com.revolgenx.anilib.data.model.thread.ThreadCommentModel

open class ThreadCommentNotification : ThreadNotification() {
    var threadCommentModel: ThreadCommentModel? = null
}