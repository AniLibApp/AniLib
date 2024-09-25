package com.revolgenx.anilib.social.ui.model

import android.text.Spanned
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.ActivityUnionQuery
import com.revolgenx.anilib.common.ext.prettyTime
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.fragment.GeneralMessageActivity
import com.revolgenx.anilib.social.factory.markdown
import com.revolgenx.anilib.social.markdown.anilify
import com.revolgenx.anilib.type.ActivityType
import com.revolgenx.anilib.user.ui.model.UserModel

data class MessageActivityModel(
    override val id: Int,
    override val type: ActivityType = ActivityType.UNKNOWN__,
    override val replyCount: Int = 0,
    override val siteUrl: String? = null,
    override val isPinned: Boolean = false,
    override val createdAt: Int = 0,
    override val createdAtPrettyTime: String = "",
    val messengerId: Int? = null,
    val messenger: UserModel? = null,
    override val userId: Int? = messengerId,
    override val user: UserModel? = messenger,
    override val replies: List<ActivityReplyModel>? = null,
    override val likes: List<UserModel>? = null,
    override val isSubscribed: MutableState<Boolean> = mutableStateOf(false),
    override val likeCount: MutableIntState = mutableIntStateOf(0),
    override val isLiked: MutableState<Boolean> = mutableStateOf(false),
    override val isDeleted: MutableState<Boolean> = mutableStateOf(false),
    val recipientId: Int? = null,
    val isPrivate: Boolean = false,
    val recipient: UserModel? = null,
    val message: String? = null,
    val messageAnilified: String = "",
    val messageSpanned: Spanned? = null
) : ActivityModel()

fun GeneralMessageActivity.toModel(): MessageActivityModel {
    val anilifiedMsg = anilify(message)
    return MessageActivityModel(
        id = id,
        message = message ?: "",
        messageAnilified = anilifiedMsg,
        messageSpanned = markdown.toMarkdown(anilifiedMsg),
        recipientId = recipientId,
        messengerId = messengerId,
        messenger = messenger?.messengerUser?.toModel(),
        isPrivate = isPrivate ?: false,
        likes = likes?.mapNotNull {
            it?.likeUsers?.toModel()
        },
        replyCount = replyCount,
        type = type!!,
        siteUrl = siteUrl,
        createdAt = createdAt,
        createdAtPrettyTime = createdAt.toLong().prettyTime(),
        likeCount = mutableIntStateOf(likeCount) ,
        isSubscribed = mutableStateOf(isSubscribed ?: false) ,
        isLiked = mutableStateOf(isLiked ?: false) ,
    )
}
