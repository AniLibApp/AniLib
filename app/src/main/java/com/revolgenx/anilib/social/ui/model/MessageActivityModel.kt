package com.revolgenx.anilib.social.ui.model

import android.text.Spanned
import androidx.core.text.toSpanned
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.social.markwon.anilify
import com.revolgenx.anilib.type.ActivityType
import com.revolgenx.anilib.user.ui.model.UserModel

data class MessageActivityModel(
    override val id: Int,
    override val userId: Int? = null,
    override val type: ActivityType = ActivityType.UNKNOWN__,
    override val replyCount: Int = 0,
    override val siteUrl: String? = null,
    override val isSubscribed: Boolean = false,
    override val likeCount: Int = 0,
    override val isLiked: Boolean = false,
    override val isPinned: Boolean = false,
    override val createdAt: Int = 0,
    override val createdAtPrettyTime: String = "",
    override val user: UserModel? = null,
    override val replies: List<ActivityReplyModel>? = null,
    override val likes: List<UserModel>? = null,
    val recipientId: Int? = null,
    val messengerId: Int? = null,
    val isPrivate: Boolean = false,
    val recipient: UserModel? = null,
    val messenger: UserModel? = null,
    val message: String? = null,
    val messageAnilified: String = "",
    val messageSpanned: Spanned = messageAnilified.toSpanned()
) : ActivityModel, BaseModel(id)