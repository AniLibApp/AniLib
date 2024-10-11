package com.revolgenx.anilib.social.ui.model

import android.text.Spanned
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.ActivityUnionQuery
import com.revolgenx.anilib.common.ext.prettyTime
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.fragment.GeneralTextActivity
import com.revolgenx.anilib.common.ext.markdown
import com.revolgenx.anilib.common.ext.anilify
import com.revolgenx.anilib.type.ActivityType
import com.revolgenx.anilib.user.ui.model.UserModel

data class TextActivityModel(
    override val id: Int,
    override val userId: Int? = null,
    override val type: ActivityType = ActivityType.UNKNOWN__,
    override val replyCount: Int = 0,
    override val siteUrl: String? = null,
    override val isPinned: Boolean = false,
    override val createdAt: Int = 0,
    override val createdAtPrettyTime: String = "",
    override val user: UserModel? = null,
    override val replies: List<ActivityReplyModel>? = null,
    override val likes: List<UserModel>? = null,
    override val isLiked: MutableState<Boolean> = mutableStateOf(false),
    override val likeCount: MutableIntState = mutableIntStateOf(0),
    override val isSubscribed: MutableState<Boolean> = mutableStateOf(false),
    override val isDeleted: MutableState<Boolean> = mutableStateOf(false),
    val text: String? = null,
    val anilifiedText: String = "",
    val textSpanned: Spanned? = null
) : ActivityModel()


fun GeneralTextActivity.toModel(): TextActivityModel {
    val anilifiedText = anilify(text)
    return TextActivityModel(
        id = id,
        text = text.orEmpty(),
        anilifiedText = anilifiedText,
        textSpanned = markdown.toMarkdown(anilifiedText),
        replyCount = replyCount,
        type = type!!,
        user = user?.activityUser?.toModel(),
        likes = likes?.mapNotNull {
            it?.likeUsers?.toModel()
        },
        siteUrl = siteUrl,
        createdAt = createdAt,
        createdAtPrettyTime = createdAt.toLong().prettyTime(),
        userId = userId,
        likeCount = mutableIntStateOf(likeCount) ,
        isSubscribed = mutableStateOf(isSubscribed ?: false) ,
        isLiked = mutableStateOf(isLiked ?: false) ,
    )
}
