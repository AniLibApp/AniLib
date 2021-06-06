package com.revolgenx.anilib.social.data.model.reply

import android.text.Spanned
import com.revolgenx.anilib.data.model.BaseModel
import com.revolgenx.anilib.data.model.user.UserModel


class ActivityReplyModel : BaseModel() {
    var isLiked: Boolean = false
    var likeCount: Int = 0
    var likes: List<UserModel>? = null
    var text: String = ""
    var anilifiedText:String = ""
    var textSpanned: Spanned? = null
    var user: UserModel? = null
    var createdAt: String? = null
}