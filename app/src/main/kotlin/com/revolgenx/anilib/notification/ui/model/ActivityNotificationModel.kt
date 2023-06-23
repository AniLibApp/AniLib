package com.revolgenx.anilib.notification.ui.model

import com.revolgenx.anilib.social.ui.model.ActivityUnionModel
import com.revolgenx.anilib.type.NotificationType
import com.revolgenx.anilib.user.ui.model.UserModel

data class ActivityNotificationModel(
    override val id: Int,
    override val createdAt: Int?,
    override val createdAtPrettyTime: String?,
    override val type: NotificationType?,
    override var unreadNotificationCount: Int = 0,
    val userId: Int,
    val activityId: Int,
    val context: String?,
    val activity: ActivityUnionModel? = null,
    val user: UserModel?
) : NotificationModel()