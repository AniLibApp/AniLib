package com.revolgenx.anilib.social.data.model.reply

import android.text.Spanned
import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.user.data.model.UserModel


class ActivityReplyModel : BaseModel() {
    var activityId:Int? = null
    var userId:Int? = null
    var isLiked: Boolean = false
    var likeCount: Int = 0
    var likes: List<UserModel>? = null
    var text: String = ""
    var anilifiedText:String = ""
    var textSpanned: Spanned? = null
    var user: UserModel? = null
    var createdAt: String? = null
}