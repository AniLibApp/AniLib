package com.revolgenx.anilib.notification.ui.model

import com.revolgenx.anilib.type.NotificationType

data class MediaDeletionNotificationModel(
    override val id: Int,
    override val createdAt: Int?,
    override val createdAtPrettyTime: String?,
    override val type: NotificationType?,
    override var unreadNotificationCount: Int = 0,
    val deletedMediaTitle: String?,
    val context: String?,
    val reason: String?
) : NotificationModel()