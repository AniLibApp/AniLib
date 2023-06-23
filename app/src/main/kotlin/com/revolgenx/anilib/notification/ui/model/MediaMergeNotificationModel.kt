package com.revolgenx.anilib.notification.ui.model

import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.type.NotificationType

data class MediaMergeNotificationModel(
    override val id: Int,
    override val createdAt: Int?,
    override val createdAtPrettyTime: String?,
    override val type: NotificationType?,
    override var unreadNotificationCount: Int = 0,
    val context: String?,
    val reason: String?,
    val deletedMediaTitles: List<String>?,
    val mediaId: Int,
    val media: MediaModel?
) : NotificationModel()