package com.revolgenx.anilib.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.meta.*
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.common.preference.userName
import com.revolgenx.anilib.databinding.UserProfileAcitivtyLayoutBinding
import com.revolgenx.anilib.ui.fragment.home.profile.ProfileFragment

//todo://, handle review
class UserProfileActivity : BaseDynamicActivity<UserProfileAcitivtyLayoutBinding>() {

    companion object {
        fun openActivity(context: Context, userMeta: UserMeta) {
            context.startActivity(Intent(context, UserProfileActivity::class.java).also {
                it.putExtra(USER_ACTIVITY_META_KEY, userMeta)
            })
        }

        const val USER_ACTIVITY_META_KEY = "USER_ACTIVITY_META_KEY"
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): UserProfileAcitivtyLayoutBinding {
        return UserProfileAcitivtyLayoutBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent == null) return

        val userMeta = if (intent.action == Intent.ACTION_VIEW) {
            val data = intent.data ?: return
            val paths = data.pathSegments
            val userId = paths[1].toIntOrNull()
            val username = paths[1].toString()
            UserMeta(
                userId,
                if (userId == null) username else null,
                userId == userId() || username == userName()
            )
        } else {
            intent.getParcelableExtra(USER_ACTIVITY_META_KEY) ?: return
        }

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction().replace(R.id.user_profile_container_layout, ProfileFragment().also {
                it.arguments = bundleOf(ProfileFragment.USER_PROFILE_INFO_KEY to userMeta)
            }).commit()
        }
    }


}