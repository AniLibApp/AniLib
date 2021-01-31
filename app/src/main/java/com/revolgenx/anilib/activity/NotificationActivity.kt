package com.revolgenx.anilib.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.revolgenx.anilib.R
import com.revolgenx.anilib.databinding.NotificationActivityLayoutBinding
import com.revolgenx.anilib.ui.fragment.notification.NotificationFragment

class NotificationActivity : BaseDynamicActivity<NotificationActivityLayoutBinding>() {


    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): NotificationActivityLayoutBinding {
        return NotificationActivityLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbarActivityToolbar.dynamicToolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.notification_activity_container, NotificationFragment()).commit()
        }
    }

    override fun onNavigateUp(): Boolean {
        finish()
        return true
    }
}