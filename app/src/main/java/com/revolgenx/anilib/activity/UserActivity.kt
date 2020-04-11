package com.revolgenx.anilib.activity

import android.content.Context
import android.content.Intent
import com.revolgenx.anilib.R
import com.revolgenx.anilib.meta.UserMeta

class UserActivity : BaseDynamicActivity() {
    override val layoutRes: Int = R.layout.user_activity_layout

    companion object {
        fun openActivity(context: Context, userMeta: UserMeta) {
            context.startActivity(Intent(context, UserActivity::class.java))
        }

        const val USER_ACTIVITY_META_KEY = "USER_ACTIVITY_META_KEY"
    }

}