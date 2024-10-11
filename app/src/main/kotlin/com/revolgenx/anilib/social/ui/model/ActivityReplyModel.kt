package com.revolgenx.anilib.social.ui.model

import android.text.Spanned
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.ActivityReplyQuery
import com.revolgenx.anilib.common.ext.prettyTime
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.fragment.ActivityReply
import com.revolgenx.anilib.common.ext.markdown
import com.revolgenx.anilib.common.ext.anilify
import com.revolgenx.anilib.type.ActivityType
import com.revolgenx.anilib.user.ui.model.UserModel

data class ActivityReplyModel(
    val id: Int,
    val activityId: Int? = null,
    val userId: Int? = null,
    val isLiked: MutableState<Boolean> = mutableStateOf(false),
    val likeCount: MutableIntState = mutableIntStateOf(0),
    val likes: List<UserModel>? = null,
    val user: UserModel? = null,
    val createdAt: Int = 0,
    val createdAtPrettyTime: String = "",

    val text: String = "",
    val anilifiedText: String = "",
    val textSpanned: Spanned? = null,
    val isDeleted: MutableState<Boolean> = mutableStateOf(false)
) : BaseModel

fun ActivityReply.toModel(): ActivityReplyModel {
    val anilifiedText = anilify(text)
    return ActivityReplyModel(
        id = id,
        activityId = activityId,
        userId = userId,
        isLiked = mutableStateOf(isLiked ?: false),
        likeCount = mutableIntStateOf(likeCount),
        text = text.orEmpty(),
        anilifiedText = anilifiedText,
        textSpanned = markdown.toMarkdown(anilifiedText),
        user = user?.activityUser?.toModel(),
        createdAt = createdAt,
        createdAtPrettyTime = createdAt.toLong().prettyTime()
    )
}