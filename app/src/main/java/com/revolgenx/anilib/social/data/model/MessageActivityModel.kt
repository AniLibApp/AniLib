package com.revolgenx.anilib.social.data.model

import android.text.Spanned
import androidx.core.text.toSpanned
import com.revolgenx.anilib.user.data.model.UserModel

class MessageActivityModel : ActivityUnionModel() {
    var recipientId: Int? = null
    var messengerId: Int?
        set(value) {
            userId = value
        }
        get() = userId
    var message: String = ""
    var messageAnilified: String = message
    var messageSpanned: Spanned = messageAnilified.toSpanned()
    var isPrivate: Boolean = false
    var messenger: UserModel?
        get() = user
        set(value) {
            user = value
        }
}