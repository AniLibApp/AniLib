package com.revolgenx.anilib.notification.ui.model

import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.type.NotificationType

data class MediaDataChangeNotificationModel(
    override val id: Int,
    override val createdAt: Int?,
    override val createdAtPrettyTime: String?,
    override val type: NotificationType?,
    override var unreadNotificationCount: Int = 0,
    val context: String?,
    val mediaId: Int,
    val media: MediaModel?,
    val reason: String?
) : NotificationModel(id)