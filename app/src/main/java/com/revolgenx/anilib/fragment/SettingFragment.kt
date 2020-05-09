package com.revolgenx.anilib.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.ContainerActivity
import com.revolgenx.anilib.fragment.base.BaseToolbarFragment
import com.revolgenx.anilib.fragment.base.ParcelableFragment
import kotlinx.android.synthetic.main.setting_fragment_layout.*
import timber.log.Timber

class SettingFragment : BaseToolbarFragment() {

    override val title: Int= R.string.settings

    override val contentRes: Int by lazy {
        R.layout.setting_fragment_layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        themeItemView.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(
                R.id.fragmentContainer,
                newInstance(ThemeControllerFragment::class.java)
            )
                .addToBackStack(null)
                .commit()
        }
    }

}
