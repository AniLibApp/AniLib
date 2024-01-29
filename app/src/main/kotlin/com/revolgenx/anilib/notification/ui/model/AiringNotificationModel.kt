package com.revolgenx.anilib.notification.ui.model

import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.type.NotificationType

data class AiringNotificationModel(
    override val id: Int,
    override val createdAt: Int?,
    override val createdAtPrettyTime: String?,
    override val type: NotificationType?,
    override var unreadNotificationCount: Int = 0,
    val animeId: Int,
    val episode: Int,
    val contexts: List<String>?,
    val media: MediaModel?
) : NotificationModel()