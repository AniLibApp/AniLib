package com.revolgenx.anilib.notification.ui.model

import com.revolgenx.anilib.thread.ui.model.ThreadModel
import com.revolgenx.anilib.type.NotificationType
import com.revolgenx.anilib.user.ui.model.UserModel

class ThreadNotificationModel(
    override val id: Int,
    override val createdAt: Int?,
    override val createdAtPrettyTime: String?,
    override val type: NotificationType?,
    override var unreadNotificationCount: Int = 0,
    val userId: Int,
    val user: UserModel?,
    val thread: ThreadModel?,
    val context: String?
) : NotificationModel()