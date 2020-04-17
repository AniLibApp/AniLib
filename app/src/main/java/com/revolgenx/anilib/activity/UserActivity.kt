package com.revolgenx.anilib.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.meta.UserMeta
import kotlinx.android.synthetic.main.user_activity_layout.*
import kotlin.math.abs

class UserActivity : BasePopupVideoActivity() {
    override val layoutRes: Int = R.layout.user_activity_layout
    private lateinit var userMeta: UserMeta

    companion object {
        fun openActivity(context: Context, userMeta: UserMeta) {
            context.startActivity(Intent(context, UserActivity::class.java).also {
                it.putExtra(USER_ACTIVITY_META_KEY, userMeta)
            })
        }

        const val USER_ACTIVITY_META_KEY = "USER_ACTIVITY_META_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userMeta = intent.getParcelableExtra(USER_ACTIVITY_META_KEY) ?: return
        setSupportActionBar(userToolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setToolbarTheme()

        userAppBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if ((abs(verticalOffset) >= (appBarLayout.totalScrollRange - userToolbar.height))) {
                return@OnOffsetChangedListener
            }

            userAvatar.pivotY = userAvatar.height.toFloat()
            userAvatar.pivotX = userAvatar.width.toFloat() / 2

            userAvatar.scaleX = 1 + (verticalOffset.toFloat() / (appBarLayout.totalScrollRange))
            userAvatar.scaleY = 1 + (verticalOffset.toFloat() / (appBarLayout.totalScrollRange))
        })

        userAvatar.hierarchy.apply {
            roundingParams =
                roundingParams?.setBorderColor(DynamicTheme.getInstance().get().backgroundColor)
        }

        val d = "res:/" + R.drawable.ic_launcher
        userAvatar.setImageURI(d)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


    private fun setToolbarTheme() {
        userCollapsingToolbar.setStatusBarScrimColor(DynamicTheme.getInstance().get().primaryColorDark)
        userCollapsingToolbar.setContentScrimColor(DynamicTheme.getInstance().get().primaryColor)
        userCollapsingToolbar.setCollapsedTitleTextColor(DynamicTheme.getInstance().get().tintPrimaryColor)
        userCollapsingToolbar.setBackgroundColor(DynamicTheme.getInstance().get().backgroundColor)
    }
}