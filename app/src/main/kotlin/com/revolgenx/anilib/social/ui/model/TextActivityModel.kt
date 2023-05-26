package com.revolgenx.anilib.social.ui.model

import android.text.Spanned
import com.revolgenx.anilib.ActivityUnionQuery
import com.revolgenx.anilib.common.ext.prettyTime
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.social.factory.markwon
import com.revolgenx.anilib.social.markwon.anilify
import com.revolgenx.anilib.type.ActivityType
import com.revolgenx.anilib.user.ui.model.UserModel

data class TextActivityModel(
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
    val text: String? = null,
    val anilifiedText: String = "",
    val textSpanned: Spanned? = null
) : ActivityModel, BaseModel(id)


fun ActivityUnionQuery.OnTextActivity.toModel(): TextActivityModel {
    val anilifiedText = anilify(text)
    return TextActivityModel(
        id = id,
        text = text ?: "",
        anilifiedText = anilifiedText,
        textSpanned = markwon.toMarkdown(anilifiedText),
        likeCount = likeCount,
        replyCount = replyCount,
        isSubscribed = isSubscribed ?: false,
        type = type!!,
        user = user?.activityUser?.toModel(),
        likes = likes?.mapNotNull {
            it?.likeUsers?.toModel()
        },
        siteUrl = siteUrl,
        createdAt = createdAt,
        createdAtPrettyTime = createdAt.toLong().prettyTime(),
        userId = userId,
        isLiked = isLiked ?: false,
    )
}
